package jasmin.instructions;

import ast.Type.Type;
import jasmin.utils.JasminUtil;

public class Jgetfield extends JasminStmt {
    private Type refType;
    private String className;
    private String fieldName;

    public Jgetfield(String className, String fieldName, Type refType) {
        this.className = className;
        this.fieldName = fieldName;
        this.refType = refType;
    }

    @Override
    public String toString() {
        return "getfield " + className + "/" + fieldName + " " + JasminUtil.toJasminType(refType);
    }
}
