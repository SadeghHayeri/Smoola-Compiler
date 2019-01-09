package ast.node.statement;

import ast.Visitor;
import ast.node.expression.Expression;
import jasmin.instructions.*;
import jasmin.utils.JlabelGenarator;

import java.util.ArrayList;
import java.util.Arrays;

import static ast.TypeChecker.*;

public class Write extends Statement {
    private Expression arg;

    public Write(int line, Expression arg) {
        super(line);
        this.arg = arg;
    }

    public Expression getArg() {
        return arg;
    }

    public void setArg(Expression arg) {
        this.arg = arg;
    }

    @Override
    public String toString() {
        return "Write";
    }
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public ArrayList<JasminStmt> toJasmin() {
        ArrayList<JasminStmt> code = new ArrayList<>();

        code.add(new Jcomment("Start writeln"));

        code.add(new Jget(JgetType.STATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
        code.addAll(arg.toJasmin());

        if(isString(arg.getExpressionType())) {
            code.add(new Jinvoke(JinvokeType.VIRTUAL, "java/io/PrintStream", "println", "Ljava/lang/String;", "V"));
        }
        else if(isInt(arg.getExpressionType())) {
            code.add(new Jinvoke(JinvokeType.VIRTUAL, "java/io/PrintStream", "println", "I", "V"));
        }
        else if(isArray(arg.getExpressionType())) {
            code.add(new Jinvoke(JinvokeType.STATIC, "java/util/Arrays", "toString", "[I", "Ljava/lang/String;"));
            code.add(new Jinvoke(JinvokeType.VIRTUAL, "java/io/PrintStream", "println", "Ljava/lang/String;", "V"));
        }

        code.add(new Jcomment("End writeln"));
        return code;
    }
}
