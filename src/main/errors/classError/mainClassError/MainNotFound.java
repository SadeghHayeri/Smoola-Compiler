package errors.classError.mainClassError;

import ast.node.declaration.ClassDeclaration;
import errors.ErrorPhase;

public class MainNotFound extends MainClassError {

    public MainNotFound() {
        super(0);
    }

    @Override
    public String toString() {
        return String.format("Line:%d:Expected Main class not found", line);
    }

    @Override
    public ErrorPhase whichPhase() {
        return ErrorPhase.PHASE3;
    }
}
