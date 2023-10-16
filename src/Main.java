import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        SymbolTable<Integer> symbolTable = new SymbolTable<>(100);
        symbolTable.add("a");
        symbolTable.add("b");
        symbolTable.add("c");
        symbolTable.add("cb");
        System.out.println(Arrays.toString(symbolTable.add("ca")));

        symbolTable.print();
        System.out.println(Arrays.toString(symbolTable.search("cb")));
    }
}