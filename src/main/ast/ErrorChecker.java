package ast;

import ast.Type.ArrayType.ArrayType;
import ast.Type.NoType;
import ast.Type.PrimitiveType.BooleanType;
import ast.Type.PrimitiveType.IntType;
import ast.Type.PrimitiveType.StringType;
import ast.Type.Type;
import ast.Type.UserDefinedType.UserDefinedType;
import ast.node.Program;
import ast.node.declaration.ClassDeclaration;
import ast.node.declaration.MethodDeclaration;
import ast.node.expression.*;
import ast.node.expression.Value.BooleanValue;
import ast.node.expression.Value.IntValue;
import ast.node.expression.Value.StringValue;
import errors.Error;
import errors.classError.CircularInheritance;
import errors.classError.NoClassExist;
import errors.classError.UndefinedClass;
import errors.classError.mainClassError.*;
import errors.expressionError.ArrayExpected;
import errors.expressionError.BadIndexType;
import errors.expressionError.UnsupportedOperand;
import errors.methodError.ArgsMismatch;
import errors.methodError.UndefinedMethod;
import errors.methodError.classExpected;
import errors.methodError.mainMethodError.BadMainArgs;
import errors.methodError.mainMethodError.BadMainReturnType;
import errors.statementError.BadLeftValue;
import errors.variableError.UndefinedVariable;
import symbolTable.ItemNotFoundException;
import symbolTable.SymbolTable;
import symbolTable.SymbolTableMethodItem;
import symbolTable.SymbolTableVariableItem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import static ast.TCH.*;

public class ErrorChecker {
    public static ArrayList<Error> errors = new ArrayList<>();
    private static boolean hasCriticalError = false;

    static boolean hasCriticalError() {
        return hasCriticalError;
    }

    static boolean hasError() {
        return !errors.isEmpty();
    }

    public static ArrayList<Error> getErrors() {
        errors.sort(Comparator.comparingInt(Error::getLine));
        return errors;
    }

    static void addError(Error error) {
        errors.add(error);
    }

    static void checkHasAnyClass(Program program) {
        if(program.getClasses().isEmpty()) {
            errors.add(new NoClassExist());
            hasCriticalError = true;
        }
    }

    static private boolean hasCircularInheritance(HashMap<String, ClassDeclaration> classesDeclaration) {
        for(ClassDeclaration classDeclaration : classesDeclaration.values()) {
            ClassDeclaration ptr1 = classDeclaration;
            ClassDeclaration ptr2 = classDeclaration;

            while(ptr2 != null) {
                ptr1 = getParent(classesDeclaration, ptr1); // Move one by one
                ptr2 = getParent(classesDeclaration, ptr2); // Move two by two
                if(ptr2 != null)
                    ptr2 = getParent(classesDeclaration, ptr2);
                else
                    break;

                if(ptr1 == ptr2)
                    return true;
            }
        }
        return false;
    }

    static void checkCircularInheritance(HashMap<String, ClassDeclaration> classesDeclaration) {
        if(hasCircularInheritance(classesDeclaration)) {
            errors.add(new CircularInheritance());
            hasCriticalError = true;
        }
    }

    static private MethodDeclaration getMainMethod(ClassDeclaration classDeclaration) {
        ArrayList<MethodDeclaration> methods = classDeclaration.getMethodDeclarations();
        for(MethodDeclaration method : methods)
            if(method.getName().getName().equals("main"))
                return method;
        return null;
    }

    static void checkMainClassErrors(Program program) {
        boolean mainClassSeen = false;
        List<ClassDeclaration> classes = program.getClasses();
        if(!classes.isEmpty()) {
            for (int i = 0; i < classes.size(); i++) {
                boolean isMainClass = getMainMethod(classes.get(i)) != null;
                if(isMainClass) {
                    ClassDeclaration mainClass = classes.get(i);
                    MethodDeclaration mainMethod = getMainMethod(mainClass);

                    if(mainClassSeen) {
                        errors.add(new MainRedefinition(mainClass));
                    } else {
                        if(i != 0)
                            errors.add(new BadMainPlacement(mainClass));

                        if(mainClass.getParentName() != null)
                            errors.add(new BadMainParent(mainClass));

                        boolean hasTooManyMethods = mainClass.getMethodDeclarations().size() != 1;
                        if(hasTooManyMethods)
                            errors.add(new TooManyMethods(mainClass));

                        boolean badMainMethodArgs = !mainMethod.getArgs().isEmpty();
                        if(badMainMethodArgs)
                            errors.add(new BadMainArgs(mainMethod));

                        boolean badMainReturnType = !(mainMethod.getReturnType() instanceof IntType);
                        if(badMainReturnType)
                            errors.add(new BadMainReturnType(mainMethod));
                    }
                    mainClassSeen = true;
                }
            }
            if(!mainClassSeen)
                errors.add(new MainNotFound());
        }
    }

