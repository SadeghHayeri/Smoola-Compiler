package errors.expressionError;

import ast.node.expression.ArrayCall;
import ast.node.expression.Expression;
import ast.node.expression.NewArray;
import errors.ErrorPhase;

public class ArrayExpected extends ExpressionError {
    public ArrayExpected(Expression array) {
        super(array.getLine());
    }

    @Override
    public String toString() {
        return String.format("Line:%d:Array Expected", line);
    }

    @Override
    public ErrorPhase whichPhase() {
        return ErrorPhase.PHASE3;
    }
}
