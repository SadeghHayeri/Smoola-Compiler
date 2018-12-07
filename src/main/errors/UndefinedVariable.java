package errors;

import ast.node.expression.Identifier;

public class UndefinedVariable extends Error {
    private String variableName;
    public UndefinedVariable(Identifier identifier) {
        super(identifier.getLine());
        this.variableName = identifier.getName();
    }

    @Override
    public String toString() {
        return String.format("Line:%d:Variable %s is not declared", line, variableName);
    }
}
