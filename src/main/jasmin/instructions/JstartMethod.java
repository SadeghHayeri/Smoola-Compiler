package jasmin.instructions;

import ast.Type.Type;
import jasmin.utils.JasminUtil;

import java.util.ArrayList;

public class JstartMethod extends JasminStmt {
    private String name;
    private String args;
    private String returnType;

    public JstartMethod(String methodName, ArrayList<Type> args, Type returnType) {
        this.name = methodName;
        this.returnType = JasminUtil.toJasminType(returnType);
        this.args = "";
        for(Type type : args)
            this.args += (JasminUtil.toJasminType(type));
    }

    public JstartMethod(String methodName, String args, String returnType) {
        this.name = methodName;
        this.args = args;
        this.returnType = returnType;
    }

    @Override
    public String toString() {
        return ".method public " + name + "(" + args + ")" + returnType;
    }
}
