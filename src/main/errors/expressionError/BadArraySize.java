package errors.expressionError;

import ast.node.expression.NewArray;
import errors.ErrorPhase;

public class BadArraySize extends ExpressionError {
    public BadArraySize(NewArray newArray) {
        super(newArray.getLine());
    }

    @Override
    public String toString() {
        return String.format("Line:%d:Array length should not be zero or negative", line);
    }

    @Override
    public ErrorPhase whichPhase() {
        return ErrorPhase.PHASE2;
    }
}
