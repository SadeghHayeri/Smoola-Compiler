package errors.variableError;

import errors.Error;

public abstract class VariableError extends Error {
    public VariableError(int line) {
        super(line);
    }
}
