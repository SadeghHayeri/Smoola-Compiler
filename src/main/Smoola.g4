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
    void _addArgToMethod(Identifier id, Type type, MethodDeclaration method) {
        VarDeclaration varDeclaration = new VarDeclaration(id, type);
        method.addArg(varDeclaration);
    }

    Identifier _ID(String id) {
        return new Identifier(id);
    }
}

program returns [Program p]
    : { $p = new Program(); }
    (classBlock
    {
        $p.addClass($classBlock.classDeclaration);
        if( $classBlock.isMainClass )
            $p.setMainClass($classBlock.classDeclaration);
    }
    )*
    EOF
    ;

classBlock returns [ClassDeclaration classDeclaration, Boolean isMainClass]:
    { $isMainClass = false; }
    CLASS IDENTIFIER { $classDeclaration = new ClassDeclaration(_ID($IDENTIFIER.text)); }
    (EXTENDS IDENTIFIER { $classDeclaration.setParentName(_ID($IDENTIFIER.text)); })? LBRACE
        (vd=variableDeclaration
            { $classDeclaration.addVarDeclaration($vd.varDeclaration); }
        )*
        (md=methodDefinition
            { $classDeclaration.addMethodDeclaration($md.methodDeclaration); }
            { $isMainClass = $isMainClass || $md.isMainMethod; }
        )*
    RBRACE;

variableDeclaration returns [VarDeclaration varDeclaration]:
    VAR typedVariable SEMI
    {
        $varDeclaration = new VarDeclaration(
            $typedVariable.varIdentifier,
            $typedVariable.varType
        );
    }
    ;

methodDefinition returns [MethodDeclaration methodDeclaration, Boolean isMainMethod]
    :
    DEF IDENTIFIER
    {
        $isMainMethod = ($IDENTIFIER.text == "main");
        $methodDeclaration = $isMainMethod ?
            new MainMethodDeclaration() :
            new MethodDeclaration(_ID($IDENTIFIER.text));
    }
    LPAREN (typedVariable { _addArgToMethod($typedVariable.varIdentifier, $typedVariable.varType, $methodDeclaration); }
    ( COMMA typedVariable { _addArgToMethod($typedVariable.varIdentifier, $typedVariable.varType, $methodDeclaration); })* )?
    RPAREN COLON type LBRACE { $methodDeclaration.setReturnType($type.varType); }
        ( variableDeclaration { $methodDeclaration.addLocalVar($variableDeclaration.varDeclaration); } )*
        ( statementBlock { $methodDeclaration.addStatement($statementBlock.stmt); } )*
        RETURN expression SEMI { $methodDeclaration.setReturnValue($expression.exp); }
    RBRACE
    ;

typedVariable returns [Identifier varIdentifier, Type varType]
    : IDENTIFIER COLON type
    {
        $varIdentifier = _ID($IDENTIFIER.text);
        $varType = $type.varType;
    }
    ;

expression returns [Expression exp]
    : IDENTIFIER                                            { $exp = _ID($IDENTIFIER.text); }
    | THIS                                                  { $exp = new This(); }
    | literal                                               { $exp = $literal.value; }
    | e=expression DOT id=IDENTIFIER { MethodCall mc = new MethodCall($e.exp, _ID($id.text)); } arguments[mc] { $exp = mc; }
    | expression DOT LENGTH                                 { $exp = new Length($expression.exp); }
    | NEW IDENTIFIER LPAREN RPAREN                          { $exp = new NewClass(_ID($IDENTIFIER.text)); }
    | NEW INT LBRACK expression RBRACK                      { $exp = new NewArray($expression.exp); }
    | LPAREN expression RPAREN                              { $exp = $expression.exp; }
    | e1=expression LBRACK e2=expression RBRACK             { $exp = new ArrayCall($e1.exp, $e2.exp); }
    | uop=(BANG | MINUS) expression                         { $exp = new UnaryExpression(($uop.text == "!") ? UnaryOperator.not : UnaryOperator.minus, $expression.exp); }
    | e1=expression bop=(STAR | SLASH) e2=expression        { $exp = new BinaryExpression($e1.exp, $e2.exp, ($bop.text == "*") ? BinaryOperator.mult : BinaryOperator.div); }
    | e1=expression bop=(PLUS | MINUS) e2=expression        { $exp = new BinaryExpression($e1.exp, $e2.exp, ($bop.text == "+") ? BinaryOperator.add : BinaryOperator.sub); }
    | e1=expression bop=(GT | LT) e2=expression             { $exp = new BinaryExpression($e1.exp, $e2.exp, ($bop.text == ">") ? BinaryOperator.gt : BinaryOperator.lt); }
    | e1=expression bop=(EQUAL | NOTEQUAL) e2=expression    { $exp = new BinaryExpression($e1.exp, $e2.exp, ($bop.text == "==") ? BinaryOperator.eq : BinaryOperator.neq); }
    | e1=expression bop=AND e2=expression                   { $exp = new BinaryExpression($e1.exp, $e2.exp, BinaryOperator.and); }
    | e1=expression bop=OR e2=expression                    { $exp = new BinaryExpression($e1.exp, $e2.exp, BinaryOperator.or); }
    | e1=expression bop=ASSIGN e2=expression                { $exp = new BinaryExpression($e1.exp, $e2.exp, BinaryOperator.assign); }
    ;

arguments [MethodCall mc]:
    LPAREN (expression { $mc.addArg($expression.exp); } (COMMA expression { $mc.addArg($expression.exp); })*)? RPAREN;

statementBlock returns [Statement stmt]
    :
    LBRACE
        { Block block = new Block(); }
        (statementBlock
            { block.addStatement($statementBlock.stmt); }
        )*
        { $stmt = block; }
    RBRACE
    | statement { $stmt = $statement.stmt; }
    ;

statement returns [Statement stmt]
    : IF e=parExpression THEN s1=statementBlock { Conditional con = new Conditional($e.exp, $s1.stmt); } (ELSE s2=statementBlock { con.setAlternativeBody($s2.stmt); })? { $stmt = con; }
    | WHILE e=parExpression s=statementBlock { $stmt = new While($e.exp, $s.stmt); }
    | expression SEMI  { $stmt = new Statement(); }
    | SEMI { $stmt = new Statement(); }
    ;

parExpression returns [Expression exp]:
    LPAREN expression RPAREN { $exp = $expression.exp; };

literal returns [Value value]
    : DECIMAL_LITERAL { $value = new IntValue($DECIMAL_LITERAL.int); }
    | STRING_LITERAL { $value = new StringValue($STRING_LITERAL.text); }
    | booleanLiteral { $value = new BooleanValue($booleanLiteral.value); }
    ;

booleanLiteral returns [Boolean value]
    : TRUE { $value = true; }
    | FALSE { $value = false; }
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
    : IDENTIFIER { $varType = new UserDefinedType(_ID($IDENTIFIER.text)); }
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