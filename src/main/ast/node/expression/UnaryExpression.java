package ast.node.expression;

import ast.Visitor;
import jasmin.instructions.*;
import jasmin.utils.JlabelGenarator;

import java.util.ArrayList;

public class UnaryExpression extends Expression {

    private UnaryOperator unaryOperator;
    private Expression value;

    public UnaryExpression(int line, UnaryOperator unaryOperator, Expression value) {
        super(line);
        this.unaryOperator = unaryOperator;
        this.value = value;
    }

    public Expression getValue() {
        return value;
    }

    public void setValue(Expression value) {
        this.value = value;
    }

    public UnaryOperator getUnaryOperator() {
        return unaryOperator;
    }

    public void setUnaryOperator(UnaryOperator unaryOperator) {
        this.unaryOperator = unaryOperator;
    }

    @Override
    public String toString() {
        return "UnaryExpression " + unaryOperator.name();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public ArrayList<JasminStmt> toJasmin() {
        ArrayList<JasminStmt> code = new ArrayList<>();

        code.add(new Jcomment("Start binary-exp"));
        code.addAll(value.toJasmin());

        String putTrueLabel = JlabelGenarator.unique("put_true");
        String putFalseLabel = JlabelGenarator.unique("put_false");
        String finishLabel = JlabelGenarator.unique("finish");

        //TODO
        switch (unaryOperator) {
            case minus:
                break;
            case not:
                break;
        }

        code.add(new Jlabel(putTrueLabel));
        code.add(new Jpush(true));
        code.add(new Jgoto(finishLabel));

        code.add(new Jlabel(putFalseLabel));
        code.add(new Jpush(false));
        code.add(new Jgoto(finishLabel));

        code.add(new Jcomment("End binary-exp"));

        return code;
    }
}

