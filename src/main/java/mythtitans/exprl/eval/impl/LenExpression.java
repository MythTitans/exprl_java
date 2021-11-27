package mythtitans.exprl.eval.impl;

import mythtitans.exprl.eval.Context;
import mythtitans.exprl.eval.Expression;
import mythtitans.exprl.parser.Parser;

public class LenExpression implements Expression {

    private final Expression operandStr;

    public LenExpression(final Expression operandStr) {
        this.operandStr = operandStr;
    }

    public static Expression len(final Expression operandStr) {
        return new LenExpression(operandStr);
    }

    @Override
    public long evaluateAsInteger(final Context context) throws EvaluationException {
        return operandStr.evaluateAsText(context).length();
    }

    @Override
    public double evaluateAsDecimal(final Context context) throws EvaluationException {
        return operandStr.evaluateAsText(context).length();
    }

    @Override
    public String getExpressionName() {
        return Parser.LEN_EXPRESSION;
    }
}
