package ast.node.expression.Value;

import ast.Type.PrimitiveType.BooleanType;
import ast.Visitor;

public class BooleanValue extends Value {
    private boolean constant;

    public BooleanValue(boolean constant) {
        this.constant = constant;
        this.type = new BooleanType();
    }

    public boolean isConstant() {
        return constant;
    }

    public void setConstant(boolean constant) {
        this.constant = constant;
    }

    @Override
    public String toString() {
        return "BooleanValue " + constant;
    }
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