    static private ClassDeclaration getParent(HashMap<String, ClassDeclaration> classesDeclaration, ClassDeclaration classDeclaration) {
        if(!classDeclaration.hasParent())
            return null;

        String parentName = classDeclaration.getParentName().getName();
        if(!classesDeclaration.containsKey(parentName))
            return null;

        return classesDeclaration.get(parentName);
    }

    static private Type getIdentifierType(Identifier identifier) {
        try {
            SymbolTableVariableItem item = (SymbolTableVariableItem)SymbolTable.top.get(SymbolTableVariableItem.PREFIX + identifier.getName());
            return item.getType();
        } catch (ItemNotFoundException e) {
            errors.add(new UndefinedVariable(identifier));
            return new NoType();
        }
    }

    static public boolean isSubType(HashMap<String, ClassDeclaration> classesDeclaration, String leftName, String rightName) {
        while (true) {
            if(leftName.equals(rightName))
                return true;

            ClassDeclaration right = classesDeclaration.get(rightName);
            if(!right.hasParent())
                return false;

            rightName = right.getParentName().getName();
        }
    }

    static enum EXP_TYPE {
        BOOLEAN     ("BOOLEAN"),
        INT         ("INT"),
        STRING      ("STRING"),
        IDENTIFIER  ("IDENTIFIER"),
        THIS        ("THIS"),
        NEW_CLASS   ("NEW_CLASS"),
        METHOD_CALL ("METHOD_CALL"),
        ARRAY_CALL  ("ARRAY_CALL"),
        LENGTH      ("LENGTH"),
        NEW_ARRAY    ("NEW_ARRAY"),
        BINARY_EXP  ("BINARY_EXP"),
        UNARY_EXP   ("UNARY_EXP"),

        BAD_TYPE    ("BAD_TYPE");

        private final String name;
        EXP_TYPE(String name) { this.name = name; }
        public String toString() { return name; }
        public boolean equals(String otherName) { return name.equals(otherName); }
    }

    static boolean isBooleanValue(Expression exp)       { return exp instanceof BooleanValue; }
    static boolean isIntValue(Expression exp)           { return exp instanceof IntValue; }
    static boolean isStringValue(Expression exp)        { return exp instanceof StringValue; }
    static boolean isIdentifier(Expression exp)         { return exp instanceof Identifier; }
    static boolean isThis(Expression exp)               { return exp instanceof This; }
    static boolean isNewClass(Expression exp)           { return exp instanceof NewClass; }
    static boolean isMethodCall(Expression exp)         { return exp instanceof MethodCall; }
    static boolean isArrayCall(Expression exp)          { return exp instanceof ArrayCall; }
    static boolean isLength(Expression exp)             { return exp instanceof Length; }
    static boolean isNewArray(Expression exp)           { return exp instanceof NewArray; }
    static boolean isBinaryExpression(Expression exp)   { return exp instanceof BinaryExpression; }
    static boolean isUnaryExpression(Expression exp)    { return exp instanceof UnaryExpression; }

    static BooleanValue BOOL(Expression exp)      { return (BooleanValue) exp; }
    static IntValue INT(Expression exp)           { return (IntValue) exp; }
    static StringValue STR(Expression exp)        { return (StringValue) exp; }
    static Identifier ID(Expression exp)          { return (Identifier) exp; }
    static This THIS(Expression exp)              { return (This) exp; }
    static NewClass NC(Expression exp)            { return (NewClass) exp; }
    static MethodCall MC(Expression exp)          { return (MethodCall) exp; }
    static ArrayCall AC(Expression exp)           { return (ArrayCall) exp; }
    static Length LEN(Expression exp)             { return (Length) exp; }
    static NewArray NA(Expression exp)            { return (NewArray) exp; }
    static BinaryExpression BE(Expression exp)    { return (BinaryExpression) exp; }
    static UnaryExpression UE(Expression exp)     { return (UnaryExpression) exp; }

