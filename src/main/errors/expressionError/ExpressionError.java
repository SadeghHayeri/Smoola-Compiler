package errors.expressionError;

import errors.Error;

public abstract class ExpressionError extends Error {
    public ExpressionError(int line) {
        super(line);
    }
}
