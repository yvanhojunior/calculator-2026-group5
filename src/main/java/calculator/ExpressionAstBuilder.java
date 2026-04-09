package calculator;

import calculator.ExpressionBaseVisitor;

public class ExpressionAstBuilder extends ExpressionBaseVisitor<Expression> {

    /**
     * Convertit un noeud 'atom' de l'arbre de parse en un objet Expression.
     * Supporte maintenant: INT, REAL, RATIONAL, COMPLEX
     * @param ctx the parse tree
     * @return Expression correspondante
     */
    @Override
    public Expression visitAtom(calculator.ExpressionParser.AtomContext ctx) {
        if (ctx.INT() != null) {
            return new MyNumber(new IntegerValue(Integer.parseInt(ctx.INT().getText())));
        }
        if (ctx.REAL() != null) {
            return new MyNumber(new RealValue(Double.parseDouble(ctx.REAL().getText())));
        }
        if (ctx.RATIONAL() != null) {
            String[] parts = ctx.RATIONAL().getText().split("/");
            return new MyNumber(new RationalValue(Long.parseLong(parts[0]), Long.parseLong(parts[1])));
        }
        if (ctx.COMPLEX() != null) {
            return parseComplex(ctx.COMPLEX().getText());
        }
        if (ctx.atom() != null) {
            Expression inner = visit(ctx.atom());
            try {
                return new Minus(java.util.Arrays.asList(new MyNumber(new IntegerValue(0)), inner));
            } catch (IllegalConstruction e) {
                return null;
            }
        }
        return visit(ctx.expression());
    }

    @Override
    public Expression visitExponentiationExpr(calculator.ExpressionParser.ExponentiationExprContext ctx) {
        Expression result = visit(ctx.atom(0));
        for (int i = 1; i < ctx.atom().size(); i++) {
            Expression right = visit(ctx.atom(i));
            java.util.List<Expression> params = new java.util.ArrayList<>();
            params.add(result);
            params.add(right);
            try {
                result = new Power(params);
            } catch (IllegalConstruction e) {
                return null;
            }
        }
        return result;
    }

    /**
     * Parse une chaîne complexe comme "2+3i", "3i", "2.0+3.0i"
     */
    private MyNumber parseComplex(String text) {
        if (!text.contains("+") && !text.contains("-")) {
            String coefficient = text.substring(0, text.length() - 1);
            double imaginary = switch (coefficient) {
                case "", "+" -> 1.0;
                case "-" -> -1.0;
                default -> Double.parseDouble(coefficient);
            };
            return new MyNumber(new ComplexValue(0, imaginary));
        }
        int splitIndex = text.lastIndexOf('+');
        if (splitIndex <= 0) splitIndex = text.lastIndexOf('-');
        double real = Double.parseDouble(text.substring(0, splitIndex));
        double imaginary = Double.parseDouble(text.substring(splitIndex, text.length() - 1));
        return new MyNumber(new ComplexValue(real, imaginary));
    }

    /**
     * Visit one expression of multiplication/division (e.g: "2 * 3 / 4").
     * Build one AST tree chaining operations left to right.
     * Manage the operators '*' et '/' by creating objects Times or Divides.
     * @param ctx the parse tree
     * @return null in case of construction error.
     */
    @Override
    public Expression visitMultiplicationExpr(calculator.ExpressionParser.MultiplicationExprContext ctx) {
        Expression result = visit(ctx.exponentiationExpr(0));
        for (int i = 1; i < ctx.exponentiationExpr().size(); i++) {
            Expression right = visit(ctx.exponentiationExpr(i));
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