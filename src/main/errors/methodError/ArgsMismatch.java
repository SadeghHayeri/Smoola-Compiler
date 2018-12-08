package errors.methodError;

import ast.Type.Type;
import ast.node.expression.Expression;
import ast.node.expression.MethodCall;

public class ArgsMismatch extends MethodError {

    public ArgsMismatch(MethodCall methodCall) {
        super(methodCall.getLine());
    }

    @Override
    public String toString() {
        return String.format("Line:%d:MethodCall Args mismatch", line);
    }
}
