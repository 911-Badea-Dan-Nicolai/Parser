import java.util.ArrayList;
import java.util.Objects;

class SymbolTable<T> {
    //Fields
    private final int size;
    private ArrayList<ArrayList<String>> symTbl;

    //Methods
    public SymbolTable(int size) {
        this.size = size;
        this.symTbl = new ArrayList<>(size);

        for (int index = 0; index < size; index++)
            symTbl.add(new ArrayList<>());
    }

    public int getSize() {
        return this.size;
    }

    public int[] add(String variableName) {
        if (contains(variableName))
            return search(variableName);

        int hashCode = hashCode(variableName);
        while (symTbl.size() <= hashCode)
            symTbl.add(new ArrayList<>());

        symTbl.get(hashCode).add(variableName);
        return search(variableName);
    }

    public int[] search(String variableName) {
        int hashCode = hashCode(variableName);
        int index = 0;

        for(String element: symTbl.get(hashCode)){
            if (element.equals(variableName))
                return new int[]{hashCode, index};
        }

        return new int[]{-1, -1};
    }

    public boolean contains(String variableName) {
        for (ArrayList<String> position : symTbl)
            if (position.contains(variableName))
                return true;
        return false;
    }

    public int hashCode(String variableName) {
        int sum = 0;
        for (char c : variableName.toCharArray())
            sum += c;

        return sum % size;
    }

    public void print() {
        for (int index = 0; index < symTbl.size(); index++) {
            ArrayList<String> entries = symTbl.get(index);

            if (entries.size() != 0)
                System.out.print(index);

            for (String entry : entries)
                System.out.print(" " + entry);

            if (entries.size() != 0)
                System.out.println();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int index = 0; index < symTbl.size(); index++) {
            ArrayList<String> entries = symTbl.get(index);

            if (entries.size() != 0)
                sb.append(index);

            for (String entry : entries)
                sb.append(" ").append(entry);

            if (entries.size() != 0)
                sb.append("\n");
        }
        return sb.toString();
    }
}