    // for debug
    static EXP_TYPE expType(Expression exp) {
        if(isBooleanValue(exp))         return EXP_TYPE.BOOLEAN;
        if(isIntValue(exp))             return EXP_TYPE.INT;
        if(isStringValue(exp))          return EXP_TYPE.STRING;
        if(isIdentifier(exp))           return EXP_TYPE.IDENTIFIER;
        if(isThis(exp))                 return EXP_TYPE.THIS;
        if(isNewClass(exp))             return EXP_TYPE.NEW_CLASS;
        if(isMethodCall(exp))           return EXP_TYPE.METHOD_CALL;
        if(isArrayCall(exp))            return EXP_TYPE.ARRAY_CALL;
        if(isLength(exp))               return EXP_TYPE.LENGTH;
        if(isNewArray(exp))             return EXP_TYPE.NEW_ARRAY;
        if(isBinaryExpression(exp))     return EXP_TYPE.BINARY_EXP;
        if(isUnaryExpression(exp))      return EXP_TYPE.UNARY_EXP;

        assert false;
        return EXP_TYPE.BAD_TYPE;
    }

    static boolean isLeftValue(Expression exp) {
        return isIdentifier(exp) || isArrayCall(exp);
    }

    static Type getExpType(HashMap<String, ClassDeclaration> classesDeclaration, HashMap<String, SymbolTable> classesSymbolTable, Expression exp) {
        switch (expType(exp)) {
            case BOOLEAN:
                return new BooleanType();
            case INT:
                return new IntType();
            case STRING:
                return new StringType();
            case IDENTIFIER:
                return getIdentifierType(ID(exp));
            case THIS:
                String refName = THIS(exp).getClassRef().getName().getName();
                return new UserDefinedType(new Identifier(exp.getLine(), refName));
            case NEW_CLASS:
                String className = NC(exp).getClassName().getName();
                if(classesDeclaration.containsKey(className))
                    return new UserDefinedType(new Identifier(exp.getLine(), className));
                else
                    errors.add(new UndefinedClass(exp.getLine(), className));
                return new NoType();
            case METHOD_CALL:
                return getMethodCallType(classesDeclaration, classesSymbolTable, MC(exp));
            case ARRAY_CALL:
                Type instanceType = getExpType(classesDeclaration, classesSymbolTable, AC(exp).getInstance());
                if(!isArrayOrNoType(instanceType))
                    errors.add(new ArrayExpected(AC(exp).getInstance()));
                return new IntType();
            case LENGTH:
                Type beforeExpType = getExpType(classesDeclaration, classesSymbolTable, LEN(exp).getExpression());
                if(!isArrayOrNoType(beforeExpType)) errors.add(new ArrayExpected(AC(exp)));
                return new IntType();
            case NEW_ARRAY:
                Type indexExpType = getExpType(classesDeclaration, classesSymbolTable, NA(exp).getExpression());
                if(!isIntOrNoType(indexExpType)) errors.add(new BadIndexType(NA(exp).getExpression()));
                return new ArrayType(); //TODO: set size!
            case BINARY_EXP:
                Type leftType = getExpType(classesDeclaration, classesSymbolTable, BE(exp).getLeft());
                Type rightType = getExpType(classesDeclaration, classesSymbolTable, BE(exp).getRight());

                BinaryOperator operator = BE(exp).getBinaryOperator();
                boolean isArithmeticOperator = operator == BinaryOperator.mult
                                || operator == BinaryOperator.div
                                || operator == BinaryOperator.add
                                || operator == BinaryOperator.sub;

                boolean isLogicalOperator = operator == BinaryOperator.and
                                || operator == BinaryOperator.or;

                boolean isEqOrNeq = operator == BinaryOperator.eq
                                || operator == BinaryOperator.neq;

                boolean isAssign = operator == BinaryOperator.assign;

                if(isArithmeticOperator) {
                    if(!isIntOrNoType(leftType)) errors.add(new UnsupportedOperand(BE(exp)));
                    if(!isIntOrNoType(rightType)) errors.add(new UnsupportedOperand(BE(exp)));
                    return new IntType();
                } else if(isLogicalOperator) {
                    if(!isBooleanOrNoType(leftType)) errors.add(new UnsupportedOperand(BE(exp)));
                    if(!isBooleanOrNoType(rightType)) errors.add(new UnsupportedOperand(BE(exp)));
                    return new BooleanType();
                } else if(isEqOrNeq) {
                    if(!haveSameType(leftType, rightType)) errors.add(new UnsupportedOperand(BE(exp)));
                    return new BooleanType();
                } else if(isAssign) {
                    if(!isLeftValue(BE(exp).getLeft())) errors.add(new BadLeftValue(BE(exp)));
                    if(canAssign(classesDeclaration, classesSymbolTable, leftType, rightType)) {
                        return leftType;
                    } else {
                        errors.add(new UnsupportedOperand(BE(exp)));
                        return new NoType();
                    }
                }

                assert false; //TODO: check no access!
                return new NoType();
            case UNARY_EXP:
                Type expType = getExpType(classesDeclaration, classesSymbolTable, UE(exp).getValue());
                UnaryOperator uop = UE(exp).getUnaryOperator();
                switch (uop) {
                    case not:
                        if(!isBooleanOrNoType(expType)) errors.add(new UnsupportedOperand(UE(exp)));
                        return new BooleanType();
                    case minus:
                        if(!isIntOrNoType(expType)) errors.add(new UnsupportedOperand(UE(exp)));
                        return new IntType();
                }

                assert false; //TODO: check no access!
                return new NoType();
            case BAD_TYPE:
                assert false;
                break;
        }

        assert false; //TODO: check no access!
        return new NoType();
    }

