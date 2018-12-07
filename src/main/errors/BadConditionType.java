package errors;

import ast.node.expression.Expression;
import ast.node.statement.SemiStatement;

public class BadConditionType extends Error {
    public BadConditionType(Expression expression) {
        super(expression.getLine());
    }

    @Override
    public String toString() {
        return String.format("Line:%d:Condition type must be boolean", line);
    }
}
