package errors.redefinedError;

import ast.node.declaration.ClassDeclaration;

public class ClassRedefinition extends RedefinationError {

    private String className;

    public ClassRedefinition(ClassDeclaration classDeclaration) {
        super(classDeclaration.getLine());
        this.className = classDeclaration.getName().getName();
    }

    @Override
    public String toString() {
        return String.format("Line:%d:Redefinition of class %s", line, className);
    }
}
