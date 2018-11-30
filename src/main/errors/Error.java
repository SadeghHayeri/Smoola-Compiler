package errors;

public class Error {
    protected int line;

    public Error(int line) {
        this.line = line;
    }

    public int getLine() {
        return line;
    }

    public String toString() {
        return "";
    }
}
