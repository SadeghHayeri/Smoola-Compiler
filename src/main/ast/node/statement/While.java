package ast.node.statement;

import ast.Visitor;
import ast.node.expression.Expression;
import jasmin.instructions.*;
import jasmin.utils.Jbranch;
import jasmin.utils.JlabelGenarator;

import java.util.ArrayList;

public class While extends Statement {
    private Expression condition;
    private Statement body;

    public While(int line, Expression condition, Statement body) {
        super(line);
        this.condition = condition;
        this.body = body;
    }

    public Expression getCondition() {
        return condition;
    }

    public void setCondition(Expression condition) {
        this.condition = condition;
    }

    public Statement getBody() {
        return body;
    }

    public void setBody(Statement body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "While";
    }
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public ArrayList<JasminStmt> toJasmin() {
        ArrayList<JasminStmt> code = new ArrayList<>();

        String nStart = JlabelGenarator.unique("start");
        String nExit = JlabelGenarator.unique("exit");

        code.add(new Jcomment("Start while"));
        code.add(new Jlabel(nStart));
        code.addAll(getCondition().toJasmin());
        code.add(new Jif(JifOperator.le, nExit));

        code.addAll(getBody().toJasmin());
        code.add(new Jgoto(nStart));

        code.add(new Jlabel(nExit));
        code.add(new Jcomment("End while"));
        return code;
    }
}
