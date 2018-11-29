package errors;

import ast.node.expression.NewArray;
import ast.node.statement.SemiStatement;

public class BadStatement extends Error {
    public BadStatement(SemiStatement semiStatement) {
        super(semiStatement.getLine());
    }

    @Override
    public String toString() {
        return String.format("Line:%d:Not Statement", line);
    }
}
