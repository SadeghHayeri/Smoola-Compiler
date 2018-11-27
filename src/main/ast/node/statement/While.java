package ast.node.statement;

import ast.Visitor;
import ast.node.expression.Expression;

public class While extends Statement {
    private Expression condition;
    private Statement body;

    public While(int line, Expression condition, Statement body) {
        super(line);
        this.condition = condition;
        this.body = body;
    }

    public Expression getCondition() {
        return condition;
    }

    public void setCondition(Expression condition) {
        this.condition = condition;
    }

    public Statement getBody() {
        return body;
    }

    public void setBody(Statement body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "While";
    }
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
