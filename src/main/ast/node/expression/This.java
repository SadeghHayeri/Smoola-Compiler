package ast.node.expression;

import ast.Visitor;
import ast.node.declaration.ClassDeclaration;
import jasmin.instructions.JasminStmt;
import jasmin.instructions.Jcomment;
import jasmin.instructions.Jload;
import jasmin.instructions.JrefType;

import java.util.ArrayList;

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

    @Override
    public ArrayList<JasminStmt> toJasmin() {
        ArrayList<JasminStmt> code = new ArrayList<>();

        code.add(new Jcomment("Start this"));
        code.add(new Jload(JrefType.a, 0));
        code.add(new Jcomment("End this"));

        return code;
    }
}
