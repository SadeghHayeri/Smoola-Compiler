package ast.node.expression.Value;

import ast.Type.PrimitiveType.IntType;
import ast.Visitor;
import jasmin.instructions.JasminStmt;
import jasmin.instructions.Jcomment;
import jasmin.instructions.Jpush;

import java.util.ArrayList;

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

    @Override
    public ArrayList<JasminStmt> toJasmin() {
        ArrayList<JasminStmt> code = new ArrayList<>();

        code.add(new Jcomment("Start int-value"));
        code.add(new Jpush(constant));
        code.add(new Jcomment("End int-value"));

        return code;
    }
}
