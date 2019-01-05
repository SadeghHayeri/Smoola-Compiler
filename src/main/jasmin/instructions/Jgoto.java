package jasmin.instructions;

public class Jgoto extends JasminStmt {
    private String label;
    public Jgoto(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "goto " + label;
    }
}
