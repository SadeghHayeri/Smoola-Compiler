package ast.node.expression;

import ast.Visitor;

public class Identifier extends Expression {
    private String name;

    public Identifier(int line, String name) {
        super(line);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Identifier " + name;
    }
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
