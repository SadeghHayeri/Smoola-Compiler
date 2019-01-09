package jasmin.instructions;

import ast.Type.Type;
import jasmin.utils.JasminUtil;

import static ast.TypeChecker.isString;

public class Jfield extends JasminStmt {
    private String name;
    private String type;

    public Jfield(String filedName, Type filedType) {
        this.name = filedName;
        this.type = JasminUtil.toJasminType(filedType);
    }


    @Override
    public String toString() {
        return ".field protected " + name + " " + type;
    }
}
