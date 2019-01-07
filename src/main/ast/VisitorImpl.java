package ast;

import ast.Type.PrimitiveType.StringType;
import ast.Type.Type;
import ast.node.Program;
import ast.node.declaration.ClassDeclaration;
import ast.node.declaration.MethodDeclaration;
import ast.node.declaration.VarDeclaration;
import ast.node.expression.*;
import ast.node.expression.Value.BooleanValue;
import ast.node.expression.Value.IntValue;
import ast.node.expression.Value.StringValue;
import ast.node.statement.*;
import errors.Error;
import errors.classError.ClassRedefinition;
import errors.classError.UndefinedClass;
import errors.expressionError.BadArraySize;
import errors.expressionError.BadConditionType;
import errors.expressionError.BadWritelnType;
import errors.methodError.BadReturnType;
import errors.methodError.MethodRedefinition;
import errors.statementError.NotAStatement;
import errors.variableError.VariableRedefinition;
import symbolTable.ItemAlreadyExistsException;
import symbolTable.SymbolTable;
import symbolTable.SymbolTableMethodItem;
import symbolTable.SymbolTableVariableItem;

import java.util.ArrayList;
import java.util.HashMap;

import static ast.ExpressionChecker.*;
import static ast.TypeChecker.*;

public class VisitorImpl implements Visitor {

    private enum Passes {
        FIND_CLASSES,
        FIND_METHODS,
        FILL_SYMBOL_TABLE,
    }

    private HashMap<String, SymbolTable> classesSymbolTable;
    private HashMap<String, ClassDeclaration> classesDeclaration;
    private ClassDeclaration mainClassDeclaration = null;
    private MethodDeclaration currentMethod = null;
    private Passes currentPass;


    private void addObjectClass(Program program) {
        Identifier objectName = new Identifier(-1, Util.MASTER_OBJECT_NAME);
        ClassDeclaration objectClass = new ClassDeclaration(-1, objectName);

        Identifier toStringName = new Identifier(-1, "toString");
        MethodDeclaration toStringMethod = new MethodDeclaration(-1, toStringName);
        toStringMethod.setReturnType(new StringType());

        Expression returnValue = new StringValue(-1, "\"Object\"");
        toStringMethod.setReturnValue(returnValue);

        objectClass.addMethodDeclaration(toStringMethod);
        objectClass.unSetParentName();
        program.addClass(objectClass);
    }

    private boolean isMainClass(ClassDeclaration classDeclaration) {
        if(mainClassDeclaration == null) return false;
        return mainClassDeclaration == classDeclaration;
    }

    private void setMainClassDeclaration(Program program) {
        if(program.hasAnyClass())
            mainClassDeclaration = program.getClasses().get(0);
    }

    @Override
    public void init(Program program) {
        do {
            setMainClassDeclaration(program);
            addObjectClass(program);

            currentPass = Passes.FIND_CLASSES;
            classesDeclaration = new HashMap<>();
            program.accept(this);
            if(ErrorChecker.hasCriticalError()) break;

            ErrorChecker.checkCircularInheritance(classesDeclaration);
            if(ErrorChecker.hasCriticalError()) break;

            currentPass = Passes.FIND_METHODS;
            classesSymbolTable = new HashMap<>();
            program.accept(this);
            if(ErrorChecker.hasCriticalError()) break;

            ErrorChecker.checkHasAnyClass(program);
            if(ErrorChecker.hasCriticalError()) break;

            ClassDeclaration mainClass = program.getClasses().get(0);
            ErrorChecker.checkMainClassErrors(mainClass);
            if(ErrorChecker.hasCriticalError()) break;

            this.currentPass = Passes.FILL_SYMBOL_TABLE;
            program.accept(this);
            if(ErrorChecker.hasCriticalError()) break;

        } while (false);

        if(ErrorChecker.hasError())
            for(Error error : ErrorChecker.getErrors())
                Util.error(error.toString());
        else
            program.compile();
    }

    public void visit(Program program) {
        for(ClassDeclaration classDeclaration : program.getClasses())
            classDeclaration.accept(this);
    }

