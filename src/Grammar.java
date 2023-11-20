import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.*;

public class Grammar {
    private Set<String> nonTerminals;
    private Set<String> terminals;
    private Map<String, List<String>> productions;
    private String startSymbol;

    public Grammar() {
        nonTerminals = new HashSet<>();
        terminals = new HashSet<>();
        productions = new HashMap<>();
    }

    public void readFromFile(String filename) throws IOException {
        File file = new File(filename);
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            if(line.startsWith("Begin state:")) {
                this.startSymbol = line.substring("Begin state:".length()).trim();
            }
            else if (line.startsWith("States:")) {
                String[] states = line.substring(8).split(", ");
                nonTerminals.addAll(Arrays.asList(states));
            } else if (line.startsWith("Alphabet:")) {
                String[] alphabet = line.substring(10).split(", ");
                terminals.addAll(Arrays.asList(alphabet));
            } else if (line.startsWith("Transitions:")) {
                while (scanner.hasNextLine()) {
                    line = scanner.nextLine();
                    if (line.isEmpty()) break;

                    String[] parts = line.split("::=");
                    String leftSide = parts[0].trim();
                    String[] rightSideRules = parts[1].split("\\|");

                    List<String> rulesList = productions.getOrDefault(leftSide, new ArrayList<>());
                    Collections.addAll(rulesList, rightSideRules);
                    productions.put(leftSide, rulesList);
                }
            }
        }

        scanner.close();
    }

    public void printStartSymbol() {
        System.out.println("Start symbol: " + startSymbol);
    }

    public void printNonTerminals() {
        System.out.println("NonTerminals: " + nonTerminals);
    }

    public void printTerminals() {
        System.out.println("Terminals: " + terminals);
    }

    public void printProductions() {
        System.out.println("Productions:");
        for (Map.Entry<String, List<String>> entry : productions.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }

    public List<String> getProductionsForNonTerminal(String nonTerminal) {
        return productions.get(nonTerminal);
    }

    public boolean isCFG() {
        for (String key : productions.keySet()) {
            if (!nonTerminals.contains(key) || key.contains(" ")) {
                return false;
            }
        }
        return true;
    }
}