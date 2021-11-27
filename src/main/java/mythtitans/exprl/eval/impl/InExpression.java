package mythtitans.exprl.eval.impl;

import mythtitans.exprl.eval.Context;
import mythtitans.exprl.eval.Expression;
import mythtitans.exprl.parser.Parser;

public class InExpression implements Expression {

    private final Expression operandA;
    private final Expression operandB;

    public InExpression(final Expression operandA, final Expression operandB) {
        this.operandA = operandA;
        this.operandB = operandB;
    }

    public static Expression in(final Expression operandA, final Expression operandB) {
        return new InExpression(operandA, operandB);
    }

    @Override
    public boolean evaluateAsBoolean(final Context context) throws EvaluationException {
        return operandA.evaluateAsText(context).contains(operandB.evaluateAsText(context));
    }

    @Override
    public String getExpressionName() {
        return Parser.IN_EXPRESSION;
    }
}
