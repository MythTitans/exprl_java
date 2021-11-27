package mythtitans.exprl;

import mythtitans.exprl.eval.Context;
import mythtitans.exprl.eval.Expression;
import mythtitans.exprl.eval.impl.AndExpression;
import mythtitans.exprl.eval.impl.LiteralExpression;
import mythtitans.exprl.eval.impl.VarExpression;
import mythtitans.exprl.parser.Parser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.LockSupport;

import static org.mockito.Mockito.when;

public class ExpressionEvaluatorTest {

    @Mock
    private Parser mockParser;

    private ExpressionEvaluator evaluator;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
        evaluator = new ExpressionEvaluator();
    }

    @Test
    public void evaluate_raw_expression() throws Parser.ParsingException, Expression.EvaluationException {
        Assert.assertTrue(evaluator.evaluateAsBoolean("true"));
        Assert.assertEquals(42, evaluator.evaluateAsInteger("42"));
        Assert.assertEquals(42.5, evaluator.evaluateAsDecimal("42.5"), 0.000001);
        Assert.assertEquals("abcdef", evaluator.evaluateAsText("'abcdef'"));
        Assert.assertTrue(evaluator.evaluateAsBoolean("and(true, true)"));
    }

    @Test
    public void evaluate_parsed_expression() throws Parser.ParsingException, Expression.EvaluationException {
        Parser parser = new Parser();
        Expression expr1 = parser.parse("true");
        Expression expr2 = parser.parse("42");
        Expression expr3 = parser.parse("42.5");
        Expression expr4 = parser.parse("'abcdef'");
        Expression expr5 = parser.parse("and(true, true)");

        Assert.assertTrue(evaluator.evaluateAsBoolean(expr1));
        Assert.assertEquals(42, evaluator.evaluateAsInteger(expr2));
        Assert.assertEquals(42.5, evaluator.evaluateAsDecimal(expr3), 0.000001);
        Assert.assertEquals("abcdef", evaluator.evaluateAsText(expr4));
        Assert.assertTrue(evaluator.evaluateAsBoolean(expr5));
    }

    @Test
    public void updating_variable_is_thread_safe_for_evaluation() throws Parser.ParsingException, Expression.EvaluationException {
        CountDownLatch afterOperandALatch = new CountDownLatch(1);
        ControlledVarExpression operandA = new ControlledVarExpression(LiteralExpression.literal("var1"), Duration.ZERO, afterOperandALatch);
        ControlledVarExpression operandB = new ControlledVarExpression(LiteralExpression.literal("var2"), Duration.ofSeconds(1), new CountDownLatch(1)); // A delay of 1 second should be enough to check that var2 was not updated during the evaluation process

        when(mockParser.parse("and(var('var1'), var('var2'))")).thenReturn(AndExpression.and(operandA, operandB));

        // Change var2 to false right after var('var1') is evaluated
        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                afterOperandALatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            evaluator.setVariable("var2", false);
        });

        evaluator.setVariable("var1", true);
        evaluator.setVariable("var2", true);

        Expression expression = mockParser.parse("and(var('var1'), var('var2'))");
        Assert.assertTrue(evaluator.evaluateAsBoolean(expression));
    }

    private static class ControlledVarExpression extends VarExpression {

        private final Duration evaluationDelay;
        private final CountDownLatch afterEvaluationLatch;

        public ControlledVarExpression(final Expression operand, final Duration evaluationDelay, final CountDownLatch afterEvaluationLatch) {
            super(operand);
            this.evaluationDelay = evaluationDelay;
            this.afterEvaluationLatch = afterEvaluationLatch;
        }

        @Override
        public boolean evaluateAsBoolean(final Context context) throws EvaluationException {
            LockSupport.parkNanos(evaluationDelay.toNanos());

            boolean result = super.evaluateAsBoolean(context);
            afterEvaluationLatch.countDown();

            return result;
        }
    }
}
