grammar Expression;

expression : additionExpr ;

additionExpr : multiplicationExpr (('+' | '-') multiplicationExpr)* ;

multiplicationExpr : atom (('*' | '/') atom)* ;

atom : COMPLEX
     | REAL
     | RATIONAL
     | INT
     | '(' expression ')' ;

// Nombres complexes: 2+3i, 3i, -1.5i
COMPLEX : (INT | REAL) [+-] (INT | REAL) 'i'
        | (INT | REAL) 'i' ;

// Nombres réels: 3.14, 0.5, .5
REAL    : [0-9]* '.' [0-9]+ ;

// Nombres rationnels: 1/3, 2/5
RATIONAL : [0-9]+ '/' [0-9]+ ;

// Entiers: 42, 0, 123
INT     : [0-9]+ ;

WS      : [ \t\r\n]+ -> skip ;