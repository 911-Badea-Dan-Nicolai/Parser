public class Main {
    public static void main(String[] args) throws Exception {
        Grammar grammar = new Grammar();
        grammar.readFromFile("src/program_inputs/g3.in");
        grammar.printTerminals();
        grammar.printProductions();
        grammar.printNonTerminals();
        grammar.printStartSymbol();
        System.out.println(grammar.getProductionsForNonTerminal("ifstmt"));
        System.out.println(grammar.isCFG());
    }
}