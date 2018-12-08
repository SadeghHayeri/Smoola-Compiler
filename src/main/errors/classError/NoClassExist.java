package errors.classError;

import errors.Error;

public class NoClassExist extends ClassError {
    public NoClassExist() {
        super(0);
    }

    @Override
    public String toString() {
        return String.format("Line:%d:No class exists in the program", line);
    }
}