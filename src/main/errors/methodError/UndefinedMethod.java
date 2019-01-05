package errors.methodError;

import ast.node.expression.Expression;
import ast.node.expression.MethodCall;
import errors.ErrorPhase;
import ast.ErrorChecker;

public class UndefinedMethod extends MethodError {

    private String className;
    private String methodName;
    public UndefinedMethod(MethodCall methodCall, String className) {
        super(methodCall.getLine());
        Expression classExp = methodCall.getInstance();
        this.className = className;
        this.methodName = methodCall.getMethodName().getName();
    }

    @Override
    public String toString() {
        return String.format("Line:%d:there is no method named %s in class %s", line, methodName, className);
    }

    @Override
    public ErrorPhase whichPhase() {
        return ErrorPhase.PHASE3;
    }
}
