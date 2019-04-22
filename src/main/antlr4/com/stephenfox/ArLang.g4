grammar ArLang;

expr: add|sub|div|mul;

add :   NUMBER ADD NUMBER;
sub:    NUMBER SUB NUMBER;
div:    NUMBER DIV NUMBER;
mul:    NUMBER MUL NUMBER;

// Lexer rules
NUMBER: ('0'..'9')+;
ADD: '+';
SUB: '-';
DIV: '/';
MUL: '*';