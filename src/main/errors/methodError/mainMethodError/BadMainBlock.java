package errors.methodError.mainMethodError;

import ast.node.declaration.MethodDeclaration;
import errors.ErrorPhase;

public class BadMainBlock extends MainMethodError {

    public BadMainBlock(MethodDeclaration methodDeclaration) {
        super(methodDeclaration.getLine());
    }

    @Override
    public String toString() {
        return String.format("Line:%d:Main method has bad body", line);
    }

    @Override
    public ErrorPhase whichPhase() {
        return ErrorPhase.PHASE3;
    }
}
