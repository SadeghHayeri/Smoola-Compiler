package jasmin.instructions;

public class Jreturn extends JasminStmt {
    private JrefType refType;

    public Jreturn(JrefType jrefType) {
        this.refType = jrefType;
    }

    @Override
    public String toString() {
        switch (refType) {
            case a:
                return "areturn";
            case i:
                return "ireturn";
            case VOID:
                return "return";
        }
        return "return";
    }
}
