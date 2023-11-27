import java.util.*;

public class Parser {
    private final Grammar grammar;
    private List<String> tokens; // Input tokens
    private int currentPosition;

    public Parser(Grammar grammar) {
        this.grammar = grammar;
    }

    public void parse(List<String> tokens) {
        this.tokens = tokens;
        this.currentPosition = 0;

        boolean parsedSuccessfully = parseStartSymbol();

        if (parsedSuccessfully && isSuccess()) {
            System.out.println("Parsing successful!");
        } else {
            System.out.println("Parsing failed.");
        }
    }

    private boolean parseStartSymbol() {
        String startSymbol = grammar.getStartSymbol().get(0); // Assume this method returns the start symbol of the grammar
        return expand(startSymbol);
    }

    private boolean tryProduction(String production) {
        String[] symbols = production.split(" ");

        for (String symbol : symbols) {
            int startPosition = currentPosition;
            boolean result;

            if (grammar.isNonTerminal(symbol)) {
                result = expand(symbol); // Expand non-terminal
            } else {
                result = advance(symbol); // Try to match terminal
            }

            if (!result) {
                anotherTry(startPosition); // Another try after momentary insuccess
                return false;
            }
        }

        return true; // Success for this production
    }

    private boolean expand(String nonTerminal) {
        if (!grammar.isNonTerminal(nonTerminal)) {
            throw new IllegalArgumentException(nonTerminal + " is not a non-terminal");
        }

        List<String> productions = grammar.getProductionsForNonTerminal(nonTerminal);
        for (String production : productions) {
            if (tryProduction(production)) {
                return true; // Success for this production
            }
            // Momentary insuccess is handled implicitly here
        }

        return false; // Momentary insuccess for all productions of this non-terminal
    }

    private boolean anotherTry(int startPosition) {
        backtrack(startPosition - 1); // Reset to the previous state
        return expand(grammar.ge); // Try the next production
    }

    private boolean isSuccess() {
        return currentPosition == tokens.size(); // Check if we reached the end of the input
    }

    // Other methods corresponding to non-terminals

    private boolean advance(String expectedToken) {
        if (currentPosition < tokens.size() && tokens.get(currentPosition).equals(expectedToken)) {
            currentPosition++;
            return true;
        }
        return false;
    }

    private void backtrack(int position) {
        currentPosition = position;
    }

    // Methods for other moves (expand, momentary insuccess, another try, success)
}
