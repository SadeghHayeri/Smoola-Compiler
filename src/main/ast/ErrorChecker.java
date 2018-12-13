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
import errors.ErrorPhase;
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
import errors.methodError.mainMethodError.VarDeclareInnMainMethod;
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

public class ErrorChecker {
    public static ArrayList<Error> errors = new ArrayList<>();

    static boolean hasCriticalError() {
        for(Error error : errors)
            if(error.isCriticalError())
                return true;
        return false;
    }

    static boolean hasError() {
        return !errors.isEmpty();
    }

    public static ArrayList<Error> getErrors() {
        errors.sort(Comparator.comparingInt(Error::getLine));
        return errors;
    }

    public static void addError(Error error) {
        errors.add(error);
    }

    static void checkHasAnyClass(Program program) {
        if(program.getClasses().isEmpty())
            errors.add(new NoClassExist());
    }

    static ArrayList<Error> getOnlyPhaseErrors(ErrorPhase phase) {
        ArrayList<Error> phaseErrors = new ArrayList<>();
        for(Error error : errors)
            if(error.whichPhase() == phase)
                phaseErrors.add(error);
        return phaseErrors;
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
        if(hasCircularInheritance(classesDeclaration))
            errors.add(new CircularInheritance());
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

                        if(!mainClass.getParentName().getName().equals(Util.MASTER_OBJECT_NAME))
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

                        boolean isVarDeclareInMainClass = !mainClass.getVarDeclarations().isEmpty();
                        if(isVarDeclareInMainClass)
                            errors.add(new VarDeclareInMainClass(mainClass));

                        boolean isVarDeclareInMainMethod = !mainMethod.getLocalVars().isEmpty();
                        if(isVarDeclareInMainMethod)
                            errors.add(new VarDeclareInnMainMethod(mainMethod));
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
}
