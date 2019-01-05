package jasmin.instructions;

public class JendMethod extends JasminStmt {
    String methodName;
    public JendMethod(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public String toString() {
        return ".end method " + "; " + methodName;
    }
}
