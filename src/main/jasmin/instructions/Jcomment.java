package jasmin.instructions;

public class Jcomment extends JasminStmt {
    private String comment;
    public Jcomment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "; " + comment;
    }
}
