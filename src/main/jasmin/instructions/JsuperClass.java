package jasmin.instructions;

public class JsuperClass extends JasminStmt {
    private String superName;

    public JsuperClass(String superName) {
        this.superName = superName;
    }

    @Override
    public String toString() {
        return ".super " + superName;
    }
}
