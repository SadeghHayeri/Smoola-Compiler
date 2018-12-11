package errors.methodError;

import ast.Type.Type;
import ast.node.expression.Expression;
import ast.node.expression.MethodCall;

public class classExpected extends MethodError {

    public classExpected(MethodCall methodCall) {
        super(methodCall.getLine());
    }

    @Override
    public String toString() {
        return String.format("Line:%d:Method call on non classType", line);
    }
}