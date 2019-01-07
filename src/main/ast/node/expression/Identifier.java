package ast.node.expression;

import ast.Visitor;
import ast.node.declaration.MethodDeclaration;
import jasmin.instructions.JasminStmt;
import jasmin.instructions.Jcomment;

import java.util.ArrayList;

public class Identifier extends Expression {
    private String name;
    private MethodDeclaration containerMethod;

    public MethodDeclaration getContainerMethod() {
        return containerMethod;
    }

    public void setContainerMethod(MethodDeclaration containerMethod) {
        this.containerMethod = containerMethod;
    }

    public Identifier(int line, String name) {
        super(line);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Identifier " + name;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public ArrayList<JasminStmt> toJasmin() {
        ArrayList<JasminStmt> code = new ArrayList<>();

        code.add(new Jcomment("Start identifier"));
        code.addAll(this.containerMethod.getVariableJasmin(this.name));
        code.add(new Jcomment("End identifier"));

        return code;
    }

    public ArrayList<JasminStmt> toStoreJasmin() {
        ArrayList<JasminStmt> code = new ArrayList<>();
        code.add(new Jcomment("Start store-identifier"));
        code.addAll(this.containerMethod.setVariableJasmin(this.name));
        code.add(new Jcomment("End store-identifier"));

        return code;
    }
}
