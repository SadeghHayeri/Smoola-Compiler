package errors;

public abstract class Error extends Throwable {
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

    public abstract ErrorPhase whichPhase();
    public boolean isCriticalError() { return false; }
}
