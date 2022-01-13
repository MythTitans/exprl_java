package mythtitans.exprl.eval.impl;

import mythtitans.exprl.eval.Context;
import mythtitans.exprl.eval.Expression;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static mythtitans.exprl.parser.Parser.CONCAT_EXPRESSION;

public class ConcatExpression implements Expression {

    private final List<Expression> operands;

    public ConcatExpression(final List<Expression> operands) {
        this.operands = operands;
    }

    public static Expression concat(final Expression operandA, final Expression operandB, final Expression... operands) {
        return new ConcatExpression(Stream.concat(Stream.of(operandA, operandB), Arrays.stream(operands)).collect(Collectors.toList()));
    }

    @Override
    public String evaluateAsText(final Context context) throws EvaluationException {
        if (operands.size() == 2) {
            return operands.get(0).evaluateAsText(context) + operands.get(1).evaluateAsText(context);
        }

        StringBuilder sb = new StringBuilder();
        for (Expression expression : operands) {
            sb.append(expression.evaluateAsText(context));
        }

        return sb.toString();
    }

    @Override
    public String getExpressionName() {
        return CONCAT_EXPRESSION;
    }

    @Override
    public Set<String> getLiteralVariables() {
        return ExpressionHelper.getLiteralVariables(operands);
    }
}
