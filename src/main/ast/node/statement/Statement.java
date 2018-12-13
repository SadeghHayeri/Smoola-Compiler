package ast.node.statement;

import ast.Visitor;
import ast.node.Node;

public class Statement extends Node {
    private boolean isInMainMethod = false;

    public Statement(int line) {
        super(line);
    }

    public boolean isInMainMethod() {
        return isInMainMethod;
    }
    public void setInMainMethod(boolean inMainMethod) {
        isInMainMethod = inMainMethod;
    }

    @Override
    public String toString() {
        return "Statement";
    }

    @Override
    public void accept(Visitor visitor) {}
}
