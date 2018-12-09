package ast.node.expression;

import ast.Type.Type;
import ast.Visitor;

import java.util.ArrayList;

public class MethodCall extends Expression {
    private Expression instance;
    private Identifier methodName;
    private Type returnType;

    public MethodCall(int line, Expression instance, Identifier methodName) {
        super(line);
        this.instance = instance;
        this.methodName = methodName;
    }

    public Type getReturnType() {
        return returnType;
    }

    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }

    private ArrayList<Expression> args = new ArrayList<>();

    public Expression getInstance() {
        return instance;
    }

    public void setInstance(Expression instance) {
        this.instance = instance;
    }

    public Identifier getMethodName() {
        return methodName;
    }

    public void setMethodName(Identifier methodName) {
        this.methodName = methodName;
    }

    public ArrayList<Expression> getArgs() {
        return args;
    }

    public void addArg(Expression arg) {
        this.args.add(arg);
    }

    @Override
    public String toString() {
        return "MethodCall";
    }
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
