package ast.node.declaration;

import ast.Util;
import ast.Visitor;
import ast.node.expression.Identifier;
import jasmin.instructions.*;

import java.util.ArrayList;

public class ClassDeclaration extends Declaration {
    private Identifier name;
    private Identifier parentName = new Identifier(-1, Util.MASTER_OBJECT_NAME);
    private ArrayList<VarDeclaration> varDeclarations = new ArrayList<>();
    private ArrayList<MethodDeclaration> methodDeclarations = new ArrayList<>();
    private ClassDeclaration parentClass;

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
                return new Jgetfield(name.getName(), variable, currVal.getType());
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

    @Override
    public ArrayList<JasminStmt> toJasmin() {
        ArrayList<JasminStmt> code = new ArrayList<>();

        String parentRef = hasParent() ? parentClass.getName().getName() : "java/lang/Object";

        code.add(new JstartClass(name.toString(), parentRef));

        // constructor
        code.add(new JstartMethod("<init>", "", "V"));
        code.add(new Jload(JrefType.a, 0));
        code.add(new Jinvoke(parentRef, "<init>", "", "V"));
        code.add(new JendMethod("<init>"));

        for(MethodDeclaration methodDeclaration : getMethodDeclarations())
            code.addAll(methodDeclaration.toJasmin());

        return code;
    }
}
