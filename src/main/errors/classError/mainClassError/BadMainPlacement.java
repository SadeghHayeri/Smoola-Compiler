package errors.classError.mainClassError;

import ast.node.declaration.ClassDeclaration;

public class BadMainPlacement extends MainClassError {

    public BadMainPlacement(ClassDeclaration classDeclaration) {
        super(classDeclaration);
    }

    @Override
    public String toString() {
        return String.format("Line:%d:Main class must be first class in file", line);
    }
}
