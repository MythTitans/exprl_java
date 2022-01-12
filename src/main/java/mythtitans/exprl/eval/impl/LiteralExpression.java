package mythtitans.exprl.eval.impl;

import mythtitans.exprl.eval.Context;
import mythtitans.exprl.eval.Expression;

import java.util.Locale;

public abstract class LiteralExpression<T> implements Expression {

    protected final T operand;

    public LiteralExpression(final T operand) {
        this.operand = operand;
    }

    public static Expression literal(final boolean operand) {
        return new BooleanExpression(operand);
    }

    public static Expression literal(final long operand) {
        return new IntegerExpression(operand);
    }

    public static Expression literal(final double operand) {
        return new DecimalExpression(operand);
    }

    public static Expression literal(final String operand) {
        return new TextExpression(operand);
    }

    private static class BooleanExpression extends LiteralExpression<Boolean> {

        public BooleanExpression(final boolean operand) {
            super(operand);
        }

        @Override
        public boolean evaluateAsBoolean(final Context context) {
            return operand;
        }

        @Override
        public String getExpressionName() {
            return Boolean.toString(operand);
        }
    }

    private static class IntegerExpression extends LiteralExpression<Long> {

        public IntegerExpression(final long operand) {
            super(operand);
        }

        @Override
        public long evaluateAsInteger(final Context context) {
            return operand;
        }

        @Override
        public double evaluateAsDecimal(final Context context) {
            return operand;
        }

        @Override
        public String getExpressionName() {
            return Long.toString(operand);
        }
    }

    private static class DecimalExpression extends LiteralExpression<Double> {

        public DecimalExpression(final double operand) {
            super(operand);
        }

        @Override
        public double evaluateAsDecimal(final Context context) {
            return operand;
        }

        @Override
        public String getExpressionName() {
            return String.format(Locale.US, "%.6f", operand);
        }
    }

    private static class TextExpression extends LiteralExpression<String> {

        public TextExpression(final String operand) {
            super(operand);
        }

        @Override
        public String evaluateAsText(final Context context) {
            return operand;
        }

        @Override
        public String getExpressionName() {
            return operand;
        }
    }
}
