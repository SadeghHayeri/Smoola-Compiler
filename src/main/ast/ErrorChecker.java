package ast;

import ast.node.Program;
import ast.node.declaration.ClassDeclaration;
import errors.Error;
import errors.classError.CircularInheritance;
import errors.classError.NoClassExist;
import symbolTable.SymbolTable;

import java.util.ArrayList;
import java.util.HashMap;

public class ErrorChecker {

    private Program program;
    private HashMap<String, SymbolTable> classesSymbolTable;
    private HashMap<String, ClassDeclaration> classesDeclaration;
    private ArrayList<Error> errors;
    private boolean hasCriticalError = false;

    boolean hasCriticalError() {
        return hasCriticalError;
    }

    ErrorChecker() {
        this.errors = new ArrayList<>();
    }

    void setProgram(Program program) {
        this.program = program;
    }

    void setClassesSymbolTable(HashMap<String, SymbolTable> classesSymbolTable) {
        this.classesSymbolTable = classesSymbolTable;
    }

    void setClassesDeclaration(HashMap<String, ClassDeclaration> classesDeclaration) {
        this.classesDeclaration = classesDeclaration;
    }

    void addError(Error error) {
        this.errors.add(error);
    }

    void checkHasAnyClass() {
        if(program.getClasses().isEmpty()) {
            errors.add(new NoClassExist());
            hasCriticalError = true;
        }
    }

    void checkCircularInheritance() {
        if(hasCircularInheritance()) {
            errors.add(new CircularInheritance());
            hasCriticalError = true;
        }
    }

    void checkMainNotFound() {
    }

    boolean hasError() {
        return !errors.isEmpty();
    }

    ArrayList<Error> getErrors() {
        return errors;
    }

    private ClassDeclaration getParent(ClassDeclaration classDeclaration) {
        if(!classDeclaration.hasParent())
            return null;

        String parentName = classDeclaration.getParentName().getName();
        if(!classesDeclaration.containsKey(parentName))
            return null;

        return classesDeclaration.get(parentName);
    }

    private boolean hasCircularInheritance() {
        for(ClassDeclaration classDeclaration : this.classesDeclaration.values()) {
            ClassDeclaration ptr1 = classDeclaration;
            ClassDeclaration ptr2 = classDeclaration;

            while(ptr2 != null) {
                // Move one by one
                ptr1 = getParent(ptr1);

                // Move two by two
                ptr2 = getParent(ptr2);
                if(ptr2 != null)
                    ptr2 = getParent(ptr2);
                else
                    break;

                if(ptr1 == ptr2)
                    return true;
            }
        }
        return false;
    }
}
