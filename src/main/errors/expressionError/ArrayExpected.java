package errors.expressionError;

import ast.node.expression.ArrayCall;
import ast.node.expression.Expression;
import ast.node.expression.NewArray;

public class ArrayExpected extends ExpressionError {
    public ArrayExpected(Expression array) {
        super(array.getLine());
    }

    @Override
    public String toString() {
        return String.format("Line:%d:array call on not array type", line);
    }
}
