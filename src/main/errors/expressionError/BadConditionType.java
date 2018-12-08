package errors.expressionError;

import ast.node.expression.Expression;

public class BadConditionType extends ExpressionError {
    public BadConditionType(Expression expression) {
        super(expression.getLine());
    }

    @Override
    public String toString() {
        return String.format("Line:%d:Condition type must be boolean", line);
    }
}
