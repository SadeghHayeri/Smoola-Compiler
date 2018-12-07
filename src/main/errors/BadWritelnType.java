package errors;

import ast.node.expression.NewArray;
import ast.node.statement.Write;

public class BadWritelnType extends Error {
    public BadWritelnType(Write write) {
        super(write.getLine());
    }

    @Override
    public String toString() {
        return String.format("Line:%d:unsupported type for writeln", line);
    }
}
