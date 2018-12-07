package errors;

import ast.node.declaration.ClassDeclaration;

public class UndefinedClass extends Redefinition {

    private String className;

    public UndefinedClass(int line, String className) {
        super(line);
        this.className = className;
    }

    @Override
    public String toString() {
        return String.format("Line:%d:class %s is not declared", line, className);
    }
}
