package mythtitans.exprl.eval.impl;

import mythtitans.exprl.eval.Context;
import mythtitans.exprl.eval.Expression;
import mythtitans.exprl.parser.Parser;

import java.util.Locale;

public class DebugExpression implements Expression {

    private final Expression debuggedOperand;
    private final Expression debugLabelOperand;

    public DebugExpression(final Expression debuggedOperand, final Expression debugLabelOperand) {
        this.debuggedOperand = debuggedOperand;
        this.debugLabelOperand = debugLabelOperand;
    }

    public static Expression debug(final Expression operand, final Expression debugLabelOperand) {
        return new DebugExpression(operand, debugLabelOperand);
    }

    @Override
    public boolean evaluateAsBoolean(final Context context) throws EvaluationException {
        boolean result = debuggedOperand.evaluateAsBoolean(context);
        produceDebugMessage(result, context);
        return result;
    }

    @Override
    public long evaluateAsInteger(final Context context) throws EvaluationException {
        long result = debuggedOperand.evaluateAsInteger(context);
        produceDebugMessage(result, context);
        return result;
    }

    @Override
    public double evaluateAsDecimal(final Context context) throws EvaluationException {
        double result = debuggedOperand.evaluateAsDecimal(context);
        produceDebugMessage(String.format(Locale.US, "%.6f", result), context);
        return result;
    }

    @Override
    public String evaluateAsText(final Context context) throws EvaluationException {
        String result = debuggedOperand.evaluateAsText(context);
        produceDebugMessage(result, context);
        return result;
    }

    @Override
    public String getExpressionName() {
        return Parser.DEBUG_EXPRESSION;
    }

    private <T> void produceDebugMessage(final T result, final Context context) throws EvaluationException {
        String debugMessage = String.format("%s : %s", debugLabelOperand.evaluateAsText(context), result);
        context.getDebugMessageHandler().accept(debugMessage);
    }
}
