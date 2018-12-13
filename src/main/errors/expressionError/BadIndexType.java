package errors.expressionError;

import ast.node.expression.Expression;
import errors.ErrorPhase;

public class BadIndexType extends ExpressionError {
    public BadIndexType(Expression array) {
        super(array.getLine());
    }

    @Override
    public String toString() {
        return String.format("Line:%d:array index must be integer", line);
    }

    @Override
    public ErrorPhase whichPhase() {
        return ErrorPhase.PHASE3;
    }
}
