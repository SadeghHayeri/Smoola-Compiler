package jasmin.instructions;

public class Jnew extends JasminStmt {
    private String className;
    public Jnew(String className) {
        this.className = className;
    }

    @Override
    public String toString() {
        return "new " + className;
    }
}
