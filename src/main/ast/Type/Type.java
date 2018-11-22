package ast.Type;

import ast.Visitor;

public abstract class Type {
    public abstract String toString();
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
