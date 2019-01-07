package jasmin.instructions;

import ast.Type.Type;
import jasmin.utils.JasminUtil;

public class Jget extends JasminStmt {
    private String refType;
    private String className;
    private String fieldName;
    private JgetType getType;

    public Jget(JgetType getType, String className, String fieldName, Type refType) {

        this.className = className;
        this.fieldName = fieldName;
        this.refType = JasminUtil.toJasminType(refType);;
        this.getType = getType;
    }

    public Jget(JgetType getType, String className, String fieldName, String refType) {

        this.className = className;
        this.fieldName = fieldName;
        this.refType = refType;
        this.getType = getType;
    }

    @Override
    public String toString() {
        return "get" + getType.toString().toLowerCase() + " " + className + "/" + fieldName + " " + refType;
    }
}
