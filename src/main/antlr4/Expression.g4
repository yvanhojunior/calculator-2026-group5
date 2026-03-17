grammar Expression;
@header {
package calculator;
}
expression : additionExpr ;

additionExpr : multiplicationExpr (('+' | '-') multiplicationExpr)* ;

multiplicationExpr : atom (('*' | '/') atom)* ;

atom : INT
     | '(' expression ')' ;

INT : [0-9]+;
WS  : [ \t\r\n]+ -> skip ;
