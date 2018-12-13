package errors.classError;

import errors.Error;
import errors.ErrorPhase;

public class NoClassExist extends ClassError {
    public NoClassExist() {
        super(0);
    }

    @Override
    public String toString() {
        return String.format("Line:%d:No class exists in the program", line);
    }

    @Override
    public ErrorPhase whichPhase() {
        return ErrorPhase.PHASE2;
    }

    @Override
    public boolean isCriticalError() {
        return true;
    }
}