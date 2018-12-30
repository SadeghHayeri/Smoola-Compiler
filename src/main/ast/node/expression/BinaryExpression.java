package ast.node.expression;

import ast.Type.Type;
import ast.Visitor;
import jasmin.instructions.*;
import jasmin.utils.JlabelGenarator;

import java.util.ArrayList;

public class BinaryExpression extends Expression {

    private Expression left;
    private Expression right;
    private BinaryOperator binaryOperator;

    public BinaryExpression(int line, Expression left, Expression right, BinaryOperator binaryOperator) {
        super(line);
        this.left = left;
        this.right = right;
        this.binaryOperator = binaryOperator;
    }

    public Expression getLeft() {
        return left;
    }

    public void setLeft(Expression left) {
        this.left = left;
    }

    public Expression getRight() {
        return right;
    }

    public void setRight(Expression right) {
        this.right = right;
    }

    public BinaryOperator getBinaryOperator() {
        return binaryOperator;
    }

    public void setBinaryOperator(BinaryOperator binaryOperator) {
        this.binaryOperator = binaryOperator;
    }

    @Override
    public String toString() {
        return "BinaryExpression " + binaryOperator.name();
    }
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public ArrayList<JasminStmt> toJasmin() {
        ArrayList<JasminStmt> code = new ArrayList<>();

        code.add(new Jcomment("Start binary-exp"));
        code.addAll(getLeft().toJasmin());
        code.addAll(getRight().toJasmin());

        String putTrueLabel = JlabelGenarator.unique("put_true");
        String putFalseLabel = JlabelGenarator.unique("put_false");
        String finishLabel = JlabelGenarator.unique("finish");

        //TODO
        switch (binaryOperator) {
            case eq:
                break;
            case neq:
                break;
            case gt:
                break;
            case lt:
                break;
            case or:
                break;

            case add:
                break;
            case and:
                break;
            case div:
                break;
            case sub:
                break;
            case mult:
                break;

            case assign:
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
