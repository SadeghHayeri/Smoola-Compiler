package errors.methodError;

import ast.node.expression.Expression;
import ast.node.expression.MethodCall;

public class UndefinedMethod extends MethodError {

    private String className;
    private String methodName;
    public UndefinedMethod(MethodCall methodCall) {
        super(methodCall.getLine());
        Expression classExp = methodCall.getInstance();
        //TODO: exp -> class
        this.className = "TODO";
        this.methodName = methodCall.getMethodName().getName();
    }

    @Override
    public String toString() {
        return String.format("Line:%d:there is no method named %s in class %s", line, methodName, className);
    }
}
