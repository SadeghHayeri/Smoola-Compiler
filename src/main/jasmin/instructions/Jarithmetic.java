package jasmin.instructions;

public class Jarithmetic extends JasminStmt {
    private JarithmaticOperator arithmaticOperator;

    public Jarithmetic(JarithmaticOperator arithmaticOperator) {
        this.arithmaticOperator = arithmaticOperator;
    }
}
