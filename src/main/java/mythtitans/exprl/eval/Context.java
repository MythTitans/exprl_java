package mythtitans.exprl.eval;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import static mythtitans.exprl.eval.Context.Variable.variable;

public class Context {

    private final Map<String, Variable<?>> variables;
    private final Consumer<String> debugMessageHandler;

    public Context() {
        this(ignored -> {
        });
    }

    public Context(final Consumer<String> debugMessageHandler) {
        this.variables = new HashMap<>();
        this.debugMessageHandler = debugMessageHandler;
    }

    public void setVariable(final String variable, final boolean value) {
        variables.put(variable, variable(variable, value));
    }

    public void setVariable(final String variable, final long value) {
        variables.put(variable, variable(variable, value));
    }

    public void setVariable(final String variable, final double value) {
        variables.put(variable, variable(variable, value));
    }

    public void setVariable(final String variable, final String value) {
        variables.put(variable, variable(variable, value));
    }

    public Optional<Variable<?>> getVariable(final String variable) {
        return Optional.ofNullable(variables.get(variable));
    }

    public Consumer<String> getDebugMessageHandler() {
        return debugMessageHandler;
    }

    public static abstract class Variable <T> {

        protected final T value;
        private final String variable;

        private Variable(final String variable, final T value) {
            this.variable = variable;
            this.value = value;
        }

        public static Variable<Boolean> variable(final String variable, final boolean value) {
            return new BooleanVariable(variable, value);
        }

        public static Variable<Long> variable(final String variable, final long value) {
            return new IntegerVariable(variable, value);
        }

        public static Variable<Double> variable(final String variable, final double value) {
            return new DecimalVariable(variable, value);
        }

        public static Variable<String> variable(final String variable, final String value) {
            return new TextVariable(variable, value);
        }

        public boolean asBoolean() throws Expression.EvaluationException {
            throw new Expression.EvaluationException(String.format("Variable [%s] cannot be evaluated as [boolean].", variable));
        }

        public long asInteger() throws Expression.EvaluationException {
            throw new Expression.EvaluationException(String.format("Variable [%s] cannot be evaluated as [integer].", variable));
        }

        public double asDecimal() throws Expression.EvaluationException {
            throw new Expression.EvaluationException(String.format("Variable [%s] cannot be evaluated as [decimal].", variable));
        }

        public String asText() throws Expression.EvaluationException {
            throw new Expression.EvaluationException(String.format("Variable [%s] cannot be evaluated as [text].", variable));
        }
    }

    public static class BooleanVariable extends Variable<Boolean> {

        public BooleanVariable(final String variable, final boolean value) {
            super(variable, value);
        }

        @Override
        public boolean asBoolean() {
            return value;
        }
    }

    public static class IntegerVariable extends Variable<Long> {

        public IntegerVariable(final String variable, final long value) {
            super(variable, value);
        }

        @Override
        public long asInteger() {
            return value;
        }

        @Override
        public double asDecimal() {
            return value;
        }
    }

    public static class DecimalVariable extends Variable<Double> {

        public DecimalVariable(final String variable, final double value) {
            super(variable, value);
        }

        @Override
        public double asDecimal() {
            return value;
        }
    }

    public static class TextVariable extends Variable<String> {

        public TextVariable(final String variable, final String value) {
            super(variable, value);
        }

        @Override
        public String asText() {
            return value;
        }
    }
}
