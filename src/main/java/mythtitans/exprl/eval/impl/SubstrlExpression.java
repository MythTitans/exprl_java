package mythtitans.exprl.eval.impl;

import mythtitans.exprl.eval.Context;
import mythtitans.exprl.eval.Expression;
import mythtitans.exprl.parser.Parser;

public class SubstrlExpression implements Expression {

    private final Expression operandStr;
    private final Expression operandBegin;
    private final Expression operandLength;

    public SubstrlExpression(final Expression operandStr, final Expression operandBegin, final Expression operandLength) {
        this.operandStr = operandStr;
        this.operandBegin = operandBegin;
        this.operandLength = operandLength;
    }

    public static Expression substrl(final Expression operandStr, final Expression operandFrom, final Expression operandLength) {
        return new SubstrlExpression(operandStr, operandFrom, operandLength);
    }

    @Override
    public String evaluateAsText(final Context context) throws EvaluationException {
        String str = operandStr.evaluateAsText(context);
        long begin = operandBegin.evaluateAsInteger(context);

        if (begin < 0) {
            begin = str.length() + begin + 1;
        }

        long end = begin + operandLength.evaluateAsInteger(context);

        begin = Math.max(begin, 0);
        end = Math.max(Math.min(end, str.length()), 0);

        return str.substring((int) begin, (int) end);
    }

    @Override
    public String getExpressionName() {
        return Parser.SUBSTRL_EXPRESSION;
    }
}
