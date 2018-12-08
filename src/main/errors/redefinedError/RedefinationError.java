package errors.redefinedError;

import errors.Error;

public abstract class RedefinationError extends Error {
    public RedefinationError(int line) {
        super(line);
    }
}
