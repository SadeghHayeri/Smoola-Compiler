package errors.methodError;

import ast.Type.Type;
import ast.node.expression.Expression;
import errors.ErrorPhase;
import errors.statementError.StatementError;

public class BadReturnType extends MethodError {

    private String returnTypeName;
    public BadReturnType(Type returnType, Expression returnValue) {
        super(returnValue.getLine());
        this.returnTypeName = returnType.toString();
    }

    @Override
    public String toString() {
        return String.format("Line:%d:return type must be %s", line, returnTypeName);
    }

    @Override
    public ErrorPhase whichPhase() {
        return ErrorPhase.PHASE3;
    }
}
