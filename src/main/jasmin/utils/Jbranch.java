package jasmin.utils;

import jasmin.instructions.JasminStmt;
import jasmin.instructions.Jgoto;
import jasmin.instructions.Jif;
import jasmin.instructions.JifOperator;

import java.util.ArrayList;

public class Jbranch {
    String nTrue, nFalse;
    public Jbranch(String nTrue, String nFalse) {
        this.nTrue = nTrue;
        this.nFalse = nFalse;
    }

    public ArrayList<JasminStmt> toJasmin() {
        ArrayList<JasminStmt> code = new ArrayList<>();
        code.add(new Jif(JifOperator.gt, nTrue));
        code.add(new Jgoto(nFalse));
        return code;
    }
}
