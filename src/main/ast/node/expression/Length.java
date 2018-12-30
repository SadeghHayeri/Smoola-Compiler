package ast.node.expression;

import ast.Visitor;
import jasmin.instructions.Jarraylength;
import jasmin.instructions.JasminStmt;
import jasmin.instructions.Jcomment;
import jasmin.instructions.Jload;

import java.util.ArrayList;

public class Length extends Expression {
    private Expression expression;

    public Length(int line, Expression expression) {
        super(line);
        this.expression = expression;
    }

    public Expression getExpression() { return expression; }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "Length";
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public ArrayList<JasminStmt> toJasmin() {
        ArrayList<JasminStmt> code = new ArrayList<>();

        code.add(new Jcomment("Start array-length"));
        code.addAll(getExpression().toJasmin());
        code.add(new Jarraylength());
        code.add(new Jcomment("Endarray-length"));

        return code;
    }
}
