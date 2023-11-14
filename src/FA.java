import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class FA {
    //Fields
    private boolean isDeterministic;
    private String programFile;
    private final List<String> setOfStates;
    private final List<String> alphabet;
    private final List<String> transitions;
    private String initialState;
    private final List<String> finalStates;

    //Methods
    public FA() {
        this.setOfStates = new LinkedList<>();
        this.alphabet = new LinkedList<>();
        this.transitions = new LinkedList<>();
        this.initialState = "";
        this.finalStates = new LinkedList<>();
        checkDeterminism();
    }

    public void setProgramFile(String programFile) {
        this.programFile = programFile;
    }

    private void readFile() throws IOException {
        System.out.println(programFile);
        BufferedReader reader = new BufferedReader(new FileReader(programFile));
        String currentLine;

        while ((currentLine = reader.readLine()) != null) {
            if (currentLine.startsWith("States: ")) {
                String states = currentLine.substring("States:".length()).trim();
                String[] stateArray = states.split(", ");

                this.setOfStates.addAll(Arrays.asList(stateArray));
            }

            if (currentLine.startsWith("Alphabet: ")) {
                String alphabet = currentLine.substring("Alphabet:".length()).trim();
                String[] alphabetArray = alphabet.split(", ");

                this.alphabet.addAll(Arrays.asList(alphabetArray));
            }

            if (currentLine.startsWith("Initial State: "))
                this.initialState = currentLine.substring("Initial State:".length()).trim();

            if (currentLine.startsWith("Final States: ")) {
                String states = currentLine.substring("Final States:".length()).trim();
                String[] stateArray = states.split(", ");

                this.finalStates.addAll(Arrays.asList(stateArray));
            }

            if (currentLine.contains(",")) {
                String[] parts = currentLine.split(",");

                if (parts.length == 3) {
                    String currentState = parts[0].trim();
                    String inputSymbol = parts[1].trim();
                    String nextState = parts[2].trim();

                    String transition = "{" + currentState + ", " + inputSymbol + ", " + nextState + "}";

                    this.transitions.add(transition);
                }
            }
        }
    }

    public void display(String fileName) throws IOException {
        Path filePath = Path.of("src/program_inputs/" + fileName);
        setProgramFile(filePath.toString());
        readFile();

        //checkDeterminism();

        System.out.println("Set of States: " + setOfStates);
        System.out.println("Alphabet: " + alphabet);
        System.out.println("Transitions: " + transitions);
        System.out.println("Initial State: " + initialState);
        System.out.println("Final States: " + finalStates);
    }

    public boolean isAccepted(String inputSequence) {
        if (!isDeterministic) {
            System.out.println("The FA is not deterministic.");
            return false;
        }

        String currentState = initialState;

        for (char inputSymbol : inputSequence.toCharArray()) {
            String nextState = null;

            for (String transition : transitions) {
                String[] parts = transition.split(", ");
                String fromState = parts[0].trim().replaceAll("[{}]", "");
                String symbol = parts[1];
                String toState = parts[2].trim().replaceAll("[{}]", "");

                if (fromState.equals(currentState) && symbol.equals(String.valueOf(inputSymbol))) {
                    nextState = toState;
                    break;
                }
            }

            currentState = nextState;
        }

        if (finalStates.contains(currentState)) {
            System.out.println("The input sequence is accepted by the DFA.");
            return true;
        }
        else
            System.out.println("The input sequence is not accepted by the DFA.");
        return false;
    }

    private void checkDeterminism() {
        isDeterministic = true;
        Set<String> uniqueTransitions = new HashSet<>();

        for (String transition : transitions) {
            String[] parts = transition.split(", ");
            String fromState = parts[0].trim().replaceAll("[{}]", "");
            String inputSymbol = parts[1];

            String transitionKey = fromState + "," + inputSymbol;

            if (uniqueTransitions.contains(transitionKey)) {
                isDeterministic = false;
                return;
            }

            uniqueTransitions.add(transitionKey);
        }
    }
}