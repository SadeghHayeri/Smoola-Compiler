package errors.classError;

import ast.node.expression.Expression;
import errors.Error;

public class CircularInheritance extends ClassError {
    public CircularInheritance() {
        super(0);
    }

    @Override
    public String toString() {
        return String.format("Line:%d:Circular inheritance occur", line);
    }
}
