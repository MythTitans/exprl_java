package mythtitans.exprl.eval.impl;

import mythtitans.exprl.eval.Context;
import mythtitans.exprl.eval.Expression;
import mythtitans.exprl.parser.Parser;

import java.util.Set;

public class NotExpression implements Expression {

    private final Expression operand;

    public NotExpression(final Expression operand) {
        this.operand = operand;
    }

    public static Expression not(final Expression operand) {
        return new NotExpression(operand);
    }

    @Override
    public boolean evaluateAsBoolean(final Context context) throws EvaluationException {
        return !operand.evaluateAsBoolean(context);
    }

    @Override
    public String getExpressionName() {
        return Parser.NOT_EXPRESSION;
    }

    @Override
    public Set<String> getLiteralVariables() {
        return ExpressionHelper.getLiteralVariables(operand);
    }
}
