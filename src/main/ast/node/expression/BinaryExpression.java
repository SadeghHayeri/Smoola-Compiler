package ast.node.expression;

import ast.Type.PrimitiveType.BooleanType;
import ast.Type.PrimitiveType.IntType;
import ast.Type.Type;
import ast.Visitor;
import jasmin.instructions.*;
import jasmin.utils.JlabelGenarator;
import java.util.ArrayList;

import static ast.ExpressionChecker.*;
import static ast.TypeChecker.*;

public class BinaryExpression extends Expression {

    private Expression left;
    private Expression right;
    private BinaryOperator binaryOperator;
    private Type sidesType;

    public BinaryExpression(int line, Expression left, Expression right, BinaryOperator binaryOperator) {
        super(line);
        this.left = left;
        this.right = right;
        this.binaryOperator = binaryOperator;
    }

    public void setSidesType(Type sidesType) { this.sidesType = sidesType; }

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

        String putTrueLabel = JlabelGenarator.unique("put_true");
        String putFalseLabel = JlabelGenarator.unique("put_false");
        String finishLabel = JlabelGenarator.unique("finish");

        switch (binaryOperator) {
            case eq:
                if(isBoolean(sidesType) || isInt(sidesType)) {
                    code.addAll(left.toJasmin());
                    code.addAll(right.toJasmin());
                    code.add(new Jif_icmp(JrefType.i, JifOperator.eq, putTrueLabel));
                    code.add(new Jgoto(putFalseLabel));
                } else {
                    code.addAll(left.toJasmin());
                    code.addAll(right.toJasmin());
                    code.add(new Jinvoke(JinvokeType.VIRTUAL, "java/lang/Object", "equals", "Ljava/lang/Object;", "Z"));
                }
                break;
            case neq:
                if(isBoolean(sidesType) || isInt(sidesType)) {
                    code.addAll(left.toJasmin());
                    code.addAll(right.toJasmin());
                    code.add(new Jif_icmp(JrefType.i, JifOperator.ne, putTrueLabel));
                    code.add(new Jgoto(putFalseLabel));
                } else {
                    code.addAll(left.toJasmin());
                    code.addAll(right.toJasmin());
                    code.add(new Jinvoke(JinvokeType.VIRTUAL,"java/lang/Object", "equals", "Ljava/lang/Object;", "Z"));
                    code.add(new Jif(JifOperator.ge, putFalseLabel));
                    code.add(new Jgoto(putTrueLabel));
                }
                break;
            case gt:
                code.addAll(left.toJasmin());
                code.addAll(right.toJasmin());
                code.add(new Jarithmetic(JarithmaticOperator.sub));
                break;
            case lt:
                code.addAll(left.toJasmin());
                code.addAll(right.toJasmin());
                code.add(new Jswap());
                code.add(new Jarithmetic(JarithmaticOperator.sub));
                break;
            case or:
                code.addAll(left.toJasmin());
                code.add(new Jif(JifOperator.ge, putTrueLabel));
                code.addAll(right.toJasmin());
                code.add(new Jif(JifOperator.ge, putTrueLabel));
                code.add(new Jgoto(putFalseLabel));
                break;
            case and:
                code.addAll(left.toJasmin());
                code.add(new Jif(JifOperator.le, putFalseLabel));
                code.addAll(right.toJasmin());
                code.add(new Jif(JifOperator.le, putFalseLabel));
                code.add(new Jgoto(putTrueLabel));
                break;
            case add:
                code.addAll(left.toJasmin());
                code.addAll(right.toJasmin());
                code.add(new Jarithmetic(JarithmaticOperator.add));
                break;
            case div:
                code.addAll(left.toJasmin());
                code.addAll(right.toJasmin());
                code.add(new Jarithmetic(JarithmaticOperator.div));
                break;
            case sub:
                code.addAll(left.toJasmin());
                code.addAll(right.toJasmin());
                code.add(new Jarithmetic(JarithmaticOperator.sub));
                break;
            case mult:
                code.addAll(left.toJasmin());
                code.addAll(right.toJasmin());
                code.add(new Jarithmetic(JarithmaticOperator.mult));
                break;
            case assign:
                code.addAll(right.toJasmin());
                if(isIdentifier(left))
                    code.addAll(ID(left).toStoreJasmin());
                else if(isArrayCall(left))
                    code.addAll(AC(left).toStoreJasmin());

                code.addAll(left.toJasmin());
                break;
        }

        code.add(new Jgoto(finishLabel));

        code.add(new Jlabel(putTrueLabel));
        code.add(new Jpush(true));
        code.add(new Jgoto(finishLabel));

        code.add(new Jlabel(putFalseLabel));
        code.add(new Jpush(false));
        code.add(new Jgoto(finishLabel));

        code.add(new Jlabel(finishLabel));

        code.add(new Jcomment("End binary-exp"));

        return code;
    }
}
