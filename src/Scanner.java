import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;

public class Scanner {
    // Fields
    private String programFile;
    private List<String> tokens;
    private List<String> reservedWords;
    private SymbolTable<String> symbolTable;
    private List<AbstractMap.SimpleEntry<String, int[]>> PIF;
    private int currentCharacter = 0;
    private int currentLine = 1;

    // Methods
    public Scanner() throws IOException {
        try{
            this.symbolTable = new SymbolTable<String>(100);
            this.PIF =  new ArrayList<>();
            this.reservedWords = new ArrayList<>();
            this.tokens = new ArrayList<>();
            readTokens();
        }catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public void setProgramFile(String programFile) {
        this.programFile = programFile;
    }

    private void readTokens() throws IOException {
        Set<String> reservedWordsSet = Set.of(
                "grid", "char", "const", "run", "otherwise", "if", "int", "program", "read", "then", "var", "while", "write", "start", "finish", "boolean"
        );

        File filePath = new File("src/program_inputs/token.in");
        BufferedReader br = Files.newBufferedReader(filePath.toPath());
        String line;

        while ( (line = br.readLine()) != null) {
            String[] tokenArray = line.split(" ");
            String token = tokenArray[0];

            if (reservedWordsSet.contains(token))
                reservedWords.add(token);
            else
                tokens.add(token);
        }
    }

    private void ignoreSpaces() {
        while (currentCharacter < programFile.length() && Character.isWhitespace(programFile.charAt(currentCharacter))) {
            if (programFile.charAt(currentCharacter) == '\n')
                currentLine++;
            currentCharacter++;
        }
    }

    private boolean isIdentifierValid(String identifier) {
        if (reservedWords.contains(identifier))
            return false;

        if (Pattern.matches("^[A-Za-z]+$", identifier))
            return true;

        return symbolTable.contains(identifier);
    }

    private boolean isIdentifier() {
        var identifierPatter = Pattern.compile("^([A-Za-z])+");
        var matcher = identifierPatter.matcher(programFile.substring(currentCharacter));

        if(!matcher.find())
            return false;

        var identifier = matcher.group();
        if(!isIdentifierValid(identifier))
            return false;

        currentCharacter += identifier.length();

        int[] position = symbolTable.add(identifier);

        PIF.add(new AbstractMap.SimpleEntry<>("identifier", position));
        System.out.println(identifier);
        return true;
    }

    private boolean isIntConstant() {
        var intConstPattern = Pattern.compile("^[+-]?[1-9][0-9]*|0");
        var matcher = intConstPattern.matcher(programFile.substring(currentCharacter));

        if(!matcher.find())
            return false;

        var intConst = matcher.group(0);
        currentCharacter += intConst.length();

        int[] position = symbolTable.add(intConst);

        PIF.add(new AbstractMap.SimpleEntry<>("int const", position));
        System.out.println(intConst);
        return true;
    }

    private boolean isStringConstant() {
        var stringConstPattern = Pattern.compile("^\"[a-zA-Z0-9_ -+*/<=>.:;]*\"");
        var matcher = stringConstPattern.matcher(programFile.substring(currentCharacter));

        if(!matcher.find())
            return false;

        var stringConst = matcher.group(0);
        currentCharacter += stringConst.length();

        int[] position = symbolTable.add(stringConst);

        PIF.add(new AbstractMap.SimpleEntry<>("string const", position));
        System.out.println(stringConst);
        return true;
    }

    private boolean addToPIF(String token) {
        currentCharacter += token.length();
        PIF.add(new AbstractMap.SimpleEntry<>(token, new int[]{-1, -1}));
        System.out.println(token);
        return true;
    }

    private boolean isToken() {
        String nextPossibleToken = programFile.substring(currentCharacter).split(" ")[0];

        for (String reservedToken : reservedWords) {
            if (nextPossibleToken.startsWith(reservedToken)) {
                var tokenInWord = "^[a-zA-Z0-9_]*" + reservedToken + "[a-zA-Z0-9_]+";
                if(Pattern.compile(tokenInWord).matcher(nextPossibleToken).find())
                    return false;

                return addToPIF(reservedToken);
            }
        }

        for (String token: tokens) {
            if (token.equals(nextPossibleToken))
                return addToPIF(token);
            else if (nextPossibleToken.startsWith(token))
                return addToPIF(token);
        }

        return false;
    }

    private void nextToken() throws Exception {
        ignoreSpaces();
        if (currentLine == programFile.length())
            return;
        if (isIdentifier())
            return;
        if (isIntConstant())
            return;
        if (isStringConstant())
            return;
        if (isToken())
            return;
        throw new Exception("Lexical error at: " + currentLine + " " + currentCharacter);
    }

    public void scan(String programFileName) throws Exception {
        Path filePath = Path.of("src/program_inputs/" + programFileName);
        setProgramFile(Files.readString(filePath));

        currentCharacter = 0;
        currentLine = 1;
        PIF = new ArrayList<>();
        symbolTable = new SymbolTable<>(100);

        while (currentCharacter<programFile.length())
            nextToken();

        FileWriter fileWriter = new FileWriter("PIF" + programFileName);
        for (var pair : PIF) {
            fileWriter.write(pair.getKey() + " : (" + pair.getValue()[0] + ", " + pair.getValue()[1] + ")\n");
        }
        fileWriter.close();
        fileWriter = new FileWriter("ST" + programFileName);
        fileWriter.write(symbolTable.toString());
        fileWriter.close();
        System.out.println("Lexically correct");
    }
}