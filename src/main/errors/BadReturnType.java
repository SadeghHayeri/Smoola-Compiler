package errors;

import ast.Type.Type;
import ast.node.expression.Expression;
import ast.node.expression.NewArray;

public class BadReturnType extends Error {

    private String returnTypeName;
    public BadReturnType(Type returnType, Expression returnValue) {
        super(returnValue.getLine());
        this.returnTypeName = returnType.toString();
    }

    @Override
    public String toString() {
        return String.format("Line:%d:return type must be %s", line, returnTypeName);
    }
}
