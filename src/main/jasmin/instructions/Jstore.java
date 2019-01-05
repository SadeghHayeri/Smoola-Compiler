package jasmin.instructions;

import ast.Type.Type;

import static ast.TypeChecker.isBoolean;
import static ast.TypeChecker.isInt;

public class Jstore extends JasminStmt {
    private JrefType refType;
    private int index;
    private boolean isIALoad;

    public Jstore() {
        this.isIALoad = true;
    }

    public Jstore(JrefType refType, int index) {
        this.isIALoad = false;
        this.refType = refType;
        this.index = index;
    }

    public Jstore(Type type, int index) {
        this.isIALoad = false;
        this.refType = (isBoolean(type) || isInt(type)) ? JrefType.i : JrefType.a;
        this.index = index;
    }

    @Override
    public String toString() {
        if(isIALoad)
            return "iastore";
        else
            return refType.toString() + "store " + index;
    }
}
