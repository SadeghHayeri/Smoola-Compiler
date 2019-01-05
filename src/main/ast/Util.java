package ast;

import ast.Type.NoType;
import ast.Type.PrimitiveType.BooleanType;
import ast.Type.PrimitiveType.IntType;
import ast.Type.PrimitiveType.StringType;
import ast.Type.Type;
import ast.node.expression.*;
import ast.node.expression.Value.BooleanValue;
import ast.node.expression.Value.IntValue;
import ast.node.expression.Value.StringValue;
import symbolTable.SymbolTable;

import java.util.HashMap;
import java.util.UUID;

public class Util {

    public static final boolean colorful = false;
    public static final String MASTER_OBJECT_NAME = "Object";
    public static final int MAX_STACK = 30;
    public static final int MAX_LOCALS = 30;

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static void error(String message) {
        if(colorful)
            System.out.println(ANSI_RED + message + ANSI_RESET);
        else
            System.out.println(message);
    }

    public static void info(String message) {
        if(colorful)
            System.out.println(ANSI_GREEN + message + ANSI_RESET);
        else
            System.out.println(message);
    }

    public static String uniqueRandomString() {
        return UUID.randomUUID().toString();
    }

    public static String findClassNameBySymbolTable(HashMap<String, SymbolTable> classesSymbolTable, SymbolTable symbolTable) {
        for(String key : classesSymbolTable.keySet())
            if(classesSymbolTable.get(key) == symbolTable)
                return key;
        return null;
    }
}
