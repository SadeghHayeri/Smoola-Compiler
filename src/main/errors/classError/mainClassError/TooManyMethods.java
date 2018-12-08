package errors.classError.mainClassError;

import ast.node.declaration.ClassDeclaration;
import ast.node.declaration.MethodDeclaration;

public class TooManyMethods extends MainClassError {

    public TooManyMethods(ClassDeclaration classDeclaration) {
        super(classDeclaration);
    }

    @Override
    public String toString() {
        return String.format("Line:%d:Main class has too many method", line);
    }
}
