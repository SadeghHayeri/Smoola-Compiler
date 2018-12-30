package ast.node.declaration;

import ast.Util;
import ast.Visitor;
import ast.node.expression.Identifier;
import jasmin.instructions.JasminStmt;
import jasmin.instructions.Jgetfield;
import jasmin.instructions.Jload;

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

    public JasminStmt getVariableJasmin(Identifier variable) {
        ArrayList<VarDeclaration> variables = this.getVarDeclarations();
        for (VarDeclaration currVal : variables) {
            String currVarName = currVal.getIdentifier().getName();
            if (variable.getName().equals(currVarName))
                return new Jgetfield(name.getName(), variable.getName(), currVal.getType());
        }

        if(parentClass != null) {
            return parentClass.getVariableJasmin(variable);
        }

        assert false;
        return null;
    }
}
