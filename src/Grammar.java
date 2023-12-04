import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Scanner;

public class Grammar {
    private final Set<String> nonTerminals;
    private final Set<String> terminals;
    private final Map<List<String>, List<String>> productions;
    private List<String> startSymbol;
    private final Map<String, Integer> productionIndices;

    public Set<String> getNonTerminals() {
        return nonTerminals;
    }

    public Set<String> getTerminals() {
        return terminals;
    }

    public Map<List<String>, List<String>> getProductions() {
        return productions;
    }

    public List<String> getStartSymbol() {
        return startSymbol;
    }

    public void setStartSymbol(List<String> startSymbol) {
        this.startSymbol = startSymbol;
    }

    public Grammar() {
        nonTerminals = new HashSet<>();
        terminals = new HashSet<>();
        productions = new HashMap<>();
        productionIndices = new HashMap<>();
    }

    public void readFromFile(String filename) throws IOException {
        File file = new File(filename);
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            if (line.startsWith("Begin state:")) {
                this.startSymbol = List.of(line.substring("Begin state:".length()).trim());
            } else if (line.startsWith("Non Terminals:")) {
                String[] states = line.substring(15).split(", ");
                nonTerminals.addAll(Arrays.asList(states));
            } else if (line.startsWith("Terminals:")) {
                String[] alphabet = line.substring(11).split(", ");
                terminals.addAll(Arrays.asList(alphabet));
            } else if (line.startsWith("Productions:")) {
                while (scanner.hasNextLine()) {
                    line = scanner.nextLine();
                    if (line.isEmpty()) break;

                    String[] parts = line.split("::=");
                    String leftSide = parts[0].trim();
                    String[] rightSideRules = parts[1].split("\\|");

                    List<String> leftSideList = List.of(leftSide);
                    List<String> rulesList = productions.getOrDefault(leftSideList, new ArrayList<>());
                    Collections.addAll(rulesList, rightSideRules);
                    productions.put(leftSideList, rulesList);

                    productionIndices.putIfAbsent(leftSide, 0);
                }
            }
        }
        initializeProductionIndices();
        scanner.close();
    }

    public void printStartSymbol() {
        System.out.println("Start symbol: " + startSymbol.get(0));
    }

    public void printNonTerminals() {
        System.out.println("NonTerminals: " + nonTerminals);
    }

    public void printTerminals() {
        System.out.println("Terminals: " + terminals);
    }

    public void printProductions() {
        System.out.println("Productions:");
        for (Map.Entry<List<String>, List<String>> entry : productions.entrySet()) {
            List<String> leftSideList = entry.getKey();
            List<String> productionList = entry.getValue();

            System.out.print(leftSideList.get(0) + " -> ");
            for (int i = 0; i < productionList.size(); i++) {
                System.out.print(productionList.get(i));

                if (i < productionList.size() - 1) {
                    System.out.print(" | ");
                }
            }
            System.out.println();
        }
    }

    public List<String> getProductionsForNonTerminal(String nonTerminal) {
        return productions.get(Collections.singletonList(nonTerminal));
    }

    public boolean isCFG() {
        if (!nonTerminals.contains(startSymbol.get(0))) {
            return false;
        }
        for (Map.Entry<List<String>, List<String>> entry : productions.entrySet()) {
            List<String> leftSideList = entry.getKey();
            List<String> productionList = entry.getValue();

            if (!nonTerminals.contains(leftSideList.get(0))) {
                return false;
            }

            for (String production : productionList) {
                String[] rhsSymbols = production.trim().split(" ");

                for (String rhsSymbol : rhsSymbols) {
                    if (!nonTerminals.contains(rhsSymbol) && !terminals.contains(rhsSymbol) && !rhsSymbol.equals("ε")) {
                        System.out.println(rhsSymbol);
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean isNonTerminal(String symbol) {
        return nonTerminals.contains(symbol);
    }

    public String getNextProductionForNonTerminal(String nonTerminal, int index) {
        List<String> productionsList = getProductionsForNonTerminal(nonTerminal);

        if (index < productionsList.size()) {
            productionIndices.put(nonTerminal, index + 1);
            return productionsList.get(index);
        }

        return null;
    }

    public String getProductionForNonTerminal(String nonTerminal, int index) {
        List<String> productionsList = getProductionsForNonTerminal(nonTerminal);
        return productionsList.get(index);
    }

    public int getProductionLen(String nonTerminal, int index) {
        List<String> productionsList = getProductionsForNonTerminal(nonTerminal);
        String production = productionsList.get(index);
        return production.split(" ").length;
    }

    public void resetProductionIndex(String nonTerminal) {
        productionIndices.put(nonTerminal, 0);
    }

    public void initializeProductionIndices() {
        for (String nonTerminal : nonTerminals) {
            List<String> productionsList = getProductionsForNonTerminal(nonTerminal);

            if (!productionsList.isEmpty()) {
                productionIndices.put(nonTerminal, 0);
            }
        }
    }
}