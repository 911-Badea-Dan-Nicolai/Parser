import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws Exception {
        Grammar grammar = new Grammar();

        grammar.readFromFile("src/program_inputs/g2.in");

        Scanner scanner = new Scanner();
        scanner.scan("p3.txt");
        Parser parser = new Parser(grammar);

        ArrayList<String> programPIF = grammar.readPIFFromFile("PIFp3.txt");
        parser.parse(programPIF);
    }
}