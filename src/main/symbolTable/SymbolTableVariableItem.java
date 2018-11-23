package symbolTable;

import ast.Type.Type;

public class SymbolTableVariableItemBase extends SymbolTableItem {

    private int index;
    protected Type type;
    private static int INDEX = 0;

    public SymbolTableVariableItemBase(String name, Type type) {
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
        return name;
    }

    public int getIndex() {
        return index;
    }


}