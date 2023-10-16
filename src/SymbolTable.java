import java.util.ArrayList;
import java.util.Objects;

class SymbolTable<T> {
    private final int size;
    private ArrayList<ArrayList<String>> symTbl;

    public SymbolTable() {
        this.size = 100;
        this.symTbl = new ArrayList<>(size);

        for (int i = 0; i < size; i++)
            symTbl.add(new ArrayList<>());
    }

    public int getSize() {
        return this.size;
    }

    public void add(String var) {
        int hashCode = hashCode(var);
        while (symTbl.size() <= hashCode)
            symTbl.add(new ArrayList<>());

        symTbl.get(hashCode).add(var);
    }

    public int[] search(String var) {
        int hashCode = hashCode(var);
        int i = 0;

        while(!Objects.equals(symTbl.get(hashCode).get(i), var))
            i++;

        return new int[]{hashCode, i};
    }

    public int hashCode(String var) {
        int sum = 0;
        for (char c : var.toCharArray())
            sum += c;

        return sum % size;
    }

    public void print() {
        for (int i = 0; i < symTbl.size(); i++) {
            ArrayList<String> entries = symTbl.get(i);

            if (entries.size() != 0)
                System.out.print(i);

            for (String entry : entries) {
                System.out.print(" " + entry);
            }

            if (entries.size() != 0)
                System.out.println();
        }
    }
}