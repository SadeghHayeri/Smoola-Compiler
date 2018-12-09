package ast.node.expression;

import ast.Type.Type;
import ast.Visitor;
import ast.node.Node;

public abstract class Expression extends Node {
    public Expression(int line) {
        super(line);
    }

    @Override
    public void accept(Visitor visitor) {}
}