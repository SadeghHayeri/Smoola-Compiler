package errors.expressionError;

import ast.node.statement.Write;
import errors.ErrorPhase;

public class BadWritelnType extends ExpressionError {
    public BadWritelnType(Write write) {
        super(write.getLine());
    }

    @Override
    public String toString() {
        return String.format("Line:%d:unsupported type for writeln", line);
    }

    @Override
    public ErrorPhase whichPhase() {
        return ErrorPhase.PHASE3;
    }
}
