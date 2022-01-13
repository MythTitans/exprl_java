package mythtitans.exprl.eval.impl;

import mythtitans.exprl.eval.Context;
import mythtitans.exprl.eval.Expression;
import mythtitans.exprl.parser.Parser;

import java.util.Set;

public class EndsExpression implements Expression {

    private final Expression operandA;
    private final Expression operandB;

    public EndsExpression(final Expression operandA, final Expression operandB) {
        this.operandA = operandA;
        this.operandB = operandB;
    }

    public static Expression ends(final Expression operandA, final Expression operandB) {
        return new EndsExpression(operandA, operandB);
    }

    @Override
    public boolean evaluateAsBoolean(final Context context) throws EvaluationException {
        return operandA.evaluateAsText(context).endsWith(operandB.evaluateAsText(context));
    }

    @Override
    public String getExpressionName() {
        return Parser.ENDS_EXPRESSION;
    }

    @Override
    public Set<String> getLiteralVariables() {
        return ExpressionHelper.getLiteralVariables(operandA, operandB);
    }
}
