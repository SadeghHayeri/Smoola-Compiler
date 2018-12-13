package errors.statementError;

import ast.node.statement.SemiStatement;
import errors.ErrorPhase;

public class NotAStatement extends StatementError {
    public NotAStatement(SemiStatement semiStatement) {
        super(semiStatement.getLine());
    }

    @Override
    public String toString() {
        return String.format("Line:%d:Not Statement", line);
    }

    @Override
    public ErrorPhase whichPhase() {
        return ErrorPhase.PHASE3;
    }
}
