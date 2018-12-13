package errors.classError;

import ast.node.declaration.ClassDeclaration;
import errors.ErrorPhase;
import errors.variableError.VariableError;

public class ClassRedefinition extends ClassError {

    private String className;

    public ClassRedefinition(ClassDeclaration classDeclaration) {
        super(classDeclaration.getLine());
        this.className = classDeclaration.getName().getName();
    }

    @Override
    public String toString() {
        return String.format("Line:%d:Redefinition of class %s", line, className);
    }

    @Override
    public ErrorPhase whichPhase() {
        return ErrorPhase.PHASE2;
    }
}
