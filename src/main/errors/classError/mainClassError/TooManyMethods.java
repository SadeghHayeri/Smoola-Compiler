package errors.classError.mainClassError;

import ast.node.declaration.ClassDeclaration;
import ast.node.declaration.MethodDeclaration;
import errors.ErrorPhase;

public class TooManyMethods extends MainClassError {

    public TooManyMethods(ClassDeclaration classDeclaration) {
        super(classDeclaration);
    }

    @Override
    public String toString() {
        return String.format("Line:%d:Main class has too many method", line);
    }

    @Override
    public ErrorPhase whichPhase() {
        return ErrorPhase.PHASE3;
    }
}
