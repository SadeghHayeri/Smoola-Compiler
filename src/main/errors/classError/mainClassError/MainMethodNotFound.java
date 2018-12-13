package errors.classError.mainClassError;

import ast.node.declaration.ClassDeclaration;
import errors.ErrorPhase;

public class MainMethodNotFound extends MainClassError {

    public MainMethodNotFound(ClassDeclaration classDeclaration) {
        super(classDeclaration);
    }

    @Override
    public String toString() {
        return String.format("Line:%d:main-method not found", line);
    }

    @Override
    public ErrorPhase whichPhase() {
        return ErrorPhase.PHASE3;
    }
}
