package mythtitans.exprl.eval.impl;

import mythtitans.exprl.eval.Context;
import mythtitans.exprl.eval.Expression;
import mythtitans.exprl.parser.Parser;

public class StartsExpression implements Expression {

    private final Expression operandA;
    private final Expression operandB;

    public StartsExpression(final Expression operandA, final Expression operandB) {
        this.operandA = operandA;
        this.operandB = operandB;
    }

    public static Expression starts(final Expression operandA, final Expression operandB) {
        return new StartsExpression(operandA, operandB);
    }

    @Override
    public boolean evaluateAsBoolean(final Context context) throws EvaluationException {
        return operandA.evaluateAsText(context).startsWith(operandB.evaluateAsText(context));
    }

    @Override
    public String getExpressionName() {
        return Parser.STARTS_EXPRESSION;
    }
}
