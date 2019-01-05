package ast.node;

import ast.Visitor;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import ast.node.declaration.ClassDeclaration;
import jasmin.instructions.*;

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
                PrintWriter writer = new PrintWriter("../../out/" + classDeclaration.getName().getName() + ".j", "UTF-8");

                for(JasminStmt jasminStmt : classDeclaration.toJasmin()) {
                    if(jasminStmt instanceof JstartClass || jasminStmt instanceof JsuperClass)
                        writer.println(jasminStmt.toString());
                    else if(jasminStmt instanceof JstartMethod || jasminStmt instanceof JendMethod || jasminStmt instanceof Jlabel)
                        writer.println("\t" + jasminStmt.toString());
                    else
                        writer.println("\t\t" +jasminStmt.toString());

                    if(jasminStmt instanceof JendMethod || jasminStmt instanceof JsuperClass)
                        writer.println("");
                }

                writer.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
