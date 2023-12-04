import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Stack;

public class ParserOutput {
    private final ArrayList<Node> parseTable;
    private final Grammar grammar;

    public ParserOutput(Grammar grammar) {
        this.parseTable = new ArrayList<>();
        this.grammar = grammar;
    }

    public void generateParsingTreeOutput(Stack<String> invertedNodes) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("output.out"))) {
            Stack<String> nodes = new Stack<>();
            Stack<String> expandedNodes = new Stack<>();

            while (!invertedNodes.isEmpty()) {
                nodes.push(invertedNodes.pop());
            }

            this.parseTable.add(new Node(parseTable.size() + 1, nodes.peek().split(" ")[0], 0, 0));
            int currentParentIndex = 1;

            while (!nodes.isEmpty()) {
                String node = nodes.pop();
                if (node.contains(" ")) {
                    String[] parts = node.split(" ");
                    String baseNonTerminal = parts[0];
                    int index = Integer.parseInt(parts[1]);
                    var productionsString = grammar.getProductionForNonTerminal(baseNonTerminal, index - 1);
                    var productions = productionsString.split(" ");

                    for (int i = productions.length - 1; i >= 0; i--) {
                        expandedNodes.push(productions[i]);
                    }

                    for (String item : productions) {
                        if (Objects.equals(item, productions[0]))
                            this.parseTable.add(new Node(parseTable.size() + 1, item, currentParentIndex, 0));
                        else
                            this.parseTable.add(new Node(parseTable.size() + 1, item, currentParentIndex, parseTable.size()));
                    }

                    while (!expandedNodes.isEmpty() && !grammar.isNonTerminal(expandedNodes.peek())) {
                        expandedNodes.pop();
                        nodes.pop();
                        currentParentIndex++;
                    }

                    if (!expandedNodes.isEmpty())
                        expandedNodes.pop();
                }
            }

            writer.println("index | info | parent | right sibling");
            System.out.println("index | info | parent | right sibling");
            for (Node n : parseTable) {
                writer.println(n);
                System.out.println(n);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
