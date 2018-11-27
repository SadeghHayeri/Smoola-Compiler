package ast.node.statement;

import ast.Visitor;
import ast.node.expression.Expression;

public class Conditional extends Statement {
    private Expression expression;
    private Statement consequenceBody;
    private Statement alternativeBody;

    public Conditional(int line, Expression expression, Statement consequenceBody) {
        super(line);
        this.expression = expression;
        this.consequenceBody = consequenceBody;
        this.alternativeBody = null;
    }

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    public Statement getConsequenceBody() {
        return consequenceBody;
    }

    public void setConsequenceBody(Statement consequenceBody) {
        this.consequenceBody = consequenceBody;
    }

    public Boolean hasAlternativeBody() {
        return alternativeBody != null;
    }

    public Statement getAlternativeBody() {
        return alternativeBody;
    }

    public void setAlternativeBody(Statement alternativeBody) {
        this.alternativeBody = alternativeBody;
    }

    @Override
    public String toString() {
        return "Conditional";
    }
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
