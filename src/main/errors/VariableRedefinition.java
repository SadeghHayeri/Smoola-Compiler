package errors;

import ast.node.declaration.VarDeclaration;

public class VariableRedefinition extends Redefinition {

    private String variableName;
    public VariableRedefinition(VarDeclaration varDeclaration) {
        super(varDeclaration.getLine());
        this.variableName = varDeclaration.getIdentifier().getName();
    }

    @Override
    public String toString() {
        return String.format("Line:%d:Redefinition of variable %s", line, variableName);
    }
}