    @Override
    public void visit(ClassDeclaration classDeclaration) {
        classDeclaration.setMainClass(mainClassDeclaration == classDeclaration);
        String className = classDeclaration.getName().getName();
        switch (currentPass) {
            case FIND_CLASSES:
                if (!classesDeclaration.containsKey(className)) {
                    classesDeclaration.put(className, classDeclaration);
                } else {
                    ErrorChecker.addError(new ClassRedefinition(classDeclaration));
                    String newName = classDeclaration.getName().getName() + "_" + Util.uniqueRandomString();
                    Identifier newId = new Identifier(classDeclaration.getName().getLine(), newName);
                    classDeclaration.setName(newId);
                    classDeclaration.accept(this);
                }
                return;
//                break;
            case FIND_METHODS:
                boolean hasSymbolTable = classesSymbolTable.containsKey(className);
                if (hasSymbolTable) return;

                // set parent symbol-table -DFS tor-
                SymbolTable parentSymbolTable = null;
                if (classDeclaration.hasParent()) {
                    String parentName = classDeclaration.getParentName().getName();
                    if (classesDeclaration.containsKey(parentName)) {
                        ClassDeclaration parent = classesDeclaration.get(parentName);
                        classDeclaration.setParentClass(parent); // use for jasmin manipulation
                        parent.accept(this);
                        parentSymbolTable = classesSymbolTable.get(parentName);
                    } else {
                        ErrorChecker.addError(new UndefinedClass(classDeclaration.getLine(), parentName));
                    }
                }

                SymbolTable symbolTable = new SymbolTable(parentSymbolTable, true);
                classesSymbolTable.put(className, symbolTable);
                SymbolTable.top = symbolTable;
                break;
            case FILL_SYMBOL_TABLE:

                SymbolTable.top = classesSymbolTable.get(className);
                break;
        }

        classDeclaration.getName().accept(this);
        if (classDeclaration.hasParent())
            classDeclaration.getParentName().accept(this);
        if (currentPass == Passes.FIND_METHODS) {
            for (VarDeclaration varDeclaration : classDeclaration.getVarDeclarations()) {
                varDeclaration.setContainerClass(classDeclaration); // use for jasmin manipulation
                varDeclaration.accept(this);
            }
        }
        for (MethodDeclaration methodDeclaration : classDeclaration.getMethodDeclarations()) {
            methodDeclaration.setInMainClass(isMainClass(classDeclaration));
            methodDeclaration.setContainerClass(classDeclaration); // use for jasmin manipulation
            methodDeclaration.accept(this);
        }

    }

    @Override
    public void visit(MethodDeclaration methodDeclaration) {
        this.currentMethod = methodDeclaration;
        boolean isMainMethod = methodDeclaration.isMainMethod();
        switch (currentPass) {
            case FIND_CLASSES:
                break;
            case FIND_METHODS:
                try {
                    String methodName = methodDeclaration.getName().getName();
                    ArrayList<Type> argsType = methodDeclaration.getArgsType();
                    Type returnType = methodDeclaration.getReturnType();

                    // set classDeclaration
                    if(isUserDefined(returnType)) {
                        String className = UD(returnType).getName().getName();
                        if(classesDeclaration.containsKey(className))
                            UD(returnType).setClassDeclaration(classesDeclaration.get(className));
                        else
                            ErrorChecker.addError(new UndefinedClass(methodDeclaration.getLine(), className));
                    }

                    SymbolTableMethodItem method = new SymbolTableMethodItem(methodName, argsType, returnType);
                    SymbolTable.top.put(method);
                } catch (ItemAlreadyExistsException e) {
                    ErrorChecker.addError(new MethodRedefinition(methodDeclaration));
                    String newName = methodDeclaration.getName().getName() + "_" + Util.uniqueRandomString();
                    Identifier newId = new Identifier(methodDeclaration.getName().getLine(), newName);
                    methodDeclaration.setName(newId);
                    methodDeclaration.accept(this);
                    return;
                }
                break;
            case FILL_SYMBOL_TABLE:
                SymbolTable.push(new SymbolTable(SymbolTable.top, false));
                break;
        }
        methodDeclaration.getName().accept(this);
        if(currentPass == Passes.FILL_SYMBOL_TABLE) {
            int jIndex = 1;
            for (VarDeclaration varDeclaration : methodDeclaration.getArgs())
                varDeclaration.accept(this);
            for (VarDeclaration varDeclaration : methodDeclaration.getLocalVars())
                varDeclaration.accept(this);
            for (Statement statement : methodDeclaration.getBody()) {
                statement.setInMainMethod(isMainMethod);
                statement.accept(this);
            }
            methodDeclaration.getReturnValue().accept(this);
        }
        if(currentPass == Passes.FILL_SYMBOL_TABLE) {
            // check return type
            Type returnType = methodDeclaration.getReturnType();
            Type returnValueType = getExpType(classesDeclaration, classesSymbolTable, methodDeclaration.getReturnValue());
            if(!isNoType(returnValueType))
                if(!canAssign(classesDeclaration, classesSymbolTable, returnType, returnValueType))
                    ErrorChecker.addError(new BadReturnType(returnType, methodDeclaration.getReturnValue()));

            SymbolTable.pop();
        }
    }

    @Override
    public void visit(VarDeclaration varDeclaration) {
        String varName = varDeclaration.getIdentifier().getName();
        Type varType = varDeclaration.getType();
        switch (currentPass) {
            case FIND_CLASSES:
                break;
            case FIND_METHODS:
            case FILL_SYMBOL_TABLE:
                try {
                    SymbolTableVariableItem variable = new SymbolTableVariableItem(varName, varType);
                    SymbolTable.top.put(variable);
                } catch (ItemAlreadyExistsException e) {
                    ErrorChecker.addError(new VariableRedefinition(varDeclaration));
                } finally {
                    if(isUserDefined(varType)) {
                        String className = UD(varType).getName().getName();
                        if(!classesDeclaration.containsKey(className))
                            ErrorChecker.addError(new UndefinedClass(varDeclaration.getLine(), className));
                    }
                }
                break;
        }

        varDeclaration.getIdentifier().accept(this);
    }

