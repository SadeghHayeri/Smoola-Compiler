package jasmin.instructions;

import ast.Type.Type;
import static ast.TypeChecker.*;

public class Jload extends JasminStmt {
    private JrefType refType;
    private int index;
    private boolean isIALoad;

    public Jload() {
        this.isIALoad = true;
    }

    public Jload(JrefType refType, int index) {
        this.isIALoad = false;
        this.refType = refType;
        this.index = index;
    }

    public Jload(Type type, int index) {
        this.isIALoad = false;
        this.refType = (isBoolean(type) || isInt(type)) ? JrefType.i : JrefType.a;
        this.index = index;
    }

    @Override
    public String toString() {
        if(isIALoad)
            return "iaload";
        else
            return refType.toString() + "load " + index;
    }
}
