package ast.node.expression.Value;

import ast.Type.PrimitiveType.IntType;
import ast.Visitor;

public class IntValue extends Value {
    private int constant;

    public IntValue(int line, int constant) {
        super(line);
        this.constant = constant;
        this.type = new IntType();
    }

    public int getConstant() {
        return constant;
    }

    public void setConstant(int constant) {
        this.constant = constant;
    }

    @Override
    public String toString() {
        return "IntValue " + constant;
    }
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
