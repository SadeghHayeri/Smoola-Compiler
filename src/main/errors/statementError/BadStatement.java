package errors.statementError;

import ast.node.statement.SemiStatement;

public class BadStatement extends StatementError {
    public BadStatement(SemiStatement semiStatement) {
        super(semiStatement.getLine());
    }

    @Override
    public String toString() {
        return String.format("Line:%d:Not Statement", line);
    }
}
