package mythtitans.exprl.eval.impl;

import mythtitans.exprl.eval.Context;
import mythtitans.exprl.eval.Expression;
import mythtitans.exprl.parser.Parser;

public class LtExpression implements Expression {

    protected final Expression operandA;
    protected final Expression operandB;

    public LtExpression(final Expression operandA, final Expression operandB) {
        this.operandA = operandA;
        this.operandB = operandB;
    }

    public static Expression lt(final Expression operandA, final Expression operandB) {
        return new LtExpression(operandA, operandB);
    }

    @Override
    public boolean evaluateAsBoolean(final Context context) throws EvaluationException {
        try {
            return operandA.evaluateAsInteger(context) < operandB.evaluateAsInteger(context);
        } catch (EvaluationException e) {
            // Ignored
        }

        try {
            return operandA.evaluateAsDecimal(context) < operandB.evaluateAsDecimal(context);
        } catch (EvaluationException e) {
            // Ignored
        }

        throw new EvaluationException(String.format("Expression [%s] failed with all combination of known types.", getExpressionName()));
    }

    @Override
    public String getExpressionName() {
        return Parser.LT_EXPRESSION;
    }
}