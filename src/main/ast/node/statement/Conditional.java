package ast.node.statement;

import ast.Visitor;
import ast.node.expression.Expression;
import jasmin.instructions.JasminStmt;
import jasmin.instructions.Jcomment;
import jasmin.instructions.Jgoto;
import jasmin.instructions.Jlabel;
import jasmin.utils.Jbranch;
import jasmin.utils.JlabelGenarator;

import java.util.ArrayList;

public class Conditional extends Statement {
    private Expression expression;
    private Statement consequenceBody;
    private Statement alternativeBody;

    public Conditional(int line, Expression expression, Statement consequenceBody) {
        super(line);
        this.expression = expression;
        this.consequenceBody = consequenceBody;
        this.alternativeBody = null;
    }

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    public Statement getConsequenceBody() {
        return consequenceBody;
    }

    public void setConsequenceBody(Statement consequenceBody) {
        this.consequenceBody = consequenceBody;
    }

    public Boolean hasAlternativeBody() {
        return alternativeBody != null;
    }

    public Statement getAlternativeBody() {
        return alternativeBody;
    }

    public void setAlternativeBody(Statement alternativeBody) {
        this.alternativeBody = alternativeBody;
    }

    @Override
    public String toString() {
        return "Conditional";
    }
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public ArrayList<JasminStmt> toJasmin() {
        ArrayList<JasminStmt> code = new ArrayList<>();
        String nAfter = JlabelGenarator.unique("after");
        String nTrue = JlabelGenarator.unique("true");
        String nFalse = JlabelGenarator.unique("false");

        code.add(new Jcomment("Start if"));
        code.addAll(getExpression().toJasmin());
        code.addAll(new Jbranch(nTrue, nFalse).toJasmin());

        code.add(new Jlabel(nTrue));
        code.addAll(getConsequenceBody().toJasmin());
        code.add(new Jgoto(nAfter));

        code.add(new Jlabel(nFalse));
        if(hasAlternativeBody())
            code.addAll(getAlternativeBody().toJasmin());

        code.add(new Jlabel(nAfter));
        code.add(new Jcomment("End if"));
        return code;
    }
}
