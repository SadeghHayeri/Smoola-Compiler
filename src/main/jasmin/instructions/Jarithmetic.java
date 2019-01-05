package jasmin.instructions;

public class Jarithmetic extends JasminStmt {
    private JarithmaticOperator arithmaticOperator;

    public Jarithmetic(JarithmaticOperator arithmaticOperator) {
        this.arithmaticOperator = arithmaticOperator;
    }

    @Override
    public String toString() {
        switch (arithmaticOperator) {
            case add:
                return "iadd";
            case minus:
                return "ineg";
            case mult:
                return "imul";
            case sub:
                return "isub";
            case div:
                return "idiv";
        }

        assert false;
        return "<ERROR>";
    }
}
