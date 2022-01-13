package mythtitans.exprl.eval.impl;

import mythtitans.exprl.eval.Context;
import mythtitans.exprl.eval.Expression;
import mythtitans.exprl.parser.Parser;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AndExpression implements Expression {

    private final List<Expression> operands;

    public AndExpression(final List<Expression> operands) {
        this.operands = operands;
    }

    public static Expression and(final Expression operandA, final Expression operandB, final Expression... operands) {
        return new AndExpression(Stream.concat(Stream.of(operandA, operandB), Arrays.stream(operands)).collect(Collectors.toList()));
    }

    @Override
    public boolean evaluateAsBoolean(final Context context) throws EvaluationException {
        if (operands.size() == 2) {
            return operands.get(0).evaluateAsBoolean(context) && operands.get(1).evaluateAsBoolean(context);
        }

        for (Expression expression : operands) {
            if (!expression.evaluateAsBoolean(context)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String getExpressionName() {
        return Parser.AND_EXPRESSION;
    }

    @Override
    public Set<String> getLiteralVariables() {
        return ExpressionHelper.getLiteralVariables(operands);
    }
}
