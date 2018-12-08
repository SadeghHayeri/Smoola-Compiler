package errors.methodError;

import errors.Error;

public abstract class MethodError extends Error {
    public MethodError(int line) {
        super(line);
    }
}
