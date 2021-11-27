package mythtitans.exprl;

import mythtitans.exprl.eval.Context;
import mythtitans.exprl.eval.Expression;
import mythtitans.exprl.parser.Parser;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ExpressionEvaluator {

    private final Parser parser;
    private final Context context;
    private final ReadWriteLock lock;

    public ExpressionEvaluator() {
        this(new Parser());
    }

    public ExpressionEvaluator(final Parser parser) {
        this.parser = parser;
        this.context = new Context();
        this.lock = new ReentrantReadWriteLock();
    }

    public void setVariable(final String variable, final boolean value) {
        lock.writeLock().lock();
        try {
            context.setVariable(variable, value);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void setVariable(final String variable, final long value) {
        lock.writeLock().lock();
        try {
            context.setVariable(variable, value);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void setVariable(final String variable, final double value) {
        lock.writeLock().lock();
        try {
            context.setVariable(variable, value);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void setVariable(final String variable, final String value) {
        lock.writeLock().lock();
        try {
            context.setVariable(variable, value);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean evaluateAsBoolean(final String expression) throws Parser.ParsingException, Expression.EvaluationException {
        return evaluateAsBoolean(parser.parse(expression));
    }

    public long evaluateAsInteger(final String expression) throws Parser.ParsingException, Expression.EvaluationException {
        return evaluateAsInteger(parser.parse(expression));
    }

    public double evaluateAsDecimal(final String expression) throws Parser.ParsingException, Expression.EvaluationException {
        return evaluateAsDecimal(parser.parse(expression));
    }

    public String evaluateAsText(final String expression) throws Parser.ParsingException, Expression.EvaluationException {
        return evaluateAsText(parser.parse(expression));
    }

    public boolean evaluateAsBoolean(final Expression expression) throws Expression.EvaluationException {
        lock.readLock().lock();
        try {
            return expression.evaluateAsBoolean(context);
        } finally {
            lock.readLock().unlock();
        }
    }

    public long evaluateAsInteger(final Expression expression) throws Expression.EvaluationException {
        lock.readLock().lock();
        try {
            return expression.evaluateAsInteger(context);
        } finally {
            lock.readLock().unlock();
        }
    }

    public double evaluateAsDecimal(final Expression expression) throws Expression.EvaluationException {
        lock.readLock().lock();
        try {
            return expression.evaluateAsDecimal(context);
        } finally {
            lock.readLock().unlock();
        }
    }

    public String evaluateAsText(final Expression expression) throws Expression.EvaluationException {
        lock.readLock().lock();
        try {
            return expression.evaluateAsText(context);
        } finally {
            lock.readLock().unlock();
        }
    }
}
