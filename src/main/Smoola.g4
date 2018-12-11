grammar Smoola;
@header {
    import ast.node.expression.Value.*;
    import ast.node.expression.*;
    import ast.node.declaration.*;
    import ast.node.statement.*;
    import ast.node.*;
    import ast.Type.ArrayType.*;
    import ast.Type.PrimitiveType.*;
    import ast.Type.UserDefinedType.*;
    import ast.Type.*;
    import ast.*;
}

///////////////////////////////////////////// SmoolaParser.g4 //////////////////////////////////////////////
// parser grammar SmoolaParser;
// options { tokenVocab = SmoolaLexer; }

@members {
    void _addArgToMethod(int line, Identifier id, Type type, MethodDeclaration method) {
        VarDeclaration varDeclaration = new VarDeclaration(line, id, type);
        method.addArg(varDeclaration);
    }

    Identifier _ID(int line, String id) {
        Identifier identifier = new Identifier(line, id);
        return identifier;
    }
}

program
    : { Program p = new Program(0); }
    (classBlock { p.addClass($classBlock.classDeclaration); })*
    EOF
    {
        Visitor visitor = new VisitorImpl();
        visitor.init(p);
    }
    ;

classBlock returns [ClassDeclaration classDeclaration]:
    CLASS IDENTIFIER { $classDeclaration = new ClassDeclaration($CLASS.line, _ID($IDENTIFIER.line, $IDENTIFIER.text)); }
    (EXTENDS IDENTIFIER { $classDeclaration.setParentName(_ID($IDENTIFIER.line, $IDENTIFIER.text)); })? LBRACE
        (vd=variableDeclaration
            { $classDeclaration.addVarDeclaration($vd.varDeclaration); }
        )*
        (md=methodDefinition
            { $classDeclaration.addMethodDeclaration($md.methodDeclaration); }
        )*
    RBRACE;

variableDeclaration returns [VarDeclaration varDeclaration]:
    VAR typedVariable SEMI
    { $varDeclaration = new VarDeclaration($VAR.line, $typedVariable.varIdentifier, $typedVariable.varType); }
    ;

methodDefinition returns [MethodDeclaration methodDeclaration]
    :
    DEF IDENTIFIER { $methodDeclaration = new MethodDeclaration($DEF.line, _ID($IDENTIFIER.line, $IDENTIFIER.text)); }
    LPAREN (typedVariable { _addArgToMethod($LPAREN.line, $typedVariable.varIdentifier, $typedVariable.varType, $methodDeclaration); }
    ( COMMA typedVariable { _addArgToMethod($COMMA.line, $typedVariable.varIdentifier, $typedVariable.varType, $methodDeclaration); })* )?
    RPAREN COLON type LBRACE { $methodDeclaration.setReturnType($type.varType); }
        ( variableDeclaration { $methodDeclaration.addLocalVar($variableDeclaration.varDeclaration); } )*
        ( statementBlock { $methodDeclaration.addStatement($statementBlock.stmt); } )*
        RETURN expression SEMI { $methodDeclaration.setReturnValue($expression.exp); }
    RBRACE
    ;

typedVariable returns [Identifier varIdentifier, Type varType]
    : IDENTIFIER COLON type
    {
        $varIdentifier = _ID($IDENTIFIER.line, $IDENTIFIER.text);
        $varType = $type.varType;
    }
    ;

expression returns [Expression exp]
    : IDENTIFIER                                                        { $exp = _ID($IDENTIFIER.line, $IDENTIFIER.text);  }
    | THIS                                                              { $exp = new This($THIS.line); }
    | literal                                                           { $exp = $literal.value; }
    | e=expression DOT LENGTH                                           { $exp = new Length($DOT.line, $e.exp); }
    | e=expression DOT id=IDENTIFIER { MethodCall mc = new MethodCall($DOT.line, $e.exp, _ID($DOT.line, $id.text)); } arguments[mc] { $exp = mc; }
    | NEW IDENTIFIER LPAREN RPAREN                                      { $exp = new NewClass($NEW.line, _ID($IDENTIFIER.line, $IDENTIFIER.text)); }
    | NEW INT LBRACK expression RBRACK                                  { $exp = new NewArray($NEW.line, $expression.exp); }
    | LPAREN expression RPAREN                                          { $exp = $expression.exp; }
    | e1=expression LBRACK e2=expression RBRACK                         { $exp = new ArrayCall($LBRACK.line, $e1.exp, $e2.exp); }
    | uop=(BANG | MINUS) expression                                     { $exp = new UnaryExpression($uop.line, $uop.text.equals("!") ? UnaryOperator.not : UnaryOperator.minus, $expression.exp); }
    | <assoc=right> e1=expression bop=(STAR | SLASH) e2=expression      { $exp = new BinaryExpression($bop.line, $e1.exp, $e2.exp, $bop.text.equals("*") ? BinaryOperator.mult : BinaryOperator.div); }
    | <assoc=right> e1=expression bop=(PLUS | MINUS) e2=expression      { $exp = new BinaryExpression($bop.line, $e1.exp, $e2.exp, $bop.text.equals("+") ? BinaryOperator.add : BinaryOperator.sub); }
    | <assoc=right> e1=expression bop=(GT | LT) e2=expression           { $exp = new BinaryExpression($bop.line, $e1.exp, $e2.exp, $bop.text.equals(">") ? BinaryOperator.gt : BinaryOperator.lt); }
    | <assoc=right> e1=expression bop=(EQUAL | NOTEQUAL) e2=expression  { $exp = new BinaryExpression($bop.line, $e1.exp, $e2.exp, $bop.text.equals("==") ? BinaryOperator.eq : BinaryOperator.neq); }
    | <assoc=right> e1=expression bop=AND e2=expression                 { $exp = new BinaryExpression($bop.line, $e1.exp, $e2.exp, BinaryOperator.and); }
    | <assoc=right> e1=expression bop=OR e2=expression                  { $exp = new BinaryExpression($bop.line, $e1.exp, $e2.exp, BinaryOperator.or); }
    | <assoc=right> e1=expression bop=ASSIGN e2=expression              { $exp = new BinaryExpression($bop.line, $e1.exp, $e2.exp, BinaryOperator.assign); }
    ;

