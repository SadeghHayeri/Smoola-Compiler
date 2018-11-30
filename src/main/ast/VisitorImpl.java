package ast;

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
import errors.*;
import errors.Error;
import symbolTable.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class VisitorImpl implements Visitor {

    private enum Passes {
        FIND_CLASSES,
        FILL_SYMBOL_TABLE,
        PRE_ORDER_PRINT
    }

    private ArrayList<Error> errors = new ArrayList<>();
    private HashMap<String, SymbolTable> classesSymbolTable;
    private HashMap<String, ClassDeclaration> classesDeclaration;
    private Passes currentPass;

    @Override
    public void init(Program program) {

        currentPass = Passes.FIND_CLASSES;
        classesDeclaration = new HashMap<>();
        program.accept(this);

        if(!program.hasAnyClass())
            errors.add(new NoClassExist());

        this.currentPass = Passes.FILL_SYMBOL_TABLE;
        classesSymbolTable = new HashMap<>();
        program.accept(this);

        if(!errors.isEmpty()) {
            errors.sort(Comparator.comparingInt(Error::getLine));
            for(Error error : errors)
                Util.error(error.toString());
        } else {
            this.currentPass = Passes.PRE_ORDER_PRINT;
            program.accept(this);
        }
    }

    @Override
    public void visit(Program program) {
        switch (currentPass) {
            case FIND_CLASSES:
                break;
            case FILL_SYMBOL_TABLE:
                break;
            case PRE_ORDER_PRINT:
                Util.info(program.toString());
                break;
        }

        for(ClassDeclaration classDeclaration : program.getClasses())
            classDeclaration.accept(this);
    }

    @Override
    public void visit(ClassDeclaration classDeclaration) {
        String className = classDeclaration.getName().getName();
        switch (currentPass) {
            case FIND_CLASSES:
                if(!classesDeclaration.containsKey(className)) {
                    classesDeclaration.put(className, classDeclaration);
                } else {
                    errors.add(new ClassRedefinition(classDeclaration));
                    String newName = classDeclaration.getName().getName() + "_" + Util.uniqueRandomString();
                    Identifier newId = new Identifier(classDeclaration.getName().getLine(), newName);
                    classDeclaration.setName(newId);
                    classDeclaration.accept(this);
                }
                return;
//                break;
            case FILL_SYMBOL_TABLE:
                boolean hasSymbolTable = classesSymbolTable.containsKey(className);
                if(hasSymbolTable) return;

                SymbolTable parentSymbolTable = null;
                if(classDeclaration.hasParent()) {
                    String parentName = classDeclaration.getParentName().getName();
                    if(classesDeclaration.containsKey(parentName)) {
                        ClassDeclaration parent = classesDeclaration.get(parentName);
                        parent.accept(this);
                        parentSymbolTable = classesSymbolTable.get(parentName);
                    } else {
//                        TODO: uncomment for next phase
//                        errors.add(new UndefinedClass(classDeclaration.getLine(), parentName));
                    }
                }

                SymbolTable symbolTable = new SymbolTable(parentSymbolTable, true);
                classesSymbolTable.put(className, symbolTable);
                SymbolTable.top = symbolTable;
                break;
            case PRE_ORDER_PRINT:
                Util.info(classDeclaration.toString());
                break;
        }

        classDeclaration.getName().accept(this);
        if(classDeclaration.hasParent())
            classDeclaration.getParentName().accept(this);
        for(VarDeclaration varDeclaration : classDeclaration.getVarDeclarations())
            varDeclaration.accept(this);
        for(MethodDeclaration methodDeclaration : classDeclaration.getMethodDeclarations())
            methodDeclaration.accept(this);
    }

    @Override
    public void visit(MethodDeclaration methodDeclaration) {
        switch (currentPass) {
            case FIND_CLASSES:
                break;
            case FILL_SYMBOL_TABLE:
                try {
                    String methodName = methodDeclaration.getName().getName();
                    ArrayList<Type> argsType = methodDeclaration.getArgsType();
                    SymbolTableMethodItem method = new SymbolTableMethodItem(methodName, argsType);
                    SymbolTable.top.put(method);
                    SymbolTable.push(new SymbolTable(SymbolTable.top, false));
                } catch (ItemAlreadyExistsException e) {
                    errors.add(new MethodRedefinition(methodDeclaration));
                    String newName = methodDeclaration.getName().getName() + "_" + Util.uniqueRandomString();
                    Identifier newId = new Identifier(methodDeclaration.getName().getLine(), newName);
                    methodDeclaration.setName(newId);
                    methodDeclaration.accept(this);
                    return;
                }
                break;
            case PRE_ORDER_PRINT:
                Util.info(methodDeclaration.toString());
                break;
        }

        methodDeclaration.getName().accept(this);
        for(VarDeclaration varDeclaration : methodDeclaration.getArgs())
            varDeclaration.accept(this);
        for(VarDeclaration varDeclaration : methodDeclaration.getLocalVars())
            varDeclaration.accept(this);
        for(Statement statement : methodDeclaration.getBody())
            statement.accept(this);
        methodDeclaration.getReturnValue().accept(this);

        if(currentPass == Passes.FILL_SYMBOL_TABLE) {
            SymbolTable.pop();
        }
    }

    @Override
    public void visit(VarDeclaration varDeclaration) {
        switch (currentPass) {
            case FIND_CLASSES:
                break;
            case FILL_SYMBOL_TABLE:
                try {
                    String varName = varDeclaration.getIdentifier().getName();
                    Type varType = varDeclaration.getType();
                    SymbolTableVariableItem variable = new SymbolTableVariableItem(varName, varType);
                    SymbolTable.top.put(variable);
                } catch (ItemAlreadyExistsException e) {
                    errors.add(new VariableRedefinition(varDeclaration));
                }
                break;
            case PRE_ORDER_PRINT:
                Util.info(varDeclaration.toString());
                break;
        }

        varDeclaration.getIdentifier().accept(this);
    }

    @Override
    public void visit(ArrayCall arrayCall) {
        switch (currentPass) {
            case FIND_CLASSES:
                break;
            case FILL_SYMBOL_TABLE:
                break;
            case PRE_ORDER_PRINT:
                Util.info(arrayCall.toString());
                break;
        }

        arrayCall.getInstance().accept(this);
        arrayCall.getIndex().accept(this);
    }

    @Override
    public void visit(BinaryExpression binaryExpression) {
        switch (currentPass) {
            case FIND_CLASSES:
                break;
            case FILL_SYMBOL_TABLE:
                break;
            case PRE_ORDER_PRINT:
                Util.info(binaryExpression.toString());
                break;
        }

        binaryExpression.getLeft().accept(this);
        binaryExpression.getRight().accept(this);
    }

    @Override
    public void visit(Identifier identifier) {
        switch (currentPass) {
            case FIND_CLASSES:
                break;
            case FILL_SYMBOL_TABLE:
                break;
            case PRE_ORDER_PRINT:
                Util.info(identifier.toString());
                break;
        }
    }

    @Override
    public void visit(Length length) {
        switch (currentPass) {
            case FIND_CLASSES:
                break;
            case FILL_SYMBOL_TABLE:
                break;
            case PRE_ORDER_PRINT:
                Util.info(length.toString());
                break;
        }

        length.getExpression().accept(this);
    }

    @Override
    public void visit(MethodCall methodCall) {
        switch (currentPass) {
            case FIND_CLASSES:
                break;
            case FILL_SYMBOL_TABLE:
                break;
            case PRE_ORDER_PRINT:
                Util.info(methodCall.toString());
                break;
        }

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
            case FILL_SYMBOL_TABLE:
                Expression exp = newArray.getExpression();
                if(exp instanceof IntValue) {
                    int value = ((IntValue)exp).getConstant();
                    if(value == 0)
                        errors.add(new BadArraySize(newArray));
                }

                //////////////////// TODO: remove in phase 4 (pre-process) //////////////////////
                boolean isUnary = exp instanceof UnaryExpression;
                if(isUnary) {
                    UnaryExpression unaryExp = (UnaryExpression)exp;
                    if(unaryExp.getUnaryOperator() == UnaryOperator.minus) {
                        Expression innerExp = unaryExp.getValue();
                        if(innerExp instanceof IntValue) {
                            int value = ((IntValue)innerExp).getConstant();
                            if(value >= 0)
                                errors.add(new BadArraySize(newArray));
                        }
                    }
                }
                /////////////////////////////////////////////////////////////////////////////////
                break;
            case PRE_ORDER_PRINT:
                Util.info(newArray.toString());
                break;
        }

        newArray.getExpression().accept(this);
    }

    @Override
    public void visit(NewClass newClass) {
        switch (currentPass) {
            case FIND_CLASSES:
                break;
            case FILL_SYMBOL_TABLE:
                break;
            case PRE_ORDER_PRINT:
                Util.info(newClass.toString());
                break;
        }

        newClass.getClassName().accept(this);
    }

    @Override
    public void visit(This instance) {
        switch (currentPass) {
            case FIND_CLASSES:
                break;
            case FILL_SYMBOL_TABLE:
                break;
            case PRE_ORDER_PRINT:
                Util.info(instance.toString());
                break;
        }
    }

    @Override
    public void visit(UnaryExpression unaryExpression) {
        switch (currentPass) {
            case FIND_CLASSES:
                break;
            case FILL_SYMBOL_TABLE:
                break;
            case PRE_ORDER_PRINT:
                Util.info(unaryExpression.toString());
                break;
        }

        unaryExpression.getValue().accept(this);
    }

    @Override
    public void visit(BooleanValue value) {
        switch (currentPass) {
            case FIND_CLASSES:
                break;
            case FILL_SYMBOL_TABLE:
                break;
            case PRE_ORDER_PRINT:
                Util.info(value.toString());
                break;
        }
    }

    @Override
    public void visit(IntValue value) {
        switch (currentPass) {
            case FIND_CLASSES:
                break;
            case FILL_SYMBOL_TABLE:
                break;
            case PRE_ORDER_PRINT:
                Util.info(value.toString());
                break;
        }
    }

    @Override
    public void visit(StringValue value) {
        switch (currentPass) {
            case FIND_CLASSES:
                break;
            case FILL_SYMBOL_TABLE:
                break;
            case PRE_ORDER_PRINT:
                Util.info(value.toString());
                break;
        }
    }

    @Override
    public void visit(Assign assign) {
        switch (currentPass) {
            case FIND_CLASSES:
                break;
            case FILL_SYMBOL_TABLE:
                break;
            case PRE_ORDER_PRINT:
                Util.info(assign.toString());
                break;
        }

        assign.getlValue().accept(this);
        assign.getrValue().accept(this);
    }

    @Override
    public void visit(Block block) {
        switch (currentPass) {
            case FIND_CLASSES:
                break;
            case FILL_SYMBOL_TABLE:
                break;
            case PRE_ORDER_PRINT:
                Util.info(block.toString());
                break;
        }

        for(Statement statement : block.getBody())
            statement.accept(this);
    }

    @Override
    public void visit(Conditional conditional) {
        switch (currentPass) {
            case FIND_CLASSES:
                break;
            case FILL_SYMBOL_TABLE:
                break;
            case PRE_ORDER_PRINT:
                Util.info(conditional.toString());
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
            case FILL_SYMBOL_TABLE:
                break;
            case PRE_ORDER_PRINT:
                Util.info(loop.toString());
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
            case FILL_SYMBOL_TABLE:
                break;
            case PRE_ORDER_PRINT:
                Util.info(write.toString());
                break;
        }

        write.getArg().accept(this);
    }

    @Override
    public void visit(SemiStatement semiStatement) {
        switch (currentPass) {
            case FIND_CLASSES:
                break;
            case FILL_SYMBOL_TABLE:
                break;
            case PRE_ORDER_PRINT:
//                Util.info();(semiStatement.toString());
                break;
        }

        if(!semiStatement.isEmpty()) {
            Expression expression = semiStatement.getInside();
            if (expression instanceof BinaryExpression) {
                BinaryExpression binaryExpression = (BinaryExpression) semiStatement.getInside();
                if (binaryExpression.getBinaryOperator() == BinaryOperator.assign) {
                    Assign assign = new Assign(semiStatement.getLine(), binaryExpression.getLeft(), binaryExpression.getRight());
                    assign.accept(this);
                    return;
                }
            }
        }
//        TODO: uncomment for next phase
//        errors.add(new BadStatement(semiStatement));
    }
}