package mythtitans.exprl.eval.impl;

import mythtitans.exprl.eval.Context;
import mythtitans.exprl.eval.Expression;
import mythtitans.exprl.parser.Parser;

public class EqExpression implements Expression {

    private static final double EPSILON = 0.000001;

    protected final Expression operandA;
    protected final Expression operandB;

    public EqExpression(final Expression operandA, final Expression operandB) {
        this.operandA = operandA;
        this.operandB = operandB;
    }

    public static Expression eq(final Expression operandA, final Expression operandB) {
        return new EqExpression(operandA, operandB);
    }

    @Override
    public boolean evaluateAsBoolean(final Context context) throws EvaluationException {
        try {
            return operandA.evaluateAsBoolean(context) == operandB.evaluateAsBoolean(context);
        } catch (EvaluationException e) {
            // Ignored
        }

        try {
            return operandA.evaluateAsInteger(context) == operandB.evaluateAsInteger(context);
        } catch (EvaluationException e) {
            // Ignored
        }

        try {
            return Math.abs(operandA.evaluateAsDecimal(context) - operandB.evaluateAsDecimal(context)) < EPSILON;
        } catch (EvaluationException e) {
            // Ignored
        }

        try {
            return operandA.evaluateAsText(context).equals(operandB.evaluateAsText(context));
        } catch (EvaluationException e) {
            // Ignored
        }

        throw new EvaluationException(String.format("Expression [%s] failed with all combination of known types.", getExpressionName()));
    }

    @Override
    public String getExpressionName() {
        return Parser.EQ_EXPRESSION;
    }
}