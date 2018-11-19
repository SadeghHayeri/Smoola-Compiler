package ast.node.expression.Value;

import ast.Type.PrimitiveType.StringType;
import ast.Visitor;

public class StringValue extends Value {
    private String constant;

    public StringValue(String constant) {
        this.constant = constant;
        this.type = new StringType();
    }

    public String getConstant() {
        return constant;
    }

    public void setConstant(String constant) {
        this.constant = constant;
    }

    @Override
    public String toString() {
        return "StringValue " + constant;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
