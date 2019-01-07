package ast.node.expression;

import ast.Type.Type;
import ast.Visitor;
import jasmin.instructions.*;

import java.util.ArrayList;

public class MethodCall extends Expression {
    private String className;
    private Expression instance;
    private Identifier methodName;
    private ArrayList<Type> argsType ;
    private Type returnType;

    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }

    public void setArgsType(ArrayList<Type> argsType) {
        this.argsType = argsType;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public MethodCall(int line, Expression instance, Identifier methodName) {
        super(line);
        this.instance = instance;
        this.methodName = methodName;
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

    @Override
    public ArrayList<JasminStmt> toJasmin() {
        ArrayList<JasminStmt> code = new ArrayList<>();
        code.add(new Jcomment("Start method-call"));
        code.addAll(instance.toJasmin());
        for (Expression arg : args)
            code.addAll(arg.toJasmin());
        code.add(new Jinvoke(JinvokeType.VIRTUAL, className, methodName.getName(), argsType, returnType));
        code.add(new Jcomment("End method-call"));

        return code;
    }
}
