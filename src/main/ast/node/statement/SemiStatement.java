package ast.node.statement;

import ast.Visitor;
import ast.node.Node;
import ast.node.expression.Expression;
import jasmin.instructions.JasminStmt;
import jasmin.instructions.Jcomment;
import jasmin.instructions.Jprintln;

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
}
