package errors;

public class NoClassExist extends Error {
    public NoClassExist() {
        super(0);
    }

    @Override
    public String toString() {
        return String.format("Line:%d:No class exists in the program", line);
    }
}