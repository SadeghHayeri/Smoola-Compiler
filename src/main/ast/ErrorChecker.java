package ast;

import ast.Type.PrimitiveType.IntType;
import ast.node.Program;
import ast.node.declaration.ClassDeclaration;
import ast.node.declaration.MethodDeclaration;
import errors.Error;
import errors.classError.CircularInheritance;
import errors.classError.NoClassExist;
import errors.classError.mainClassError.BadMainParent;
import errors.classError.mainClassError.MainNotFound;
import errors.classError.mainClassError.TooManyMethods;
import errors.methodError.mainMethodError.BadMainArgs;
import errors.methodError.mainMethodError.BadMainReturnType;

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
}
