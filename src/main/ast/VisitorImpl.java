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
import exceptions.BadArraySizeException;
import exceptions.RedefinitionOfClassException;
import symbolTable.*;

import java.util.ArrayList;
import java.util.HashMap;

public class VisitorImpl implements Visitor {

    private enum Passes {
        PRE_PROCESS, ERROR_CHECK, PRE_ORDER_PRINT
    }

    private Boolean hasError;
    private Passes currentPass;

    @Override
    public void init(Program program) {

        this.hasError = false;
        this.currentPass = Passes.PRE_PROCESS;
        program.accept(this);

        //////////////////////////

        // Check errors
        this.currentPass = Passes.ERROR_CHECK;


        // Print pre-order
        if(!this.hasError) {
            this.currentPass = Passes.PRE_ORDER_PRINT;
            program.accept(this);
        }
    }

    @Override
    public void visit(Program program) {
        switch (currentPass) {
            case PRE_PROCESS:
                SymbolTable.top = new SymbolTable();
                break;
            case ERROR_CHECK:
                break;
            case PRE_ORDER_PRINT:
                System.out.println(program.toString());
                break;
        }

        for(ClassDeclaration classDeclaration : program.getClasses())
            classDeclaration.accept(this);
    }

    @Override
    public void visit(ClassDeclaration classDeclaration) {
        switch (currentPass) {
            case PRE_PROCESS:
                try {
                    String className = classDeclaration.getName().getName();

                    SymbolTableClassItem classItem = classDeclaration.hasParent() ?
                            new SymbolTableClassItem(className, classDeclaration.getParentName().getName()) :
                            new SymbolTableClassItem(className);

                    SymbolTable.top.put(classItem);
                    SymbolTable.push(new SymbolTable());
                } catch (ItemAlreadyExistsException e) {
                    System.out.println("EEEEEEER: redefination class");///////////////////////////////////////////////////////////////////////////////
                    this.hasError = true;
                }
                break;
            case ERROR_CHECK:
                break;
            case PRE_ORDER_PRINT:
                System.out.println(classDeclaration.toString());
                break;
        }

        classDeclaration.getName().accept(this);
        if(classDeclaration.hasParent())
            classDeclaration.getParentName().accept(this);
        for(VarDeclaration varDeclaration : classDeclaration.getVarDeclarations())
            varDeclaration.accept(this);
        for(MethodDeclaration methodDeclaration : classDeclaration.getMethodDeclarations())
            methodDeclaration.accept(this);

        SymbolTable.pop();
    }

