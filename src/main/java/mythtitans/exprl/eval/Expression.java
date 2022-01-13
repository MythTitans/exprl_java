package mythtitans.exprl.eval;

import java.util.Set;

public interface Expression {

    default boolean evaluateAsBoolean(final Context context) throws EvaluationException {
        throw EvaluationException.booleanEvaluationError(getExpressionName());
    }

    default long evaluateAsInteger(final Context context) throws EvaluationException {
        throw EvaluationException.integerEvaluationError(getExpressionName());
    }

    default double evaluateAsDecimal(final Context context) throws EvaluationException {
        throw EvaluationException.decimalEvaluationError(getExpressionName());
    }

    default String evaluateAsText(final Context context) throws EvaluationException {
        throw EvaluationException.textEvaluationError(getExpressionName());
    }

    String getExpressionName();

    Set<String> getLiteralVariables();

    class EvaluationException extends Exception {

        public EvaluationException(final String message) {
            super(message);
        }

        public static EvaluationException booleanEvaluationError(final String operation) {
            return new EvaluationException(String.format("Expression [%s] cannot be evaluated as [boolean].", operation));
        }

        public static EvaluationException integerEvaluationError(final String operation) {
            return new EvaluationException(String.format("Expression [%s] cannot be evaluated as [integer].", operation));
        }

        public static EvaluationException decimalEvaluationError(final String operation) {
            return new EvaluationException(String.format("Expression [%s] cannot be evaluated as [decimal].", operation));
        }

        public static EvaluationException textEvaluationError(final String operation) {
            return new EvaluationException(String.format("Expression [%s] cannot be evaluated as [text].", operation));
        }
    }
}
