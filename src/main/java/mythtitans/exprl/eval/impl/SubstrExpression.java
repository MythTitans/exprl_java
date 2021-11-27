package mythtitans.exprl.eval.impl;

import mythtitans.exprl.eval.Context;
import mythtitans.exprl.eval.Expression;
import mythtitans.exprl.parser.Parser;

public class SubstrExpression implements Expression {

    private final Expression operandStr;
    private final Expression operandBegin;
    private final Expression operandEnd;

    public SubstrExpression(final Expression operandStr, final Expression operandBegin, final Expression operandEnd) {
        this.operandStr = operandStr;
        this.operandBegin = operandBegin;
        this.operandEnd = operandEnd;
    }

    public static Expression substr(final Expression operandStr, final Expression operandBegin, final Expression operandEnd) {
        return new SubstrExpression(operandStr, operandBegin, operandEnd);
    }

    @Override
    public String evaluateAsText(final Context context) throws EvaluationException {
        String str = operandStr.evaluateAsText(context);
        long begin = operandBegin.evaluateAsInteger(context);
        long end = operandEnd.evaluateAsInteger(context);

        if (begin < 0) {
            begin = str.length() + begin + 1;
        }

        if (end < 0) {
            end = str.length() + end + 1;
        }

        begin = Math.max(begin, 0);
        end = Math.max(Math.min(end, str.length()), 0);

        return str.substring((int) begin, (int) end);
    }

    @Override
    public String getExpressionName() {
        return Parser.SUBSTR_EXPRESSION;
    }
}
