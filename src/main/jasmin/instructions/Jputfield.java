package jasmin.instructions;

import ast.Type.Type;
import jasmin.utils.JasminUtil;

public class Jputfield extends JasminStmt {
    private Type refType;
    private String className;
    private String fieldName;

    public Jputfield(String className, String fieldName, Type refType) {
        this.className = className;
        this.fieldName = fieldName;
        this.refType = refType;
    }

    @Override
    public String toString() {
        return "putfield " + className + "/" + fieldName + " " + JasminUtil.toJasminType(refType);
    }
}
