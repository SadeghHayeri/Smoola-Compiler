package errors.classError.mainClassError;

import ast.node.declaration.ClassDeclaration;
import errors.ErrorPhase;

public class VarDeclareInMainClass extends MainClassError {

    public VarDeclareInMainClass(ClassDeclaration classDeclaration) {
        super(classDeclaration);
    }

    @Override
    public String toString() {
        return String.format("Line:%d:Can't declare variable in Main class", line);
    }

    @Override
    public ErrorPhase whichPhase() {
        return ErrorPhase.PHASE3;
    }
}