    @Override
    public void visit(ArrayCall arrayCall) {
        arrayCall.getInstance().accept(this);
        arrayCall.getIndex().accept(this);
    }

    @Override
    public void visit(BinaryExpression binaryExpression) {
        binaryExpression.getLeft().accept(this);
        binaryExpression.getRight().accept(this);
    }

    @Override
    public void visit(Identifier identifier) {
        identifier.setContainerMethod(currentMethod);
    }

    @Override
    public void visit(Length length) {
        length.getExpression().accept(this);
    }

    @Override
    public void visit(MethodCall methodCall) {
        methodCall.getInstance().accept(this);
        methodCall.getMethodName().accept(this);
        for(Expression expression : methodCall.getArgs())
            expression.accept(this);
    }

    @Override
    public void visit(NewArray newArray) {
        switch (currentPass) {
            case FIND_CLASSES:
                break;
            case FIND_METHODS:
                break;
            case FILL_SYMBOL_TABLE:
                Expression exp = newArray.getExpression();
                if(isIntValue(exp)) {
                    int value = ((IntValue)exp).getConstant();
                    if(value == 0)
                        ErrorChecker.addError(new BadArraySize(newArray));
                }
                break;
        }

        newArray.getExpression().accept(this);
    }

    @Override
    public void visit(NewClass newClass) {
        newClass.getClassName().accept(this);
    }

    @Override
    public void visit(This instance) {
    }

    @Override
    public void visit(UnaryExpression unaryExpression) {
        unaryExpression.getValue().accept(this);
    }

    @Override
    public void visit(BooleanValue value) {
    }

    @Override
    public void visit(IntValue value) {
    }

    @Override
    public void visit(StringValue value) {
    }

    @Override
    public void visit(Assign assign) {
        assign.getlValue().accept(this);
        assign.getrValue().accept(this);
    }

    @Override
    public void visit(Block block) {
        for(Statement statement : block.getBody())
            statement.accept(this);
    }

    @Override
    public void visit(Conditional conditional) {
        switch (currentPass) {
            case FIND_CLASSES:
                break;
            case FIND_METHODS:
                break;
            case FILL_SYMBOL_TABLE:
                Type conditionType = getExpType(classesDeclaration, classesSymbolTable, conditional.getExpression());
                if(!isBooleanOrNoType(conditionType))
                    ErrorChecker.addError(new BadConditionType(conditional.getExpression()));
                break;
        }

        conditional.getExpression().accept(this);
        conditional.getConsequenceBody().accept(this);
        if(conditional.hasAlternativeBody())
            conditional.getAlternativeBody().accept(this);
    }

    @Override
    public void visit(While loop) {
        switch (currentPass) {
            case FIND_CLASSES:
                break;
            case FIND_METHODS:
                break;
            case FILL_SYMBOL_TABLE:
                Type conditionType = getExpType(classesDeclaration, classesSymbolTable, loop.getCondition());
                if(!isBooleanOrNoType(conditionType))
                    ErrorChecker.addError(new BadConditionType(loop.getCondition()));
                break;
        }

        loop.getCondition().accept(this);
        loop.getBody().accept(this);
    }

    @Override
    public void visit(Write write) {
        switch (currentPass) {
            case FIND_CLASSES:
                break;
            case FIND_METHODS:
                break;
            case FILL_SYMBOL_TABLE:
                Type insideType = getExpType(classesDeclaration, classesSymbolTable, write.getArg());
                boolean validInsideType = isInt(insideType) || isString(insideType) || isArray(insideType) || isNoType(insideType);
                if(!validInsideType)
                    ErrorChecker.addError(new BadWritelnType(write));
                break;
        }

        write.getArg().accept(this);
    }

    @Override
    public void visit(SemiStatement semiStatement) {
        switch (currentPass) {
            case FIND_CLASSES:
                break;
            case FIND_METHODS:
                break;
            case FILL_SYMBOL_TABLE:
                if(!semiStatement.isEmpty()) {
                    Expression insideExp = semiStatement.getInside();

                    // type check inside exp
                    getExpType(classesDeclaration, classesSymbolTable, insideExp);

                    // convert to AssignStatement
                    if(isAssignExp(insideExp)) {
                        Assign assign = new Assign(semiStatement.getLine(), BE(insideExp).getLeft(), BE(insideExp).getRight());
                        assign.accept(this);
                        return;
                    }

                    // is in main class
                    if(semiStatement.isInMainMethod() && isMethodCall(insideExp))
                        return;
                }
                ErrorChecker.addError(new NotAStatement(semiStatement));
                break;
        }
    }
}