package symbolTable;

import ast.Type.Type;
import exceptions.RedefinitionOfVariableException;

public class SymbolTableVariableItem extends SymbolTableItem {
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
        return "_VAR_" + this.name;
    }

    public int getIndex() {
        return index;
    }
}