arguments [MethodCall mc]:
    LPAREN (expression { $mc.addArg($expression.exp); } (COMMA expression { $mc.addArg($expression.exp); })*)? RPAREN;

statementBlock returns [Statement stmt]
    :
    LBRACE
        { Block block = new Block($LBRACE.line); }
        (statementBlock
            { block.addStatement($statementBlock.stmt); }
        )*
        { $stmt = block; }
    RBRACE
    | statement { $stmt = $statement.stmt; }
    ;

statement returns [Statement stmt]
    : IF pe=parExpression THEN s1=statementBlock { Conditional con = new Conditional($IF.line, $pe.exp, $s1.stmt); } (ELSE s2=statementBlock { con.setAlternativeBody($s2.stmt); })? { $stmt = con; }
    | WHILE pe=parExpression s=statementBlock { $stmt = new While($WHILE.line, $pe.exp, $s.stmt); }
    | WRITELN LPAREN expression RPAREN SEMI{ $stmt = new Write($WRITELN.line, $expression.exp); }
    | expression SEMI { $stmt = new SemiStatement($SEMI.line, $expression.exp); }
    | SEMI { $stmt = new SemiStatement($SEMI.line); }
    ;

parExpression returns [Expression exp]:
    LPAREN expression RPAREN { $exp = $expression.exp; };

literal returns [Value value]
    : DECIMAL_LITERAL { $value = new IntValue($DECIMAL_LITERAL.line, $DECIMAL_LITERAL.int); }
    | STRING_LITERAL { $value = new StringValue($STRING_LITERAL.line, $STRING_LITERAL.text); }
    | booleanLiteral { $value = new BooleanValue($booleanLiteral.line, $booleanLiteral.value); }
    ;

booleanLiteral returns [Boolean value, int line]
    : TRUE { $value = true; $line = $TRUE.line; }
    | FALSE { $value = false; $line = $FALSE.line; }
    ;

type returns [Type varType]
    : primitiveType { $varType = $primitiveType.varType; }
    | userDefineType { $varType = $userDefineType.varType; }
    ;

primitiveType returns [Type varType]
    : INT { $varType = new IntType(); }
    | STRING { $varType = new StringType(); }
    | BOOLEAN { $varType = new BooleanType(); }
    | INT LBRACK RBRACK { $varType = new ArrayType(); }
    ;

userDefineType returns [Type varType]
    : IDENTIFIER { $varType = new UserDefinedType(_ID($IDENTIFIER.line, $IDENTIFIER.text)); }
    ;

///////////////////////////////////////////// SmoolaLexer.g4 //////////////////////////////////////////////
// lexer grammar SmoolaLexer;

// Reserved Words
DEF:                'def';
VAR:                'var';
IF:                 'if';
THEN:               'then';
ELSE:               'else';
EXTENDS:            'extends';
THIS:               'this';
WHILE:              'while';
RETURN:             'return';
NEW:                'new';
CLASS:              'class';
BOOLEAN:            'boolean';
STRING:             'string';
INT:                'int';
FALSE:              'false';
TRUE:               'true';
LENGTH:             'length';
WRITELN:            'writeln';

// Separators
LPAREN:             '(';
RPAREN:             ')';
LBRACE:             '{';
RBRACE:             '}';
LBRACK:             '[';
RBRACK:             ']';
SEMI:               ';';
COLON:              ':';
COMMA:              ',';
DOT:                '.';

// Operators
ASSIGN:             '=';
BANG:               '!';
EQUAL:              '==';
NOTEQUAL:           '<>';
GT:                 '>';
LT:                 '<';
AND:                '&&';
OR:                 '||';
PLUS:                '+';
MINUS:                '-';
STAR:                '*';
SLASH:                '/';

// Literals
DECIMAL_LITERAL:    Digits;
STRING_LITERAL:     '"' (~["\r\n])* '"';
IDENTIFIER:         Letter LetterOrDigit*;

// Whitespace and comments
WS:                 [ \t\r\n]+ -> channel(HIDDEN);
LINE_COMMENT:       '#' ~[\r\n]*    -> channel(HIDDEN);

// Fragment rules
fragment Digit:
    [0-9];

fragment Digits:
    Digit+;

fragment LetterOrDigit:
    Letter | Digit;

fragment Letter:
    [a-zA-Z_];