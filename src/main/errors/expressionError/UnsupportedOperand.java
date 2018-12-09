package errors.expressionError;

import ast.node.expression.*;

public class UnsupportedOperand extends ExpressionError {

    private Operator operator;
    public UnsupportedOperand(BinaryExpression binaryExpression) {
        super(binaryExpression.getLine());
        this.operator = binaryExpression.getBinaryOperator();
    }

    public UnsupportedOperand(UnaryExpression unaryExpression) {
        super(unaryExpression.getLine());
        this.operator = unaryExpression.getUnaryOperator();
    }

    @Override
    public String toString() {
        return String.format("Line:%d:unsupported operand type for %s", line, operator.toString());
    }
}
