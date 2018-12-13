package errors.methodError.mainMethodError;

import ast.node.declaration.MethodDeclaration;
import errors.ErrorPhase;

public class BadMainReturnType extends MainMethodError {

    public BadMainReturnType(MethodDeclaration methodDeclaration) {
        super(methodDeclaration.getLine());
    }

    @Override
    public String toString() {
        return String.format("Line:%d:Main method return type must be INT", line);
    }

    @Override
    public ErrorPhase whichPhase() {
        return ErrorPhase.PHASE3;
    }
}
