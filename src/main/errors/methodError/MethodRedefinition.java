package errors.methodError;

import ast.node.declaration.MethodDeclaration;
import errors.variableError.VariableError;

public class MethodRedefinition extends MethodError {

    private String methodName;
    public MethodRedefinition(MethodDeclaration methodDeclaration) {
        super(methodDeclaration.getLine());
        this.methodName = methodDeclaration.getName().getName();
    }

    @Override
    public String toString() {
        return String.format("Line:%d:Redefinition of method %s", line, methodName);
    }
}
