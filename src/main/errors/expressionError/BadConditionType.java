package errors.expressionError;

import ast.node.expression.Expression;
import errors.ErrorPhase;

public class BadConditionType extends ExpressionError {
    public BadConditionType(Expression expression) {
        super(expression.getLine());
    }

    @Override
    public String toString() {
        return String.format("Line:%d:Condition type must be boolean", line);
    }

    @Override
    public ErrorPhase whichPhase() {
        return ErrorPhase.PHASE3;
    }
}
