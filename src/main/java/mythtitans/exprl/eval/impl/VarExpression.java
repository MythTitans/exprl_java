package mythtitans.exprl.eval.impl;

import mythtitans.exprl.eval.Context;
import mythtitans.exprl.eval.Expression;
import mythtitans.exprl.parser.Parser;

public class VarExpression implements Expression {

    private final Expression operand;

    public VarExpression(final Expression operand) {
        this.operand = operand;
    }

    public static VarExpression var(final Expression operand) {
        return new VarExpression(operand);
    }

    private static Expression.EvaluationException variableEvaluationError(final String variableName) {
        return new Expression.EvaluationException(String.format("Cannot find variable [%s].", variableName));
    }

    @Override
    public boolean evaluateAsBoolean(final Context context) throws EvaluationException {
        return accessVariable(context).asBoolean();
    }

    @Override
    public long evaluateAsInteger(final Context context) throws EvaluationException {
        return accessVariable(context).asInteger();
    }

    @Override
    public double evaluateAsDecimal(final Context context) throws EvaluationException {
        return accessVariable(context).asDecimal();
    }

    @Override
    public String evaluateAsText(final Context context) throws EvaluationException {
        return accessVariable(context).asText();
    }

    @Override
    public String getExpressionName() {
        return Parser.VAR_EXPRESSION;
    }

    private Context.Variable<?> accessVariable(final Context context) throws EvaluationException {
        String variableName = operand.evaluateAsText(context);
        return context.getVariable(operand.evaluateAsText(context)).orElseThrow(() -> variableEvaluationError(variableName));
    }
}
