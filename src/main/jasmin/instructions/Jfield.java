package jasmin.instructions;

import ast.Type.Type;
import jasmin.utils.JasminUtil;

import static ast.TypeChecker.*;

public class Jfield extends JasminStmt {
    private String name;
    private Type type;

    public Jfield(String filedName, Type filedType) {
        this.name = filedName;
        this.type = filedType;
    }

    @Override
    public String toString() {
        return ".field protected " + name + " " + JasminUtil.toJasminType(type);
    }
}
