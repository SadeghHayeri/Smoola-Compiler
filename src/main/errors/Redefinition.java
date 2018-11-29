package errors;

public class Redefinition extends Error {
    public Redefinition(int line) {
        super(line);
    }
}
