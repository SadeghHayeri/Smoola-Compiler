package errors.methodError.mainMethodError;

import ast.node.declaration.MethodDeclaration;
import errors.ErrorPhase;

public class VarDeclareInnMainMethod extends MainMethodError {

    public VarDeclareInnMainMethod(MethodDeclaration methodDeclaration) {
        super(methodDeclaration.getLine());
    }

    @Override
    public String toString() {
        return String.format("Line:%d:Can't declare variable in Main Method", line);
    }

    @Override
    public ErrorPhase whichPhase() {
        return ErrorPhase.PHASE3;
    }
}
