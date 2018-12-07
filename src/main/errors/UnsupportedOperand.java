package errors;

import ast.node.expression.BinaryOperator;
import ast.node.expression.NewArray;
import ast.node.expression.Operator;
import ast.node.expression.UnaryOperator;

public class UnsupportedOperand extends Error {

    private Operator operator;
    public UnsupportedOperand(int line, BinaryOperator operator) {
        super(line);
        this.operator = operator;
    }

    public UnsupportedOperand(int line, UnaryOperator operator) {
        super(line);
        this.operator = operator;
    }

    @Override
    public String toString() {
        return String.format("Line:%d:unsupported operand type for %s", line, operator.toString());
    }
}
