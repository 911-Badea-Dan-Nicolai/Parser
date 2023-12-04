import java.util.*;

public class Parser {
    private final Grammar grammar;
    private String stateOfParsing; // q - normal state, b - back state, f - final state, e - error state
    private int currentSymbolPosition;
    private final Stack<String> workingStack;
    private final Stack<String> inputStack;


    public Parser(Grammar grammar) {
        this.grammar = grammar;
        this.stateOfParsing = "q";
        this.currentSymbolPosition = 0;
        this.workingStack = new Stack<>();
        this.inputStack = new Stack<>();
        inputStack.push(grammar.getStartSymbol().get(0));
    }

    public void parse(List<String> input) {
        while(!Objects.equals(stateOfParsing, "f") && !Objects.equals(stateOfParsing, "e")) {
            if(stateOfParsing.equals("q")) {
                if(currentSymbolPosition == input.size() && inputStack.isEmpty()) {
                    isSuccess();
                } else {
                    if(grammar.isNonTerminal(inputStack.peek())) {
                        expand();
                    } else {
                        if(currentSymbolPosition < input.size()){
                            if(Objects.equals(inputStack.peek(), input.get(currentSymbolPosition))) {
                                advance();
                            } else {
                                momentaryInsuccess();
                            }
                        } else {
                        momentaryInsuccess();
                        }
                    }
                }
            } else {

                if(stateOfParsing.equals("b")) {
                    if(currentSymbolPosition >= input.size()) {
                        back();
                    } else if (Objects.equals(workingStack.peek(), input.get(currentSymbolPosition-1))) {
                        back();
                    } else {
                        anotherTry();
                    }
                }
            }
        }

        if(stateOfParsing.equals("e")) {
            System.out.println("Error");
        } else {
            System.out.println("Sequence accepted");
            generateParsingTreeOutput(this.workingStack);
        }
    }

    private void isSuccess() {
        stateOfParsing = "f";
    }

    private void anotherTry() {
        String currentNonTerminal = workingStack.peek();
        String[] parts = currentNonTerminal.split(" ");
        String baseNonTerminal = parts[0];
        int index = Integer.parseInt(parts[1]);
        String nextProduction = grammar.getNextProductionForNonTerminal(baseNonTerminal, index);

        for(int i = 0; i<grammar.getProductionLen(baseNonTerminal, index - 1); i++) {
            inputStack.pop();
        }

        if (nextProduction != null) {
            workingStack.pop();
            workingStack.push(baseNonTerminal + " " + (index + 1));
            String[] tokens = nextProduction.split("\\s+");
            List<String> tokenList = Arrays.asList(tokens);
            Collections.reverse(tokenList);
            for (String token : tokenList) {
                inputStack.push(token);
            }

            stateOfParsing = "q";
        } else {
            inputStack.push(baseNonTerminal);
            workingStack.pop();
        }


        if(currentSymbolPosition == 1 && stateOfParsing.equals("s")){
            stateOfParsing = "e";
        }
    }

    private void back() {
        currentSymbolPosition--;
        inputStack.push(workingStack.pop());
    }

    private void momentaryInsuccess() {
        stateOfParsing = "b";
    }

    private void advance() {
        workingStack.push(inputStack.pop());
        currentSymbolPosition++;
    }

    private void expand() {
        workingStack.push(inputStack.peek() + " 1");
        String production = grammar.getProductionsForNonTerminal(inputStack.pop()).get(0);

        String[] tokens = production.split("\\s+");
        List<String> tokenList = Arrays.asList(tokens);
        Collections.reverse(tokenList);
        for(String token : tokenList) {
            inputStack.push(token);
        }
    }

    public void generateParsingTreeOutput(Stack<String> invertedNodes ) {
        Stack <String> nodes = new Stack<>();
        Stack <String> expandedNodes = new Stack<>();
        while(!invertedNodes.isEmpty()) {
            nodes.push(invertedNodes.pop());
        }

        while(!nodes.isEmpty()) {
            String node = nodes.pop();
            if(node.contains(" ")) {
                String [] parts = node.split(" ");
                String baseNonTerminal = parts[0];
                int index = Integer.parseInt(parts[1]);
                var productionsString = grammar.getProductionForNonTerminal(baseNonTerminal, index - 1);
                var productions = productionsString.split(" ");

                int fdslaf = 0;
            }
        }
    }
}