    @Override
    public void visit(MethodDeclaration methodDeclaration) {
        switch (currentPass) {
            case PRE_PROCESS:
                try {
                    String methodName = methodDeclaration.getName().getName();
                    ArrayList<Type> argsType = methodDeclaration.getArgsType();
                    SymbolTableMethodItem method = new SymbolTableMethodItem(methodName, argsType);
                    SymbolTable.top.put(method);
                } catch (ItemAlreadyExistsException e) {
                    System.out.println("EEEEEEER: redefination method");///////////////////////////////////////////////////////////////////////////////
                    this.hasError = true;
                } finally {
                    SymbolTable.push(new SymbolTable(SymbolTable.top));
                }
                break;
            case ERROR_CHECK:
                break;
            case PRE_ORDER_PRINT:
                System.out.println(methodDeclaration.toString());
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

        SymbolTable.pop();
    }

    @Override
    public void visit(VarDeclaration varDeclaration) {
        switch (currentPass) {
            case PRE_PROCESS:
                try {
                    String varName = varDeclaration.getIdentifier().getName();
                    Type varType = varDeclaration.getType();
                    SymbolTableVariableItem variable = new SymbolTableVariableItem(varName, varType);
                    SymbolTable.top.put(variable);
                } catch (ItemAlreadyExistsException e) {
                    System.out.println("EEEEEEER: redefination variable");///////////////////////////////////////////////////////////////////////////////
                    this.hasError = true;
                }
                break;
            case ERROR_CHECK:
                break;
            case PRE_ORDER_PRINT:
                System.out.println(varDeclaration.toString());
                break;
        }

        varDeclaration.getIdentifier().accept(this);
    }

    @Override
    public void visit(ArrayCall arrayCall) {
        switch (currentPass) {
            case PRE_PROCESS:
                return;
//                break;
            case ERROR_CHECK:
                break;
            case PRE_ORDER_PRINT:
                System.out.println(arrayCall.toString());
                break;
        }

        arrayCall.getInstance().accept(this);
        arrayCall.getIndex().accept(this);
    }

    @Override
    public void visit(BinaryExpression binaryExpression) {
        switch (currentPass) {
            case PRE_PROCESS:
                return;
//                break;
            case ERROR_CHECK:
                break;
            case PRE_ORDER_PRINT:
                System.out.println(binaryExpression.toString());
                break;
        }

        binaryExpression.getLeft().accept(this);
        binaryExpression.getRight().accept(this);
    }

    @Override
    public void visit(Identifier identifier) {
        switch (currentPass) {
            case PRE_PROCESS:
                return;
//                break;
            case ERROR_CHECK:
                break;
            case PRE_ORDER_PRINT:
                System.out.println(identifier.toString());
                break;
        }
    }

    @Override
    public void visit(Length length) {
        switch (currentPass) {
            case PRE_PROCESS:
                return;
//                break;
            case ERROR_CHECK:
                break;
            case PRE_ORDER_PRINT:
                System.out.println(length.toString());
                break;
        }

        length.getExpression().accept(this);
    }

    @Override
    public void visit(MethodCall methodCall) {
        switch (currentPass) {
            case PRE_PROCESS:
                return;
//                break;
            case ERROR_CHECK:
                break;
            case PRE_ORDER_PRINT:
                System.out.println(methodCall.toString());
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
            case PRE_PROCESS:
                try {
                    Expression exp = newArray.getExpression();
                    boolean isNumberIndex = exp instanceof IntValue;
                    if(isNumberIndex) {
                        int value = ((IntValue)exp).getConstant();
                        if(value <= 0)
                            throw new BadArraySizeException();
                    }
                } catch (BadArraySizeException e) {
                    System.out.println("EEEEEEER: bad array size");///////////////////////////////////////////////////////////////////////////////
                    this.hasError = true;
                }
                break;
            case ERROR_CHECK:
                break;
            case PRE_ORDER_PRINT:
                System.out.println(newArray.toString());
                break;
        }

        newArray.getExpression().accept(this);
    }

    @Override
    public void visit(NewClass newClass) {
        switch (currentPass) {
            case PRE_PROCESS:
                return;
//                break;
            case ERROR_CHECK:
                break;
            case PRE_ORDER_PRINT:
                System.out.println(newClass.toString());
                break;
        }

        newClass.getClassName().accept(this);
    }

    @Override
    public void visit(This instance) {
        switch (currentPass) {
            case PRE_PROCESS:
                return;
//                break;
            case ERROR_CHECK:
                break;
            case PRE_ORDER_PRINT:
                System.out.println(instance.toString());
                break;
        }
    }

    @Override
    public void visit(UnaryExpression unaryExpression) {
        switch (currentPass) {
            case PRE_PROCESS:
                return;
//                break;
            case ERROR_CHECK:
                break;
            case PRE_ORDER_PRINT:
                System.out.println(unaryExpression.toString());
                break;
        }

        unaryExpression.getValue().accept(this);
    }

    @Override
    public void visit(BooleanValue value) {
        switch (currentPass) {
            case PRE_PROCESS:
                return;
//                break;
            case ERROR_CHECK:
                break;
            case PRE_ORDER_PRINT:
                System.out.println(value.toString());
                break;
        }
    }

    @Override
    public void visit(IntValue value) {
        switch (currentPass) {
            case PRE_PROCESS:
                return;
//                break;
            case ERROR_CHECK:
                break;
            case PRE_ORDER_PRINT:
                System.out.println(value.toString());
                break;
        }
    }

    @Override
    public void visit(StringValue value) {
        switch (currentPass) {
            case PRE_PROCESS:
                return;
//                break;
            case ERROR_CHECK:
                break;
            case PRE_ORDER_PRINT:
                System.out.println(value.toString());
                break;
        }
    }

    @Override
    public void visit(Assign assign) {
        switch (currentPass) {
            case PRE_PROCESS:
                return;
//                break;
            case ERROR_CHECK:
                break;
            case PRE_ORDER_PRINT:
                System.out.println(assign.toString());
                break;
        }

        assign.getlValue().accept(this);
        assign.getrValue().accept(this);
    }

    @Override
    public void visit(Block block) {
        switch (currentPass) {
            case PRE_PROCESS:
                return;
//                break;
            case ERROR_CHECK:
                break;
            case PRE_ORDER_PRINT:
                System.out.println(block.toString());
                break;
        }

        for(Statement statement : block.getBody())
            statement.accept(this);
    }

    @Override
    public void visit(Conditional conditional) {
        switch (currentPass) {
            case PRE_PROCESS:
                return;
//                break;
            case ERROR_CHECK:
                break;
            case PRE_ORDER_PRINT:
                System.out.println(conditional.toString());
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
            case PRE_PROCESS:
                return;
//                break;
            case ERROR_CHECK:
                break;
            case PRE_ORDER_PRINT:
                System.out.println(loop.toString());
                break;
        }

        loop.getCondition().accept(this);
        loop.getBody().accept(this);
    }

    @Override
    public void visit(Write write) {
        switch (currentPass) {
            case PRE_PROCESS:
                return;
//                break;
            case ERROR_CHECK:
                break;
            case PRE_ORDER_PRINT:
                System.out.println(write.toString());
                break;
        }

        write.getArg().accept(this);
    }

    @Override
    public void visit(SemiStatement semiStatement) {
        switch (currentPass) {
            case PRE_PROCESS:
                return;
//                break;
            case ERROR_CHECK:
                break;
            case PRE_ORDER_PRINT:
//                System.out.println(semiStatement.toString());
                break;
        }

        semiStatement.getInside().accept(this);
    }
}