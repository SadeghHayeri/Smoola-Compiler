package jasmin.instructions;

public class JstartClass extends JasminStmt {
    private String className;

    public JstartClass(String className) {
        this.className = className;
    }

    @Override
    public String toString() {
        return ".class public " + className;
    }
}
