package errors.classError;

import errors.ErrorPhase;

public class UndefinedClass extends ClassError {

    private String className;

    public UndefinedClass(int line, String className) {
        super(line);
        this.className = className;
    }

    @Override
    public String toString() {
        return String.format("Line:%d:class %s is not declared", line, className);
    }

    @Override
    public ErrorPhase whichPhase() {
        return ErrorPhase.PHASE3;
    }
}
