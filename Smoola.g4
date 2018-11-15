grammar Smoola;

///////////////////////////////////////////// SmoolaParser.g4 //////////////////////////////////////////////
// parser grammar SmoolaParser;
// options { tokenVocab = SmoolaLexer; }

@members{
    void P(String str){
       System.out.print(str);
    }
    void PL(String str){
       System.out.println(str);
    }
    void OPL(String str){
       System.out.println("Operator:" + str);
    }
    void L() {
        System.out.println("");
    }
}

program:
    classBlock* EOF;

classBlock:
    // CLASS (id=IDENTIFIER) (EXTENDS (id=IDENTIFIER) LBRACE
    CLASS id=IDENTIFIER {P("ClassDec:" + $id.text);} (EXTENDS pid=IDENTIFIER {P("," + $pid.text);})? {L();} LBRACE
        variableDeclaration*
        methodDefinition*
    RBRACE;

variableDeclaration:
    VAR v=typedVariable SEMI
    {
        PL("VarDec:" + $v.varName + "," + $v.varType);
    };

methodDefinition:
    // DEF IDENTIFIER LPAREN (typedVariable (COMMA typedVariable)*)?  RPAREN COLON (type | IDENTIFIER) LBRACE
    DEF id=IDENTIFIER {P("MethodDec:" + $id.text);} LPAREN (v=typedVariable {P("," + $v.varName);} (COMMA v=typedVariable {P("," + $v.varName);})*)? RPAREN {L();} COLON type LBRACE
        variableDeclaration*
        statementBlock*
    RBRACE;

typedVariable returns [String varName, String varType]:
    vName=IDENTIFIER COLON vType=type
    {
        $varName=$vName.text;
        $varType=$vType.text;
    };

expression
    : IDENTIFIER
    | THIS
    | literal
    | methodCall
    | expression DOT methodCall
    | expression DOT IDENTIFIER
    | NEW IDENTIFIER arguments
    | NEW arrayType LBRACK expression RBRACK
    | LPAREN expression RPAREN
    | expression LBRACK expression RBRACK
    | uop=uneriOperators expression
    | expression bop=(STAR | SLASH) expression
    | expression bop=(PLUS | MINUS) expression
    | expression bop=(GT | LT) expression
    | expression bop=(EQUAL | NOTEQUAL) expression
    | expression bop=AND expression
    | expression bop=OR expression
    | expression bop=ASSIGN expression
    ;

parExpression:
    LPAREN expression RPAREN;

statementBlock
    :
    LBRACE
        variableDeclaration*
        statementBlock*
    RBRACE
    | statement
    ;

statement
    : IF {PL("Conditional:if");} parExpression THEN statementBlock (ELSE {PL("Conditional:else");}  statementBlock)?
    | WHILE {PL("Loop:While");} parExpression statementBlock
    | expression SEMI
    | RETURN expression SEMI
    | SEMI
    ;

methodCall
    : IDENTIFIER arguments
    | THIS arguments
    ;

expressionList:
    expression (COMMA expression)*;

arguments:
    LPAREN expressionList? RPAREN;

literal:
    DECIMAL_LITERAL | STRING_LITERAL | booleanLiteral;

booleanLiteral:
    TRUE | FALSE;

primitiveType:
    INT | STRING | BOOLEAN | arrayType LBRACK RBRACK;

type:
   primitiveType | IDENTIFIER;

arrayType:
    INT;

relationalOperators:
    EQUAL | NOTEQUAL | GT | LT;

arithmeticOperators:
    PLUS | MINUS | STAR | SLASH;

logicalOperators:
    AND | OR;

uneriOperators:
    BANG | MINUS | PLUS;

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