grammar ArLang;

program
    : expression
    | (statement*);

statement
    : forLoop
    | assign
    | print;

forLoop
    : FOR expression LBRACKET (statement*) RBRACKET;

print
    : PRINT (NUMBER | STRING | IDENTIFIER);

expression
    : binaryOperator;

assign:
    IDENTIFIER COLON type EQ (STRING| NUMBER);

type:
    IDENTIFIER;

binaryOperator
    : (NUMBER | IDENTIFIER) LT  (NUMBER | IDENTIFIER)
    | (NUMBER | IDENTIFIER) LTE (NUMBER | IDENTIFIER)
    | (NUMBER | IDENTIFIER) GTE ( NUMBER | IDENTIFIER)
    | (NUMBER | IDENTIFIER) GT  (NUMBER | IDENTIFIER)
    | (NUMBER | IDENTIFIER) SUB ( NUMBER | IDENTIFIER)
    | (NUMBER | IDENTIFIER) DIV ( NUMBER | IDENTIFIER)
    | (NUMBER | IDENTIFIER) MUL ( NUMBER | IDENTIFIER)
    | (NUMBER | IDENTIFIER) ADD ( NUMBER | IDENTIFIER)
    | (NUMBER | IDENTIFIER) EQQ ( NUMBER | IDENTIFIER);

// Lexer rules

PRINT: 'print';
FOR: 'for';
VAL: 'val';
COLON: ':';
NUMBER: [0-9]+;

IDENTIFIER: ([a-zA-Z0-9]+);
LBRACKET : '{';
RBRACKET: '}';
QUOTE: '"';

EQQ: '==';
EQ: '=';
ADD: '+';
SUB: '-';
DIV: '/';
MUL: '*';
LT: '<';
GT: '>';
LTE: '<=';
GTE: '>=';

STRING : QUOTE (ESC | ~["\\])* QUOTE ;
fragment ESC : '\\' (["\\/bfnrt] | UNICODE) ;
fragment UNICODE : 'u' HEX HEX HEX HEX ;
fragment HEX : [0-9a-fA-F] ;

WS : (' ' | '\t' | '\n')+ -> channel(HIDDEN);