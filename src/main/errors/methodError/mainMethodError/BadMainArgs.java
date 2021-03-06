package errors.methodError.mainMethodError;

import ast.node.declaration.MethodDeclaration;
import errors.ErrorPhase;

public class BadMainArgs extends MainMethodError {

    public BadMainArgs(MethodDeclaration methodDeclaration) {
        super(methodDeclaration.getLine());
    }

    @Override
    public String toString() {
        return String.format("Line:%d:Main method must have no argument", line);
    }

    @Override
    public ErrorPhase whichPhase() {
        return ErrorPhase.PHASE3;
    }
}
