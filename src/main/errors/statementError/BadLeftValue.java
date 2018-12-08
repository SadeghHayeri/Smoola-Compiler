package errors.statementError;

import ast.node.expression.Expression;

public class BadLeftValue extends StatementError {
    public BadLeftValue(Expression expression) {
        super(expression.getLine());
    }

    @Override
    public String toString() {
        return String.format("Line:%d:left side of assignment must be a valid lvalue", line);
    }
}
