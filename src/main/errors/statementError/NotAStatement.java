package errors.statementError;

import ast.node.statement.SemiStatement;

public class NotAStatement extends StatementError {
    public NotAStatement(SemiStatement semiStatement) {
        super(semiStatement.getLine());
    }

    @Override
    public String toString() {
        return String.format("Line:%d:Not Statement", line);
    }
}
