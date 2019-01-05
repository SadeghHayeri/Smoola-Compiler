package jasmin.instructions;

public class Jif extends JasminStmt {
    private JifOperator ifOperator;
    private String label;

    public Jif(JifOperator operator, String label) {
        this.ifOperator = operator;
        this.label = label;
    }

    @Override
    public String toString() {
        return "if" + ifOperator.toString() + " " + label;
    }
}
