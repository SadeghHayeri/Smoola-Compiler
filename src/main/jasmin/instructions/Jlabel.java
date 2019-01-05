package jasmin.instructions;

public class Jlabel extends JasminStmt {
    private String label;
    public Jlabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label + ":";
    }
}
