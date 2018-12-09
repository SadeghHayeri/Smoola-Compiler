package ast.Type;

import ast.node.declaration.ClassDeclaration;
import ast.node.expression.Identifier;

public class NoType extends Type {

    public NoType() { }

    @Override
    public String toString() {
        return "NoType";
    }
}
