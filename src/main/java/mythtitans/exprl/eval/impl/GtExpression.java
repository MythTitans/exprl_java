package mythtitans.exprl.eval.impl;

import mythtitans.exprl.eval.Context;
import mythtitans.exprl.eval.Expression;
import mythtitans.exprl.parser.Parser;

import java.util.Set;

import static mythtitans.exprl.eval.impl.LteExpression.lte;
import static mythtitans.exprl.eval.impl.NotExpression.not;

public class GtExpression implements Expression {

    protected final Expression operandA;
    protected final Expression operandB;

    public GtExpression(final Expression operandA, final Expression operandB) {
        this.operandA = operandA;
        this.operandB = operandB;
    }

    public static Expression gt(final Expression operandA, final Expression operandB) {
        return new GtExpression(operandA, operandB);
    }

    @Override
    public boolean evaluateAsBoolean(final Context context) throws EvaluationException {
        try {
            return not(lte(operandA, operandB)).evaluateAsBoolean(context);
        } catch (EvaluationException e) {
            throw new EvaluationException(String.format("Expression [%s] failed with all combination of known types.", getExpressionName()));
        }
    }

    @Override
    public String getExpressionName() {
        return Parser.GT_EXPRESSION;
    }

    @Override
    public Set<String> getLiteralVariables() {
        return ExpressionHelper.getLiteralVariables(operandA, operandB);
    }
}