package calculator;

import calculator.ExpressionBaseVisitor;

public class ExpressionAstBuilder extends  ExpressionBaseVisitor<Expression>{

    /**
     * Convertit un noeud 'atom' de l'arbre de parse en un objet Expression.
     * Si c'esst un entier, retourne un MyNumber. Sinon, visite l'expression imbriqué.
     * @param ctx the parse tree
     * @return
     */
    @Override
    public Expression visitAtom(calculator.ExpressionParser.AtomContext ctx) {
        if (ctx.INT() != null) {
            return new MyNumber(Integer.parseInt(ctx.INT().getText()));
        }
        return visit(ctx.expression());
    }


    /**
     * Visit one expression of multiplication/division (e.g: "2 * 3 / 4").
     * Build one AST tree chaining operations left to right.
     * Manage the operators '*' et '/' by creating objects Times or Divides.
     * @param ctx
     * @return null in case of construction error.
     */
    @Override
    public Expression visitMultiplicationExpr(calculator.ExpressionParser.MultiplicationExprContext ctx) {
        Expression result = visit(ctx.atom(0));

        for (int i = 1; i < ctx.atom().size(); i++) {
            Expression right = visit(ctx.atom(i));
            java.util.List<Expression> params = new java.util.ArrayList<>();
            params.add(result);
            params.add(right);

            try {
                String operator = ctx.getChild(2 * i - 1).getText();
                if (operator.equals("*")) {
                    result = new Times(params);
                } else {
                    result = new Divides(params);
                }
            } catch (IllegalConstruction e) {
                return null;
            }
        }

        return result;
    }


    @Override
    public Expression visitAdditionExpr(calculator.ExpressionParser.AdditionExprContext ctx) {
        Expression result = visit(ctx.multiplicationExpr(0));

        for (int i = 1; i < ctx.multiplicationExpr().size(); i++) {
            Expression right = visit(ctx.multiplicationExpr(i));
            java.util.List<Expression> params = new java.util.ArrayList<>();
            params.add(result);
            params.add(right);

            try {
                String operator = ctx.getChild(2 * i - 1).getText();
                if (operator.equals("+")) {
                    result = new Plus(params);
                } else {
                    result = new Minus(params);
                }
            } catch (IllegalConstruction e) {
                return null;
            }
        }

        return result;
    }

    /**
     * Visitor entry point. Delegates to additionExpr which is the actual root.
     * @param ctx the parse tree
     * @return result of visiting additionExpr
     */
    @Override
    public Expression visitExpression(calculator.ExpressionParser.ExpressionContext ctx) {
        return visit(ctx.additionExpr());
    }
}
