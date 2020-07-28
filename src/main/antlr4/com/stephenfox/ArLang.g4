grammar ArLang;

program:
        expression
    |   statement;

statement:
        forLoop
    |   print;

forLoop:
        FOR expression LBRACKET statement RBRACKET;

print:
        PRINT NUMBER;

expression:
    binaryOperator;

binaryOperator:
        NUMBER LT NUMBER
    |   NUMBER LTE NUMBER
    |   NUMBER GTE NUMBER
    |   NUMBER GT NUMBER
    |   NUMBER SUB NUMBER
    |   NUMBER DIV NUMBER
    |   NUMBER MUL NUMBER
    |   NUMBER ADD NUMBER;

// Lexer rules
PRINT: 'print';
FOR: 'for';
VAL: 'val';
COLON: ':';
IDENTIFIER: [a-z_]+;
NUMBER: [0-9]+;
EQ: '=';
ADD: '+';
SUB: '-';
DIV: '/';
MUL: '*';
LT: '<';
GT: '>';
LTE: '<=';
GTE: '>=';
LBRACKET : '{';
RBRACKET: '}';

WS : (' ' | '\t' | '\n')+ -> channel(HIDDEN);