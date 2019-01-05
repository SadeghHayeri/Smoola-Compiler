package ast.node.statement;

import ast.Visitor;
import ast.node.expression.Expression;
import jasmin.instructions.JasminStmt;
import jasmin.instructions.Jcomment;
import jasmin.instructions.Jpop;

import java.util.ArrayList;

public class SemiStatement extends Statement {
    private Expression inside;

    public SemiStatement(int line, Expression inside) {
        super(line);
        this.inside = inside;
    }

    public SemiStatement(int line) {
        super(line);
    }

    public boolean isEmpty() {
        return inside == null;
    }

    public Expression getInside() {
        return inside;
    }

    @Override
    public String toString() {
        return "SemiStatement";
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public ArrayList<JasminStmt> toJasmin() {
        ArrayList<JasminStmt> code = new ArrayList<>();

        code.add(new Jcomment("Start semi-statement"));
        code.addAll(inside.toJasmin());
        code.add(new Jpop());
        code.add(new Jcomment("End semi-statement"));

        return code;
    }
}
