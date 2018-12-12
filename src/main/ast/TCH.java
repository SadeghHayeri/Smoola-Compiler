package ast;

import ast.Type.ArrayType.ArrayType;
import ast.Type.NoType;
import ast.Type.PrimitiveType.BooleanType;
import ast.Type.PrimitiveType.IntType;
import ast.Type.PrimitiveType.StringType;
import ast.Type.Type;
import ast.Type.UserDefinedType.UserDefinedType;
import ast.node.expression.Expression;

public class TCH {
    static BooleanType BOOL(Type type)      { return (BooleanType) type; }
    static IntType INT(Type type)           { return (IntType) type; }
    static StringType STR(Type type)        { return (StringType) type; }
    static UserDefinedType UD(Type type)    { return (UserDefinedType) type; }
    static ArrayType ARR(Type type)         { return (ArrayType) type; }
    static NoType NOTYPE(Type type)         { return (NoType) type; }

    static boolean isBoolean(Type type)     { return type instanceof BooleanType; }
    static boolean isInt(Type type)         { return type instanceof IntType; }
    static boolean isString(Type type)      { return type instanceof StringType; }
    static boolean isArray(Type type)       { return type instanceof ArrayType; }
    static boolean isUserDefined(Type type) { return type instanceof UserDefinedType; }
    static boolean isNoType(Type type)      { return type instanceof NoType; }

    static boolean isBooleanOrNoType(Type type)     { return isBoolean(type) || isNoType(type); }
    static boolean isIntOrNoType(Type type)         { return isInt(type) || isNoType(type); }
    static boolean isStringOrNoType(Type type)      { return isString(type) || isNoType(type); }
    static boolean isArrayOrNoType(Type type)       { return isArray(type) || isNoType(type); }
    static boolean isUserDefinedOrNoType(Type type) { return isUserDefined(type) || isNoType(type); }

    // only for debug
    static String typeToString(Type type) {
        if(isBoolean(type))         return "BOOLEAN";
        if(isInt(type))             return "INT";
        if(isString(type))          return "STRING";
        if(isArray(type))           return "ARRAY";
        if(isUserDefined(type))     return "USER_DEFINED_" + UD(type).getName();
        if(isNoType(type))          return "NO_TYPE";

        assert false;
        return "BAD_TYPE";
    }

    static boolean haveSameType(Type left, Type right, boolean strict) {
        if(!strict && (isNoType(left) || isNoType(right)))
            return true;
        return typeToString(left).equals(typeToString(right));
    }
    static boolean haveSameType(Type left, Type right) { return haveSameType(left, right, false); }
}





















