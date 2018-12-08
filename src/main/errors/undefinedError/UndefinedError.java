package errors.undefinedError;

import errors.Error;

public abstract class UndefinedError extends Error {
    public UndefinedError(int line) {
        super(line);
    }
}
