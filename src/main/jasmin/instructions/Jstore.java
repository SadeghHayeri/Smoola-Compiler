package jasmin.instructions;

public class Jstore extends JasminStmt {
    private JrefType refType;

    public Jstore(JrefType refType) {
        this.refType = refType;
    }
}
