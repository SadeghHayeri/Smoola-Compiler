package ast.node.statement;

import ast.Visitor;
import ast.node.expression.Expression;
import jasmin.instructions.*;
import jasmin.utils.JlabelGenarator;

import java.util.ArrayList;

public class Write extends Statement {
    private Expression arg;

    public Write(int line, Expression arg) {
        super(line);
        this.arg = arg;
    }

    public Expression getArg() {
        return arg;
    }

    public void setArg(Expression arg) {
        this.arg = arg;
    }

    @Override
    public String toString() {
        return "Write";
    }
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public ArrayList<JasminStmt> toJasmin() {
        ArrayList<JasminStmt> code = new ArrayList<>();

        //TODO: for string only?
        code.add(new Jcomment("Start writeln"));
        code.addAll(getArg().toJasmin());
//        code.add(new Jprintln()); //TODO
        code.add(new Jcomment("End writeln"));
        return code;
    }
}
