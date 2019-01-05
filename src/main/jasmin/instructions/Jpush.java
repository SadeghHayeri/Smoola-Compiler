package jasmin.instructions;

public class Jpush extends JasminStmt {
    private boolean isString;
    private int iValue;
    private String sValue;

    public Jpush(int value) {
        this.isString = false;
        this.iValue = value;
    }
    public Jpush(String value) {
        this.isString = true;
        this.sValue = value;
    }
    public Jpush(Boolean value) {
        this.isString = false;
        this.iValue = value ? 1 : 0;
    }

    @Override
    public String toString() {
        if(isString)
            return "ldc " + sValue;
        else
            return "ldc " + iValue;
    }
}