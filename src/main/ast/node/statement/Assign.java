package ast.node.statement;

import ast.Visitor;
import ast.node.expression.Expression;
import jasmin.instructions.JasminStmt;
import jasmin.instructions.Jcomment;

import java.util.ArrayList;

import static ast.ExpressionChecker.*;
import static ast.ExpressionChecker.AC;

public class Assign extends Statement {
    private Expression lValue;
    private Expression rValue;

    public Assign(int line, Expression lValue, Expression rValue) {
        super(line);
        this.lValue = lValue;
        this.rValue = rValue;
    }

    public Expression getlValue() {
        return lValue;
    }

    public void setlValue(Expression lValue) {
        this.lValue = lValue;
    }

    public Expression getrValue() {
        return rValue;
    }

    public void setrValue(Expression rValue) {
        this.rValue = rValue;
    }

    @Override
    public String toString() {
        return "Assign";
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public ArrayList<JasminStmt> toJasmin() {
        ArrayList<JasminStmt> code = new ArrayList<>();
        code.add(new Jcomment("Start assign-stmt"));

        if(isIdentifier(lValue))
            ID(lValue).toStoreJasmin();
        else if(isArrayCall(lValue))
            AC(lValue).toStoreJasmin();

        code.add(new Jcomment("End assign-stmt"));
        return code;
    }
}
