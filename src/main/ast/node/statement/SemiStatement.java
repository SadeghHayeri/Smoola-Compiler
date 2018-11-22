package ast.node.statement;

import ast.Visitor;
import ast.node.Node;
import ast.node.expression.Expression;

public class SemiStatement extends Statement {
    private Expression inside;

    public SemiStatement(Expression inside) {
        this.inside = inside;
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
}
