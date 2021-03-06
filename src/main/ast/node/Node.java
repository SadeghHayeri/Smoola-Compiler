package ast.node;

import ast.Visitor;
import jasmin.instructions.JasminStmt;
import java.util.ArrayList;

public abstract class Node {
    protected int line;

    public Node(int line) {
        this.line = line;
    }

    public Node() {
    }

    public void setLine(int line) { this.line = line; }
    public int getLine() { return this.line; }
    public void accept(Visitor visitor) {}

    public ArrayList<JasminStmt> toJasmin() { return null; }
}
