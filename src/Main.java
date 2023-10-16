import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        SymbolTable<Integer> symbolTable = new SymbolTable<>();
        symbolTable.add("a");
        symbolTable.add("b");
        symbolTable.add("c");
        symbolTable.add("ca");
        symbolTable.add("cb");

        symbolTable.print();
        System.out.println(Arrays.toString(symbolTable.search("cb")));
    }
}