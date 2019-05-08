grammar ArLang;


arlang
    : expr EOF
    ;

expr
    : '-' expr        #UnaryExpr
    | '(' expr ')'    #SubExpr
    | expr '*' expr   #Mul
    | expr '/' expr   #Div
    | expr '+' expr   #Add
    | expr '-' expr   #Sub
    | FLOAT           #Float
    | INT             #Int
    ;

FLOAT
    : DIGIT+ '.' DIGIT*
    | '.' DIGIT+
    ;

INT
    : DIGIT+
    ;

MUL: '*';
ADD: '+';
DIV: '/';
SUB: '-';

// fragment signifies this lexer rule can only be used by other
// lexer rules
fragment
DIGIT: [0-9];


IGNORE : [ \t\r\n]+ -> skip;

