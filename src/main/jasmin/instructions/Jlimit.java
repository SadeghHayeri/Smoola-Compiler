package jasmin.instructions;

public class Jlimit extends JasminStmt {
    private String label;
    private int value;

    public Jlimit(String label, int value) {
        this.label = label;
        this.value = value;
    }

    @Override
    public String toString() {
        return ".limit " + label + " " + value;
    }
}
