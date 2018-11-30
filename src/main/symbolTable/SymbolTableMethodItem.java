package symbolTable;

import ast.Type.Type;

import java.util.ArrayList;

public class SymbolTableMethodItem extends SymbolTableItem {

    public static final String PREFIX = "_DEF_";

    ArrayList<Type> argTypes = new ArrayList<>();

    public SymbolTableMethodItem(String name, ArrayList<Type> argTypes) {
        this.name = name;
        this.argTypes = argTypes;
    }

    @Override
    public String getKey() {
        return PREFIX + this.name;
    }
}
