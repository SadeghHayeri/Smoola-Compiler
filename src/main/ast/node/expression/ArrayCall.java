package ast.node.expression;

import ast.Visitor;
import jasmin.instructions.*;

import java.util.ArrayList;

public class ArrayCall extends Expression {
    private Expression instance;
    private Expression index;

    public ArrayCall(int line, Expression instance, Expression index) {
        super(line);
        this.instance = instance;
        this.index = index;
    }

    public Expression getInstance() {
        return instance;
    }

    public void setInstance(Expression instance) {
        this.instance = instance;
    }

    public Expression getIndex() {
        return index;
    }

    public void setIndex(Expression index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "ArrayCall";
    }
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public ArrayList<JasminStmt> toJasmin() {
        ArrayList<JasminStmt> code = new ArrayList<>();

        code.add(new Jcomment("Start array-call"));
        code.addAll(getInstance().toJasmin());
        code.addAll(getIndex().toJasmin());
        code.add(new Jload());
        code.add(new Jcomment("End array-call"));

        return code;
    }

    public ArrayList<JasminStmt> toStoreJasmin() {
        ArrayList<JasminStmt> code = new ArrayList<>();

        code.add(new Jcomment("Start store-array-call"));
        code.addAll(getInstance().toJasmin());
        code.add(new Jswap()); // move 'value' above 'ref'
        code.addAll(getIndex().toJasmin());
        code.add(new Jswap()); // move 'value' above 'index'
        code.add(new Jstore());
        code.add(new Jcomment("End store-array-call"));

        return code;
    }
}
