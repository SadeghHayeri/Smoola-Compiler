grammar Smoola;

///////////////////////////////////////////// SmoolaParser.g4 //////////////////////////////////////////////
// parser grammar SmoolaParser;
// options { tokenVocab = SmoolaLexer; }

@members{
}

program:
    classBlock* EOF;

classBlock:
    CLASS IDENTIFIER (EXTENDS IDENTIFIER)? LBRACE
        variableDeclaration*
        methodDefinition*
    RBRACE;

variableDeclaration:
    VAR typedVariable SEMI;

methodDefinition:
    DEF IDENTIFIER LPAREN (typedVariable (COMMA typedVariable)*)? RPAREN COLON type LBRACE
        variableDeclaration*
        statementBlock*
    RBRACE;

typedVariable:
    IDENTIFIER COLON type;

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
    : IF parExpression THEN statementBlock (ELSE statementBlock)?
    | WHILE parExpression statementBlock
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