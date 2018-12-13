package errors.variableError;

import ast.node.expression.Identifier;
import errors.ErrorPhase;

public class UndefinedVariable extends VariableError {
    private String variableName;
    public UndefinedVariable(Identifier identifier) {
        super(identifier.getLine());
        this.variableName = identifier.getName();
    }

    @Override
    public String toString() {
        return String.format("Line:%d:Variable %s is not declared", line, variableName);
    }

    @Override
    public ErrorPhase whichPhase() {
        return ErrorPhase.PHASE3;
    }
}
