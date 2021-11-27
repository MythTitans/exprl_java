package mythtitans.exprl.eval.impl;

import mythtitans.exprl.eval.Context;
import mythtitans.exprl.eval.Expression;
import mythtitans.exprl.parser.Parser;

public class CondExpression implements Expression {

    private final Expression operandCond;
    private final Expression operandThen;
    private final Expression operandElse;

    public CondExpression(final Expression operandCond, final Expression operandThen, final Expression operandElse) {
        this.operandCond = operandCond;
        this.operandThen = operandThen;
        this.operandElse = operandElse;
    }

    public static Expression cond(final Expression operandCond, final Expression operandThen, final Expression operandElse) {
        return new CondExpression(operandCond, operandThen, operandElse);
    }

    @Override
    public boolean evaluateAsBoolean(final Context context) throws EvaluationException {
        return evaluateCondition(context).evaluateAsBoolean(context);
    }

    @Override
    public long evaluateAsInteger(final Context context) throws EvaluationException {
        return evaluateCondition(context).evaluateAsInteger(context);
    }

    @Override
    public double evaluateAsDecimal(final Context context) throws EvaluationException {
        return evaluateCondition(context).evaluateAsDecimal(context);
    }

    @Override
    public String evaluateAsText(final Context context) throws EvaluationException {
        return evaluateCondition(context).evaluateAsText(context);
    }

    @Override
    public String getExpressionName() {
        return Parser.COND_EXPRESSION;
    }

    private Expression evaluateCondition(final Context context) throws EvaluationException {
        if (operandCond.evaluateAsBoolean(context)) {
            return operandThen;
        } else {
            return operandElse;
        }
    }
}
