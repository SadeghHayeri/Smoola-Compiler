package ast.node.declaration;

import ast.Type.Type;
import ast.Visitor;
import ast.node.expression.Identifier;

public class VarDeclaration extends Declaration {
    private Identifier identifier;
    private Type type;
    private ClassDeclaration containerClass;

    public VarDeclaration(int line, Identifier identifier, Type type) {
        super(line);
        this.identifier = identifier;
        this.type = type;
    }

    public void setContainerClass(ClassDeclaration containerClass) {
        this.containerClass = containerClass;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Identifier identifier) {
        this.identifier = identifier;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "VarDeclaration";
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
