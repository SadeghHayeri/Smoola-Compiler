package symbolTable;

import ast.Type.Type;

public class SymbolTableVariableItem extends SymbolTableItem {

    public static final String PREFIX = "_VAR_";
    private int index;
    protected Type type;
    private static int INDEX = 0;

    public SymbolTableVariableItem(String name, Type type) {
        this.name = name;
        this.type = type;
        this.index = INDEX++;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String getKey() {
        return PREFIX + this.name;
    }

    public int getIndex() {
        return index;
    }
}