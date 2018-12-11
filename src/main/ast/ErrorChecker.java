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
import errors.classError.mainClassError.BadMainParent;
import errors.classError.mainClassError.MainNotFound;
import errors.classError.mainClassError.TooManyMethods;
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
import symbolTable.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

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
        if(!program.getClasses().isEmpty()) {
            ClassDeclaration firstClass = program.getClasses().get(0);
            MethodDeclaration mainMethod = getMainMethod(firstClass);
            boolean hasTooManyMethods = firstClass.getMethodDeclarations().size() != 1;

            if(firstClass.getParentName() != null)
                errors.add(new BadMainParent(firstClass));
            if(mainMethod != null && hasTooManyMethods)
                errors.add(new TooManyMethods(firstClass));
            if(mainMethod == null)
                errors.add(new MainNotFound());

            if(mainMethod != null) {
                boolean badMainMethodArgs = !mainMethod.getArgs().isEmpty();
                boolean badMainReturnType = !(mainMethod.getReturnType() instanceof IntType);

                if(badMainMethodArgs)
                    errors.add(new BadMainArgs(mainMethod));
                if(badMainReturnType)
                    errors.add(new BadMainReturnType(mainMethod));
            }
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

    static public Type findExpType(HashMap<String, ClassDeclaration> classesDeclaration, HashMap<String, SymbolTable> classesSymbolTable, Expression exp) {
        if (exp instanceof BooleanValue) {
            return new BooleanType();
        } else if (exp instanceof IntValue) {
            return new IntType();
        } else if (exp instanceof StringValue) {
            return new StringType();
        } else if (exp instanceof ArrayCall) {
            ArrayCall arrayCall = (ArrayCall)exp;
            Type instanceType = findExpType(classesDeclaration, classesSymbolTable, arrayCall.getInstance());

            if(!(instanceType instanceof ArrayType))
                errors.add(new ArrayExpected(arrayCall.getInstance()));

            return new IntType();
        } else if (exp instanceof BinaryExpression) {
            BinaryExpression binaryExpression = (BinaryExpression)exp;
            Type leftType = findExpType(classesDeclaration, classesSymbolTable, binaryExpression.getLeft());
            Type rightType = findExpType(classesDeclaration, classesSymbolTable, binaryExpression.getRight());

            BinaryOperator operator = binaryExpression.getBinaryOperator();
            boolean arithmeticOperator =
                    operator == BinaryOperator.mult
                    || operator == BinaryOperator.div
                    || operator == BinaryOperator.add
                    || operator == BinaryOperator.sub;

            boolean logicalOperator =
                    operator == BinaryOperator.and
                    || operator == BinaryOperator.or;

            boolean relationalOperator =
                    operator == BinaryOperator.gt
                    || operator == BinaryOperator.lt
                    || operator == BinaryOperator.eq
                    || operator == BinaryOperator.neq;

            boolean eqOrNeq =
                    operator == BinaryOperator.eq
                    || operator == BinaryOperator.neq;

            boolean isAssign =
                    operator == BinaryOperator.assign;

            if(arithmeticOperator) {
                bothSideInt(binaryExpression, leftType, rightType);
                return new IntType();
            }
            else if(logicalOperator) {
                if(!(leftType instanceof BooleanType || leftType instanceof NoType))
                    errors.add(new UnsupportedOperand(binaryExpression));
                if(!(rightType instanceof BooleanType || rightType instanceof NoType))
                    errors.add(new UnsupportedOperand(binaryExpression));
                return new BooleanType();
            }
            else if(relationalOperator) {
                if(eqOrNeq) {
                    if (leftType instanceof NoType)
                        return rightType;
                    if (rightType instanceof NoType)
                        return leftType;
                    if (leftType instanceof BooleanType && rightType instanceof BooleanType)
                        return new BooleanType();
                    else if (leftType instanceof IntType && rightType instanceof IntType)
                        return new IntType();
                    else if (leftType instanceof StringType && rightType instanceof StringType)
                        return new StringType();
                    else if (leftType instanceof ArrayType && rightType instanceof ArrayType) {
                        ArrayType leftArray = (ArrayType)leftType;
                        ArrayType rightArray = (ArrayType)rightType;
                        if(leftArray.getSize() != rightArray.getSize()) {
                            //TODO: set size first!
                            errors.add(new UnsupportedOperand(binaryExpression));
                            return new NoType();
                        }
                        return leftArray;
                    }
                    else if(leftType instanceof UserDefinedType && rightType instanceof UserDefinedType) {
                        String leftTypeName = ((UserDefinedType)leftType).getName().getName();
                        String rightTypeName = ((UserDefinedType)rightType).getName().getName();

                        if(leftTypeName.equals(rightTypeName)) {
                            return ((UserDefinedType)leftType);
                        } else {
                            errors.add(new UnsupportedOperand(binaryExpression));
                            return new NoType();
                        }
                    }
                } else {
                    bothSideInt(binaryExpression, leftType, rightType);
                    return new BooleanType();
                }
            } else if(isAssign) {
                if(!(binaryExpression.getLeft() instanceof Identifier || binaryExpression.getLeft() instanceof ArrayCall))
                    errors.add(new BadLeftValue(binaryExpression));

                if(leftType instanceof IntType && rightType instanceof IntType)
                    return new IntType();
                else if(leftType instanceof BooleanType && rightType instanceof BooleanType)
                    return new BooleanType();
                else if(leftType instanceof ArrayType && rightType instanceof ArrayType)
                    return new ArrayType();
                else if(leftType instanceof StringType && rightType instanceof StringType)
                    return new StringType();
                else if(leftType instanceof UserDefinedType && rightType instanceof UserDefinedType) {
                    String leftTypeName = ((UserDefinedType)leftType).getName().getName();
                    String rightTypeName = ((UserDefinedType)rightType).getName().getName();
                    if (isSubType(classesDeclaration, leftTypeName, rightTypeName))
                        return leftType;
                } else if(leftType instanceof NoType) {
                    return rightType;
                } else if(rightType instanceof NoType) {
                    return leftType;
                } else {
                    errors.add(new UnsupportedOperand(binaryExpression));
                    return new NoType();
                }
            }
            return new NoType();
        } else if (exp instanceof UnaryExpression) {
            UnaryExpression unaryExpression = (UnaryExpression) exp;
            Type expType = findExpType(classesDeclaration, classesSymbolTable, unaryExpression.getValue());

            UnaryOperator operator = unaryExpression.getUnaryOperator();

            if(operator == UnaryOperator.minus) {
                if(!(expType instanceof IntType))
                    errors.add(new UnsupportedOperand(unaryExpression));
                return new IntType();
            } else if(operator == UnaryOperator.not) {
                if(!(expType instanceof BooleanType))
                    errors.add(new UnsupportedOperand(unaryExpression));
                return new BooleanType();
            }
        } else if (exp instanceof Identifier) {
            Identifier identifier = (Identifier)exp;
            return getIdentifierType(identifier);
        } else if (exp instanceof Length) {
            Expression arrayExp = ((Length)exp).getExpression();
            Type instanceType = findExpType(classesDeclaration, classesSymbolTable, arrayExp);

            if(!(instanceType instanceof ArrayType))
                errors.add(new ArrayExpected(arrayExp));

            return new IntType();
        } else if (exp instanceof MethodCall) {
            MethodCall methodCall = (MethodCall)exp;
            String methodName = methodCall.getMethodName().getName();
            ArrayList<Expression> args = methodCall.getArgs();

            Expression instance = methodCall.getInstance();

            Type instanceType = findExpType(classesDeclaration, classesSymbolTable, instance);

            if(instanceType instanceof UserDefinedType) {
                String className = ((UserDefinedType)instanceType).getName().getName();
                if(classesSymbolTable.containsKey(className)) {
                    SymbolTable classSymbolTable = classesSymbolTable.get(className);

                    try {
                        SymbolTableMethodItem methodItem = (SymbolTableMethodItem)classSymbolTable.get(SymbolTableMethodItem.PREFIX + methodName);
                        ArrayList<Type> argsType = methodItem.getArgTypes();

                        if(args.size() == argsType.size()) {
                            for(int i = 0; i < args.size(); i++) {
                                Type currCalledArgType = findExpType(classesDeclaration, classesSymbolTable, args.get(i));
                                Type currMethodArgType = argsType.get(i);

                                if(!(currCalledArgType instanceof NoType)) {
                                    boolean twoSideUserDefinedType = currMethodArgType instanceof UserDefinedType && currCalledArgType instanceof UserDefinedType;
                                    boolean argMismatchType =
                                            currMethodArgType instanceof IntType && !(currCalledArgType instanceof IntType)
                                                    || currMethodArgType instanceof BooleanType && !(currCalledArgType instanceof BooleanType)
                                                    || currMethodArgType instanceof StringType && !(currCalledArgType instanceof StringType)
                                                    || currMethodArgType instanceof ArrayType && !(currCalledArgType instanceof ArrayType)
                                                    || currMethodArgType instanceof UserDefinedType && !(currCalledArgType instanceof UserDefinedType);

                                    if (argMismatchType) {
                                        errors.add(new ArgsMismatch(methodCall));
                                        return methodItem.getReturnType();
                                    } else if (twoSideUserDefinedType) {
                                        String methodArgClassName = ((UserDefinedType) currMethodArgType).getName().getName();
                                        String calledArgClassName = ((UserDefinedType) currCalledArgType).getName().getName();
                                        if (!isSubType(classesDeclaration, methodArgClassName, calledArgClassName)) {
                                            errors.add(new ArgsMismatch(methodCall));
                                            return methodItem.getReturnType();
                                        }
                                    }
                                }
                            }
                        } else {
                            errors.add(new ArgsMismatch(methodCall));
                        }
                    } catch (ItemNotFoundException e) {
                        errors.add(new UndefinedMethod(methodCall));
                        return new NoType();
                    }
                } else {
                    errors.add(new UndefinedClass(instance.getLine(), className));
                    return new NoType();
                }
            } else if(instanceType instanceof NoType) {
                return new NoType();
            } else {
                errors.add(new classExpected(methodCall));
                return new NoType();
            }
        } else if (exp instanceof NewArray) {
            Expression arrayExp = ((NewArray)exp).getExpression();
            Type arrayExpType = findExpType(classesDeclaration, classesSymbolTable, arrayExp);
            if(!(arrayExpType instanceof IntType || arrayExpType instanceof NoType))
                errors.add(new BadIndexType(arrayExp));
            return new ArrayType();
        } else if (exp instanceof NewClass) {
            String className = ((NewClass)exp).getClassName().getName();
            if(classesDeclaration.containsKey(className)) {
                return new UserDefinedType(new Identifier(exp.getLine(), className));
            } else {
                errors.add(new UndefinedClass(exp.getLine(), className));
                return new NoType();
            }
        } else if (exp instanceof This) {
            String className = ((This)exp).getClassRef().getName().getName();
            return new UserDefinedType(new Identifier(exp.getLine(), className));
        }

        return new NoType();
    }

    private static void bothSideInt(BinaryExpression binaryExpression, Type leftType, Type rightType) {
        if(!(leftType instanceof IntType || leftType instanceof NoType))
            errors.add(new UnsupportedOperand(binaryExpression));
        if(!(rightType instanceof IntType || rightType instanceof NoType))
            errors.add(new UnsupportedOperand(binaryExpression));
    }
}
