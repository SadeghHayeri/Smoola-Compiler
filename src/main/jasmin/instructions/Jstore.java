package jasmin.instructions;

import ast.Type.Type;

public class Jstore extends JasminStmt {
    private JrefType refType;

    public Jstore() {
        //TODO: iastore
    }

    public Jstore(JrefType type, int index) {
        this.refType = refType;
    }

    public Jstore(Type type, int index) {

    }
}
