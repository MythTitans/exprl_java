package mythtitans.exprl.eval.impl;

import mythtitans.exprl.eval.Context;
import mythtitans.exprl.eval.Expression;
import mythtitans.exprl.parser.Parser;

import java.util.Set;

public class LiteralVarExpression implements Expression {

    private final String operand;

    public LiteralVarExpression(final String operand) {
        this.operand = operand;
    }

    public static LiteralVarExpression var(final String operand) {
        return new LiteralVarExpression(operand);
    }

    private static EvaluationException variableEvaluationError(final String variableName) {
        return new EvaluationException(String.format("Cannot find variable [%s].", variableName));
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

    @Override
    public Set<String> getLiteralVariables() {
        return Set.of(operand);
    }

    private Context.Variable<?> accessVariable(final Context context) throws EvaluationException {
        String variableName = operand;
        return context.getVariable(variableName).orElseThrow(() -> variableEvaluationError(variableName));
    }
}
