package jasmin.instructions;

import ast.Type.Type;
import jasmin.utils.JasminUtil;

import java.util.ArrayList;

public class Jinvoke extends JasminStmt {
    private JinvokeType invokeType;
    private String className;
    private String methodName;
    private String args;
    private String returnType;

    public Jinvoke(JinvokeType invokeType, String className, String methodName, ArrayList<Type> argsType, Type returnType) {
        this.invokeType = invokeType;
        this.className = className;
        this.methodName = methodName;
        this.returnType = JasminUtil.toJasminType(returnType);
        this.args = "";
        for(Type type : argsType)
            this.args += (JasminUtil.toJasminType(type) + "; ");
    }

    public Jinvoke(JinvokeType invokeType, String className, String methodName, String args, String returnType) {
        this.invokeType = invokeType;
        this.className = className;
        this.methodName = methodName;
        this.args = args;
        this.returnType = returnType;
    }

    @Override
    public String toString() {
        return "invoke" + invokeType.toString().toLowerCase() + " " + className + "/" + methodName + "(" + args + ")" + returnType;
    }
}
