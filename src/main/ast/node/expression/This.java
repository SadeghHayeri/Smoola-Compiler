package ast.node.expression;

import ast.Visitor;
import ast.node.declaration.ClassDeclaration;

public class This extends Expression {

    private ClassDeclaration classRef;
    public This(int line) {
        super(line);
    }

    public void setClassRef(ClassDeclaration classRef) {
        this.classRef = classRef;
    }

    public ClassDeclaration getClassRef() {
        return classRef;
    }

    @Override
    public String toString() {
        return "This";
    }
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
