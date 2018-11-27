package ast.node.expression;

import ast.Visitor;

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
}
