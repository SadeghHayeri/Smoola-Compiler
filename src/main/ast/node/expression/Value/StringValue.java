package ast.node.expression.Value;

import ast.Type.PrimitiveType.StringType;
import ast.Visitor;
import jasmin.instructions.JasminStmt;
import jasmin.instructions.Jcomment;
import jasmin.instructions.Jpush;

import java.util.ArrayList;

public class StringValue extends Value {
    private String constant;

    public StringValue(int line, String constant) {
        super(line);
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
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public ArrayList<JasminStmt> toJasmin() {
        ArrayList<JasminStmt> code = new ArrayList<>();

        code.add(new Jcomment("Start string-value"));
        code.add(new Jpush(constant));
        code.add(new Jcomment("End string-value"));

        return code;
    }
}
