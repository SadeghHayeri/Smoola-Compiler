package ast.node.expression.Value;

import ast.Type.PrimitiveType.BooleanType;
import ast.Visitor;
import jasmin.instructions.JasminStmt;
import jasmin.instructions.Jcomment;
import jasmin.instructions.Jload;
import jasmin.instructions.Jpush;

import java.util.ArrayList;

public class BooleanValue extends Value {
    private boolean constant;

    public BooleanValue(int line, boolean constant) {
        super(line);
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

    @Override
    public ArrayList<JasminStmt> toJasmin() {
        ArrayList<JasminStmt> code = new ArrayList<>();

        code.add(new Jcomment("Start boolean-value"));
        code.add(new Jpush(constant));
        code.add(new Jcomment("End boolean-value"));

        return code;
    }
}
