package jasmin.instructions;

public class Jif_icmp extends JasminStmt {
    private JrefType refType;
    private JifOperator ifOperator;
    private String label;

    public Jif_icmp(JrefType refType, JifOperator operator, String label) {
        assert refType != JrefType.a || operator == JifOperator.eq || operator == JifOperator.ne;

        this.refType = refType;
        this.ifOperator = operator;
        this.label = label;
    }
}
