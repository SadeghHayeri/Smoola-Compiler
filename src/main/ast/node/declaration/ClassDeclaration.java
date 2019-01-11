package ast.node.declaration;

import ast.Util;
import ast.Visitor;
import ast.node.expression.BinaryExpression;
import ast.node.expression.BinaryOperator;
import ast.node.expression.Identifier;
import ast.node.expression.Value.StringValue;
import ast.node.statement.Assign;
import ast.node.statement.Statement;
import jasmin.instructions.*;

import java.util.ArrayList;

import static ast.TypeChecker.*;

public class ClassDeclaration extends Declaration {
    private Identifier name;
    private Identifier parentName = new Identifier(-1, Util.MASTER_OBJECT_NAME);
    private ArrayList<VarDeclaration> varDeclarations = new ArrayList<>();
    private ArrayList<MethodDeclaration> methodDeclarations = new ArrayList<>();
    private ClassDeclaration parentClass;

    public void setMainClass(boolean mainClass) {
        if(mainClass) {
            assert methodDeclarations.get(0).getName().getName().equals("main");
            methodDeclarations.get(0).setMainMethod(true);
        }
    }

    public void setParentClass(ClassDeclaration parentClass) {
        this.parentClass = parentClass;
    }

    public ClassDeclaration(int line, Identifier name) {
        super(line);
        this.name = name;
    }

    public Identifier getName() {
        return name;
    }

    public void setName(Identifier name) {
        this.name = name;
    }

    public Boolean hasParent() {
        return this.parentName != null;
    }

    public Identifier getParentName() {
        return parentName;
    }

    public void setParentName(Identifier parentName) {
        this.parentName = parentName;
    }
    public void unSetParentName() { this.parentName = null; }


    public ArrayList<VarDeclaration> getVarDeclarations() {
        return varDeclarations;
    }

    public void addVarDeclaration(VarDeclaration varDeclaration) {
        this.varDeclarations.add(varDeclaration);
    }

    public ArrayList<MethodDeclaration> getMethodDeclarations() {
        return methodDeclarations;
    }

    public void addMethodDeclaration(MethodDeclaration methodDeclaration) {
        this.methodDeclarations.add(methodDeclaration);
    }

    @Override
    public String toString() {
        return "ClassDeclaration";
    }
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public JasminStmt getVariableJasmin(String variable) {
        ArrayList<VarDeclaration> variables = this.getVarDeclarations();
        for (VarDeclaration currVal : variables) {
            String currVarName = currVal.getIdentifier().getName();
            if (variable.equals(currVarName))
                return new Jget(JgetType.FIELD, name.getName(), variable, currVal.getType());
        }

        if(parentClass != null) {
            return parentClass.getVariableJasmin(variable);
        }

        assert false;
        return null;
    }

    public JasminStmt setVariableJasmin(String variable) {
        ArrayList<VarDeclaration> variables = this.getVarDeclarations();
        for (VarDeclaration currVal : variables) {
            String currVarName = currVal.getIdentifier().getName();
            if (variable.equals(currVarName))
                return new Jputfield(name.getName(), variable, currVal.getType());
        }

        if(parentClass != null) {
            return parentClass.getVariableJasmin(variable);
        }

        assert false;
        return null;
    }

    private ArrayList<JasminStmt> initialFields() {
        ArrayList<JasminStmt> code = new ArrayList<>();

        for(VarDeclaration varDeclaration : varDeclarations) {
            if(isInt(varDeclaration.getType()) || isBoolean(varDeclaration.getType())) {
                code.add(new Jload(JrefType.a, 0));
                code.add(new Jpush(0));
            } else if(isString(varDeclaration.getType())) {
                code.add(new Jload(JrefType.a, 0));
                code.add(new Jpush("\"\""));
            } else
                continue;
            code.add(this.setVariableJasmin(varDeclaration.getIdentifier().getName()));
        }

        return code;
    }

    @Override
    public ArrayList<JasminStmt> toJasmin() {
        ArrayList<JasminStmt> code = new ArrayList<>();

        String parentRef = hasParent() ? parentClass.getName().getName() : "java/lang/Object";

        code.add(new JstartClass(name.getName()));
        code.add(new JsuperClass(parentRef));


        for(VarDeclaration varDeclaration : getVarDeclarations())
            code.addAll(varDeclaration.toJasmin());

        // constructor
        code.add(new JstartMethod("<init>", "", "V"));
        code.add(new Jlimit("stack", Util.MAX_STACK));
        code.add(new Jlimit("locals", Util.MAX_LOCALS));
        code.add(new Jload(JrefType.a, 0));
        code.add(new Jinvoke(JinvokeType.SPECIAL, parentRef, "<init>", "", "V"));
        code.addAll(initialFields());
        code.add(new Jreturn(JrefType.VOID));
        code.add(new JendMethod("<init>"));

        for(MethodDeclaration methodDeclaration : getMethodDeclarations())
            code.addAll(methodDeclaration.toJasmin());

        return code;
    }
}
