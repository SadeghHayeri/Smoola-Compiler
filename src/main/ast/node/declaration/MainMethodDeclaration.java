package ast.node.declaration;

import ast.Visitor;
import ast.node.expression.Expression;
import ast.node.expression.Identifier;
import ast.node.statement.Statement;

import java.util.ArrayList;

public class MainMethodDeclaration extends MethodDeclaration {
    private Expression returnValue;
    private ArrayList<Statement> body = new ArrayList<>();

    public MainMethodDeclaration() {
        super(new Identifier("main"));
    }

    public Expression getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Expression returnValue) {
        this.returnValue = returnValue;
    }

    public ArrayList<Statement> getBody() {
        return body;
    }

    public void addStatement(Statement statement) {
        this.body.add(statement);
    }

    @Override
    public String toString() {
        return "MainMethodDeclaration";
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
