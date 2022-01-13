package mythtitans.exprl.eval.impl;

import mythtitans.exprl.eval.Context;
import mythtitans.exprl.eval.Expression;
import mythtitans.exprl.parser.Parser;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MaxExpression implements Expression {

    private final List<Expression> operands;

    public MaxExpression(final List<Expression> operands) {
        this.operands = operands;
    }

    public static Expression max(final Expression operandA, final Expression operandB, final Expression... operands) {
        return new MaxExpression(Stream.concat(Stream.of(operandA, operandB), Arrays.stream(operands)).collect(Collectors.toList()));
    }

    @Override
    public long evaluateAsInteger(final Context context) throws EvaluationException {
        if (operands.size() == 2) {
            return Math.max(operands.get(0).evaluateAsInteger(context), operands.get(1).evaluateAsInteger(context));
        }

        long max = operands.get(0).evaluateAsInteger(context);
        for (Expression expression : operands) {
            max = Math.max(max, expression.evaluateAsInteger(context));
        }

        return max;
    }

    @Override
    public double evaluateAsDecimal(final Context context) throws EvaluationException {
        if (operands.size() == 2) {
            return Math.max(operands.get(0).evaluateAsDecimal(context), operands.get(1).evaluateAsDecimal(context));
        }

        double max = operands.get(0).evaluateAsDecimal(context);
        for (Expression expression : operands) {
            max = Math.max(max, expression.evaluateAsDecimal(context));
        }

        return max;
    }

    @Override
    public String getExpressionName() {
        return Parser.MAX_EXPRESSION;
    }

    @Override
    public Set<String> getLiteralVariables() {
        return ExpressionHelper.getLiteralVariables(operands);
    }
}
