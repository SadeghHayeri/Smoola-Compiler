package jasmin.utils;

import jasmin.instructions.JasminStmt;

import java.util.ArrayList;

public class Jbranch {
    String nTrue, nFalse;
    public Jbranch(String nTrue, String nFalse) {
        this.nTrue = nTrue;
        this.nFalse = nFalse;
    }

    public ArrayList<JasminStmt> toJasmin() {
        return null; //TODO complete
    }
}
