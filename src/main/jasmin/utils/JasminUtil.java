package jasmin.utils;

import ast.Type.Type;
import static ast.TypeChecker.*;

public class JasminUtil {
    public static String toJasminType(Type type) {
        if(isBoolean(type))
            return "Z";
        if(isInt(type))
            return "I";
        if(isString(type))
            return "Ljava/lang/String";
        if(isArray(type))
            return "[I";
        if(isUserDefined(type))
            return "L" + UD(type).getName().getName();

        assert false;
        return "<BAD TYPE>";
    }
}
