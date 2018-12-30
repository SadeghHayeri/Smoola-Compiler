package ast.node.expression;

import ast.Visitor;
import jasmin.instructions.JasminStmt;
import jasmin.instructions.Jcomment;
import jasmin.instructions.Jload;

import java.util.ArrayList;

public class Identifier extends Expression {
    private String name;

    public Identifier(int line, String name) {
        super(line);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Identifier " + name;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public ArrayList<JasminStmt> toJasmin() {
        ArrayList<JasminStmt> code = new ArrayList<>();

        code.add(new Jcomment("Start identifier"));
        //TODO////////////////////////////
        code.add(new Jcomment("End identifier"));

        return code;
    }
}
