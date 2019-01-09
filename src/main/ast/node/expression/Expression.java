package ast.node.expression;

import ast.Type.Type;
import ast.Visitor;
import ast.node.Node;

public abstract class Expression extends Node {
    protected Type expressionType;
    public Expression(int line) {
        super(line);
    }

    public Type getExpressionType() {
        return expressionType;
    }

    public void setExpressionType(Type expressionType) {
        this.expressionType = expressionType;
    }

    @Override
    public void accept(Visitor visitor) {}
}