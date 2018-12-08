package errors.classError;

import errors.Error;

public abstract class ClassError extends Error {
    public ClassError(int line) {
        super(line);
    }
}
