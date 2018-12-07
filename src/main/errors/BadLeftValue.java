package errors;

import ast.node.expression.Expression;
import ast.node.expression.NewArray;

public class BadLeftValue extends Error {
    public BadLeftValue(Expression expression) {
        super(expression.getLine());
    }

    @Override
    public String toString() {
        return String.format("Line:%d:left side of assignment must be a valid lvalue", line);
    }
}
