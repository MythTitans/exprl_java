package mythtitans.exprl.eval.impl;

import mythtitans.exprl.eval.Context;
import mythtitans.exprl.eval.Expression;
import mythtitans.exprl.parser.Parser;

import static mythtitans.exprl.eval.impl.EqExpression.eq;
import static mythtitans.exprl.eval.impl.LtExpression.lt;
import static mythtitans.exprl.eval.impl.OrExpression.or;

public class LteExpression implements Expression {

    protected final Expression operandA;
    protected final Expression operandB;

    public LteExpression(final Expression operandA, final Expression operandB) {
        this.operandA = operandA;
        this.operandB = operandB;
    }

    public static Expression lte(final Expression operandA, final Expression operandB) {
        return new LteExpression(operandA, operandB);
    }

    @Override
    public boolean evaluateAsBoolean(final Context context) throws EvaluationException {
        try {
            return or(lt(operandA, operandB), eq(operandA, operandB)).evaluateAsBoolean(context);
        } catch (EvaluationException e) {
            throw new EvaluationException(String.format("Expression [%s] failed with all combination of known types.", getExpressionName()));
        }
    }

    @Override
    public String getExpressionName() {
        return Parser.LTE_EXPRESSION;
    }
}