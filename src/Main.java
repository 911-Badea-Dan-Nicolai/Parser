import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws Exception {
        Grammar grammar = new Grammar();

        grammar.readFromFile("src/program_inputs/g1.in");

        Parser parser = new Parser(grammar);
        parser.parse(new ArrayList<String>(){{add("a"); add("a"); add("c"); add("b"); add("c");}});

    }
}