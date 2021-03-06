package ast.node.expression;

import ast.Visitor;
import jasmin.instructions.*;

import java.util.ArrayList;

public class NewClass extends Expression {
    private Identifier className;

    public NewClass(int line, Identifier className) {
        super(line);
        this.className = className;
    }

    public Identifier getClassName() {
        return className;
    }

    public void setClassName(Identifier className) {
        this.className = className;
    }

    @Override
    public String toString() {
        return "NewClass";
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public ArrayList<JasminStmt> toJasmin() {
        ArrayList<JasminStmt> code = new ArrayList<>();
        code.add(new Jcomment("Start new-class"));
        code.add(new Jnew(className.getName()));
        code.add(new Jdup());
        code.add(new Jinvoke(JinvokeType.SPECIAL, className.getName(), "<init>", "", "V"));
        code.add(new Jcomment("End new-class"));

        return code;
    }
}
