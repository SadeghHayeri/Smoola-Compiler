package errors.methodError.mainMethodError;

import ast.node.declaration.MethodDeclaration;

public class BadMainReturnType extends MainMethodError {

    public BadMainReturnType(MethodDeclaration methodDeclaration) {
        super(methodDeclaration.getLine());
    }

    @Override
    public String toString() {
        return String.format("Line:%d:Main method return type must be INT", line);
    }
}
