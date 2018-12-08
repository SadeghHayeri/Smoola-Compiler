package errors.statementError;

import errors.Error;

public abstract class StatementError extends Error {
    public StatementError(int line) {
        super(line);
    }
}
