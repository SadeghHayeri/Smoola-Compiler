package errors.classError.mainClassError;
import ast.node.declaration.ClassDeclaration;
import errors.classError.ClassError;

public abstract class MainClassError extends ClassError {
    public MainClassError(ClassDeclaration classDeclaration) {
        super(classDeclaration.getLine());
    }
    public MainClassError(int line) {
        super(line);
    }

    @Override
    public String toString() {
        return "";
    }
}
