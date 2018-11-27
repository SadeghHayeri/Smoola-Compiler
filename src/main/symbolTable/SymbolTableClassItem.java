package symbolTable;

import ast.Type.Type;

import java.util.ArrayList;

public class SymbolTableClassItem extends SymbolTableItem {

    private String parentName = "";
    private boolean hasParent = false;

    public SymbolTableClassItem(String name, String parentName) {
        this.name = name;
        this.parentName = parentName;
        this.hasParent = true;
    }

    public SymbolTableClassItem(String name) {
        this.name = name;
        this.hasParent = false;
    }

    @Override
    public String getKey() {
        return this.name;
    }
}
