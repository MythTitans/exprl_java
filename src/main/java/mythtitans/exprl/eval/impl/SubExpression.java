package mythtitans.exprl.eval.impl;

import mythtitans.exprl.eval.Context;
import mythtitans.exprl.eval.Expression;
import mythtitans.exprl.parser.Parser;

public class SubExpression implements Expression {

    private final Expression operandA;
    private final Expression operandB;

    public SubExpression(final Expression operandA, final Expression operandB) {
        this.operandA = operandA;
        this.operandB = operandB;
    }

    public static Expression sub(final Expression operandA, final Expression operandB) {
        return new SubExpression(operandA, operandB);
    }

    @Override
    public long evaluateAsInteger(final Context context) throws EvaluationException {
        return operandA.evaluateAsInteger(context) - operandB.evaluateAsInteger(context);
    }

    @Override
    public double evaluateAsDecimal(final Context context) throws EvaluationException {
        return operandA.evaluateAsDecimal(context) - operandB.evaluateAsDecimal(context);
    }

    @Override
    public String getExpressionName() {
        return Parser.SUB_EXPRESSION;
    }
}
