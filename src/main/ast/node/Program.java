package ast.node;

import ast.Visitor;
import java.util.ArrayList;
import ast.node.declaration.ClassDeclaration;
import java.util.List;

public class Program extends Node {
    private ArrayList<ClassDeclaration> classes = new ArrayList<>();

    public Program(int line) {
        super(line);
    }

    public void addClass(ClassDeclaration classDeclaration) {
        classes.add(classDeclaration);
    }

    public List<ClassDeclaration> getClasses() {
        return classes;
    }

    @Override
    public String toString() {
        return "Program";
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
