package ast.node.declaration;

import ast.Type.Type;
import ast.Util;
import ast.Visitor;
import ast.node.expression.Expression;
import ast.node.expression.Identifier;
import ast.node.statement.Statement;
import jasmin.instructions.*;
import static ast.TypeChecker.*;

import java.util.ArrayList;

public class MethodDeclaration extends Declaration {
    private Expression returnValue;
    private Type returnType;
    private Identifier name;
    private ArrayList<VarDeclaration> args = new ArrayList<>();
    private ArrayList<VarDeclaration> localVars = new ArrayList<>();
    private ArrayList<Statement> body = new ArrayList<>();
    private ClassDeclaration containerClass;

    public void setContainerClass(ClassDeclaration containerClass) {
        this.containerClass = containerClass;
    }

    public boolean isMainMethod() {
        return isInMainClass && name.getName().equals("main");
    }

    public void setInMainClass(boolean inMainClass) {
        isInMainClass = inMainClass;
    }

    boolean isInMainClass = false;

    public MethodDeclaration(int line, Identifier name) {
        super(line);
        this.name = name;
    }

    public Expression getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Expression returnValue) {
        this.returnValue = returnValue;
    }

    public Type getReturnType() {
        return returnType;
    }

    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }

    public Identifier getName() {
        return name;
    }

    public void setName(Identifier name) {
        this.name = name;
    }

    public ArrayList<VarDeclaration> getArgs() {
        return args;
    }

    public ArrayList<Type> getArgsType() {
        ArrayList<Type> argsType = new ArrayList<>();
        for(VarDeclaration varDeclaration : this.getArgs())
            argsType.add(varDeclaration.getType());
        return argsType;
    }

    public void addArg(VarDeclaration arg) {
        this.args.add(arg);
    }

    public ArrayList<Statement> getBody() {
        return body;
    }

    public void addStatement(Statement statement) {
        this.body.add(statement);
    }

    public ArrayList<VarDeclaration> getLocalVars() {
        return localVars;
    }

    public void addLocalVar(VarDeclaration localVar) {
        this.localVars.add(localVar);
    }

    public ArrayList<JasminStmt> getVariableJasmin(String variable) {
        ArrayList<JasminStmt> code = new ArrayList<>();

        ArrayList<VarDeclaration> variables = new ArrayList<>();
        variables.addAll(this.getArgs());
        variables.addAll(this.getLocalVars());

        for(int i = 0; i < variables.size(); i++) {
            VarDeclaration currVal = variables.get(i);
            String currVarName = currVal.getIdentifier().getName();
            if (variable.equals(currVarName)) {
                code.add(new Jload(currVal.getType(), i + JasminStmt.ARRAY_BASE));
                return code;
            }
        }

        code.add(new Jload(JrefType.a, 0)); // add ref
        code.add(containerClass.getVariableJasmin(variable));
        return code;
    }

    public ArrayList<JasminStmt> setVariableJasmin(String variable) {
        ArrayList<JasminStmt> code = new ArrayList<>();

        ArrayList<VarDeclaration> variables = new ArrayList<>();
        variables.addAll(this.getArgs());
        variables.addAll(this.getLocalVars());

        for(int i = 0; i < variables.size(); i++) {
            VarDeclaration currVal = variables.get(i);
            String currVarName = currVal.getIdentifier().getName();
            if (variable.equals(currVarName)) {
                code.add(new Jstore(currVal.getType(), i + JasminStmt.ARRAY_BASE));
                return code;
            }
        }

        code.add(new Jload(JrefType.a, 0));
        code.add(new Jswap());
        code.add(containerClass.setVariableJasmin(variable));
        return code;
    }

    @Override
    public String toString() {
        return "MethodDeclaration";
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public ArrayList<JasminStmt> toJasmin() {
        ArrayList<JasminStmt> code = new ArrayList<>();

        code.add(new JstartMethod(name.getName(), getArgsType(), returnType));
        code.add(new Jlimit("stack", Util.MAX_STACK));
        code.add(new Jlimit("locals", Util.MAX_LOCALS));

        for(Statement statement : body)
            code.addAll(statement.toJasmin());

        code.addAll(returnValue.toJasmin());
        code.add(new Jreturn(isInt(returnType) || isBoolean(returnType) ? JrefType.i : JrefType.a));
        code.add(new JendMethod(name.getName()));

        return code;
    }
}
