package ast.node;

import ast.Visitor;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import ast.node.declaration.ClassDeclaration;
import jasmin.instructions.JasminStmt;

import java.util.List;

public class Program extends Node {
    private ArrayList<ClassDeclaration> classes = new ArrayList<>();

    public Program(int line) {
        super(line);
    }

    public boolean hasAnyClass() {
        return !this.classes.isEmpty();
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

    public void compile() {
        try {
            for(ClassDeclaration classDeclaration : classes) {
                PrintWriter writer = new PrintWriter(classDeclaration.getName().getName() + ".j", "UTF-8");

                for(JasminStmt jasminStmt : classDeclaration.toJasmin())
                    writer.println(jasminStmt.toString());

                writer.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
