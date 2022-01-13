package mythtitans.exprl.eval.impl;

import mythtitans.exprl.eval.Context;
import mythtitans.exprl.eval.Expression;
import mythtitans.exprl.parser.Parser;

import java.util.Set;

public class ModExpression implements Expression {

    private final Expression operandA;
    private final Expression operandB;

    public ModExpression(final Expression operandA, final Expression operandB) {
        this.operandA = operandA;
        this.operandB = operandB;
    }

    public static Expression mod(final Expression operandA, final Expression operandB) {
        return new ModExpression(operandA, operandB);
    }

    @Override
    public long evaluateAsInteger(final Context context) throws EvaluationException {
        return operandA.evaluateAsInteger(context) % operandB.evaluateAsInteger(context);
    }

    @Override
    public double evaluateAsDecimal(final Context context) throws EvaluationException {
        return evaluateAsInteger(context);
    }

    @Override
    public String getExpressionName() {
        return Parser.MOD_EXPRESSION;
    }

    @Override
    public Set<String> getLiteralVariables() {
        return ExpressionHelper.getLiteralVariables(operandA, operandB);
    }
}
