package ast.node.statement;

import ast.Visitor;
import ast.node.expression.Expression;
import jasmin.instructions.*;
import jasmin.utils.JlabelGenarator;

import java.util.ArrayList;
import java.util.Arrays;

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

        String haveStringLabel = JlabelGenarator.unique("have_string");
        String haveArrayLabel = JlabelGenarator.unique("have_array");
        String haveIntLabel = JlabelGenarator.unique("have_int");
        String finishLabel = JlabelGenarator.unique("finish");

        code.addAll(arg.toJasmin());
        code.add(new Jdup());

        code.add(new Jinstanceof("java/lang/String"));
        code.add(new Jif(JrefType.a, JifOperator.ne, haveStringLabel));

        code.add(new Jinstanceof("[I"));
        code.add(new Jif(JrefType.a, JifOperator.ne, haveArrayLabel));

        code.add(new Jgoto(haveIntLabel));

        code.add(new Jlabel(haveStringLabel)); // string
        code.add(new Jpop());
        code.add(new Jinvokevirtual("java/io/PrintStream", "println", "Ljava/lang/String;", "V"));
        code.add(new Jgoto(finishLabel));

        code.add(new Jlabel(haveArrayLabel)); // array
        code.add(new Jinvokevirtual("java/io/PrintStream", "println", "Ljava/lang/String;", "V"));
        code.add(new Jgoto(finishLabel));

        code.add(new Jlabel(haveIntLabel)); // int
        code.add(new Jinvokestatic("java/util/Arrays", "toString", "[I", "Ljava/lang/String"));
        code.add(new Jinvokevirtual("java/io/PrintStream", "println", "Ljava/lang/String;", "V"));
        code.add(new Jgoto(finishLabel));

        code.add(new Jlabel(finishLabel)); // finish

        code.add(new Jcomment("End writeln"));
        return code;
    }
}
