public class Main {
    public static void main(String[] args) throws Exception {
        Grammar grammar = new Grammar();
        grammar.readFromFile("src/program_inputs/g1.in");
//        grammar.printTerminals();
//        grammar.printProductions();
//        grammar.printNonTerminals();
//        System.out.println(grammar.getProductionsForNonTerminal("A"));
        System.out.println(grammar.isCFG());
    }
}