    private static Type getMethodCallType(HashMap<String, ClassDeclaration> classesDeclaration, HashMap<String, SymbolTable> classesSymbolTable, MethodCall methodCall) {
        Expression instance = methodCall.getInstance();
        String methodName = methodCall.getMethodName().getName();
        ArrayList<Expression> args = methodCall.getArgs();

        Type instanceType = getExpType(classesDeclaration, classesSymbolTable, instance);
        if(isNoType(instanceType)) return new NoType();
        if(isUserDefined(instanceType)) {
            String className = UD(instanceType).getName().getName();
            if(classesSymbolTable.containsKey(className)) {
                SymbolTable classSymbolTable = classesSymbolTable.get(className);
                try {
                    SymbolTableMethodItem methodItem = (SymbolTableMethodItem)classSymbolTable.get(SymbolTableMethodItem.PREFIX + methodName);
                    ArrayList<Type> argsType = methodItem.getArgTypes();
                    if(args.size() == argsType.size()) {
                        for(int i = 0; i < args.size(); i++) {
                            Type methodArgType = argsType.get(i);
                            Type calledArgType = getExpType(classesDeclaration, classesSymbolTable, args.get(i));
                            if(!canAssign(classesDeclaration, classesSymbolTable, methodArgType, calledArgType)) {
                                errors.add(new ArgsMismatch(methodCall));
                                return methodItem.getReturnType();
                            }
                        }
                        return methodItem.getReturnType();
                    } else errors.add(new ArgsMismatch(methodCall));
                } catch (ItemNotFoundException e) { errors.add(new UndefinedMethod(methodCall)); }
            } else errors.add(new UndefinedClass(instance.getLine(), className));
        } else errors.add(new classExpected(methodCall));
        return new NoType();
    }

    static public boolean canAssign(HashMap<String, ClassDeclaration> classesDeclaration, HashMap<String, SymbolTable> classesSymbolTable, Type leftType, Type rightType) {
        if (isNoType(leftType) || isNoType(rightType)) return true;
        if (isUserDefined(leftType) && isUserDefined(rightType)) {
            String leftTypeName = UD(leftType).getName().getName();
            String rightTypeName = UD(rightType).getName().getName();
            return isSubType(classesDeclaration, leftTypeName, rightTypeName);
        }
        return haveSameType(leftType, rightType);
    }

    private static void bothSideInt(BinaryExpression binaryExpression, Type leftType, Type rightType) {
        if(!(leftType instanceof IntType || leftType instanceof NoType))
            errors.add(new UnsupportedOperand(binaryExpression));
        if(!(rightType instanceof IntType || rightType instanceof NoType))
            errors.add(new UnsupportedOperand(binaryExpression));
    }
}
