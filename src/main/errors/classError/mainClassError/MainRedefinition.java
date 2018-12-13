package errors.classError.mainClassError;

import ast.node.declaration.ClassDeclaration;
import ast.node.declaration.MethodDeclaration;
import errors.ErrorPhase;
import errors.methodError.mainMethodError.MainMethodError;

public class MainRedefinition extends MainClassError {

    public MainRedefinition(ClassDeclaration classDeclaration) {
        super(classDeclaration);
    }

    @Override
    public String toString() {
        return String.format("Line:%d:too many main classes", line);
    }

    @Override
    public ErrorPhase whichPhase() {
        return ErrorPhase.PHASE3;
    }
}
