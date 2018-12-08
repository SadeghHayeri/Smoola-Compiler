package errors.classError.mainClassError;

import ast.node.declaration.ClassDeclaration;
import ast.node.declaration.MethodDeclaration;
import errors.methodError.mainMethodError.MainMethodError;

public abstract class BadMainParent extends MainClassError {

    public BadMainParent(ClassDeclaration classDeclaration) {
        super(classDeclaration);
    }

    @Override
    public String toString() {
        return String.format("Line:%d:Main class must not inherited", line);
    }
}
