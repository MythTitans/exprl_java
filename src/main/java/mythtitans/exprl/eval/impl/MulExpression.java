package mythtitans.exprl.eval.impl;

import mythtitans.exprl.eval.Context;
import mythtitans.exprl.eval.Expression;
import mythtitans.exprl.parser.Parser;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MulExpression implements Expression {

    private final List<Expression> operands;

    public MulExpression(final List<Expression> operands) {
        this.operands = operands;
    }

    public static Expression mul(final Expression operandA, final Expression operandB, final Expression... operands) {
        return new MulExpression(Stream.concat(Stream.of(operandA, operandB), Arrays.stream(operands)).collect(Collectors.toList()));
    }

    @Override
    public long evaluateAsInteger(final Context context) throws EvaluationException {
        if (operands.size() == 2) {
            return operands.get(0).evaluateAsInteger(context) * operands.get(1).evaluateAsInteger(context);
        }

        long product = 1;
        for (Expression expression : operands) {
            product *= expression.evaluateAsInteger(context);
        }

        return product;
    }

    @Override
    public double evaluateAsDecimal(final Context context) throws EvaluationException {
        if (operands.size() == 2) {
            return operands.get(0).evaluateAsDecimal(context) * operands.get(1).evaluateAsDecimal(context);
        }

        double product = 1.0;
        for (Expression expression : operands) {
            product *= expression.evaluateAsDecimal(context);
        }

        return product;
    }

    @Override
    public String getExpressionName() {
        return Parser.MUL_EXPRESSION;
    }
}
