package ast.node.statement;

import ast.Visitor;
import jasmin.instructions.JasminStmt;
import jasmin.instructions.Jcomment;

import java.util.ArrayList;

public class Block extends Statement {
    private ArrayList<Statement> body = new ArrayList<>();

    public Block(int line) {
        super(line);
    }

    public ArrayList<Statement> getBody() {
        return body;
    }

    public void addStatement(Statement statement) {
        this.body.add(statement);
    }

    @Override
    public String toString() {
        return "Block";
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public ArrayList<JasminStmt> toJasmin() {
        ArrayList<JasminStmt> code = new ArrayList<>();

        code.add(new Jcomment("Start block"));
        for(Statement statement : body)
            code.addAll(statement.toJasmin());
        code.add(new Jcomment("End block"));

        return code;
    }
}
