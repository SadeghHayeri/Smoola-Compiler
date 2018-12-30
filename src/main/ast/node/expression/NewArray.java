package ast.node.expression;

import ast.Visitor;
import jasmin.instructions.JasminStmt;
import jasmin.instructions.Jcomment;
import jasmin.instructions.Jload;
import jasmin.instructions.Jnewarray;

import java.util.ArrayList;

public class NewArray extends Expression {
    private Expression expression;

    public NewArray(int line, Expression expression) {
        super(line);
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "NewArray";
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public ArrayList<JasminStmt> toJasmin() {
        ArrayList<JasminStmt> code = new ArrayList<>();

        code.add(new Jcomment("Start new-array"));
        code.addAll(expression.toJasmin());
        code.add(new Jnewarray());
        code.add(new Jcomment("End new-array"));

        return code;
    }
}
