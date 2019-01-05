package jasmin.instructions;

public class Jinstanceof extends JasminStmt {
    private String classRef;
    public Jinstanceof(String classRef) {
        this.classRef = classRef;
    }

    @Override
    public String toString() {
        return "instanceof " + classRef;
    }
}
