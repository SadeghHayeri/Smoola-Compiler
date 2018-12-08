package errors.classError;

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
}
