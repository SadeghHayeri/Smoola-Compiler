package errors.undefinedError;

public class UndefinedClass extends UndefinedError {

    private String className;

    public UndefinedClass(int line, String className) {
        super(line);
        this.className = className;
    }

    @Override
    public String toString() {
        return String.format("Line:%d:class %s is not declared", line, className);
    }
}
