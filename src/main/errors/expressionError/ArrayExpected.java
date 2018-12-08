package errors.expressionError;

import ast.node.expression.ArrayCall;
import ast.node.expression.NewArray;

public class ArrayExpected extends ExpressionError {
    public ArrayExpected(ArrayCall arrayCall) {
        super(arrayCall.getLine());
    }

    @Override
    public String toString() {
        return String.format("Line:%d:array call on not array type", line);
    }
}
