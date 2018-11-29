package errors;

import ast.node.declaration.MethodDeclaration;

public class MethodRedefinition extends Redefinition {

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
