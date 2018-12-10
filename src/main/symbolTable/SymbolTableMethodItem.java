package symbolTable;

import ast.Type.Type;

import java.util.ArrayList;

public class SymbolTableMethodItem extends SymbolTableItem {

    public static final String PREFIX = "_DEF_";
    private ArrayList<Type> argTypes = new ArrayList<>();
    private Type returnType;

    public SymbolTableMethodItem(String name, ArrayList<Type> argTypes, Type returnType) {
        this.name = name;
        this.argTypes = argTypes;
        this.returnType = returnType;
    }

    public ArrayList<Type> getArgTypes() {
        return argTypes;
    }

    public Type getReturnType() {
        return returnType;
    }

    @Override
    public String getKey() {
        return PREFIX + this.name;
    }
}
