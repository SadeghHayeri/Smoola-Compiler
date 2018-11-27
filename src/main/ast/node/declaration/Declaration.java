package ast.node.declaration;

import ast.Visitor;
import ast.node.Node;

public abstract class Declaration extends Node {
    public Declaration(int line) {
        super(line);
    }

    @Override
    public void accept(Visitor visitor) {}
}