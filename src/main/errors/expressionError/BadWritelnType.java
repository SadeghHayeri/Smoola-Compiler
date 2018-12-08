package errors.expressionError;

import ast.node.statement.Write;

public class BadWritelnType extends ExpressionError {
    public BadWritelnType(Write write) {
        super(write.getLine());
    }

    @Override
    public String toString() {
        return String.format("Line:%d:unsupported type for writeln", line);
    }
}
