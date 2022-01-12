package mythtitans.exprl.eval;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.function.Consumer;

import static mythtitans.exprl.eval.impl.AddExpression.add;
import static mythtitans.exprl.eval.impl.AndExpression.and;
import static mythtitans.exprl.eval.impl.ConcatExpression.concat;
import static mythtitans.exprl.eval.impl.CondExpression.cond;
import static mythtitans.exprl.eval.impl.DebugExpression.debug;
import static mythtitans.exprl.eval.impl.DivExpression.div;
import static mythtitans.exprl.eval.impl.EndsExpression.ends;
import static mythtitans.exprl.eval.impl.EqExpression.EvaluationException;
import static mythtitans.exprl.eval.impl.EqExpression.eq;
import static mythtitans.exprl.eval.impl.GtExpression.gt;
import static mythtitans.exprl.eval.impl.GteExpression.gte;
import static mythtitans.exprl.eval.impl.InExpression.in;
import static mythtitans.exprl.eval.impl.LenExpression.len;
import static mythtitans.exprl.eval.impl.LiteralExpression.literal;
import static mythtitans.exprl.eval.impl.LtExpression.lt;
import static mythtitans.exprl.eval.impl.LteExpression.lte;
import static mythtitans.exprl.eval.impl.MaxExpression.max;
import static mythtitans.exprl.eval.impl.MinExpression.min;
import static mythtitans.exprl.eval.impl.ModExpression.mod;
import static mythtitans.exprl.eval.impl.MulExpression.mul;
import static mythtitans.exprl.eval.impl.NeqExpression.neq;
import static mythtitans.exprl.eval.impl.NotExpression.not;
import static mythtitans.exprl.eval.impl.OrExpression.or;
import static mythtitans.exprl.eval.impl.StartsExpression.starts;
import static mythtitans.exprl.eval.impl.SubExpression.sub;
import static mythtitans.exprl.eval.impl.SubstrExpression.substr;
import static mythtitans.exprl.eval.impl.SubstrlExpression.substrl;
import static mythtitans.exprl.eval.impl.VarExpression.var;
import static org.mockito.Mockito.verify;

public class ExpressionTest {

    private static final double EPSILON = 0.000001;

    @Mock
    private Consumer<String> debugMessageConsumer;

    private Context context;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);

        context = new Context(debugMessageConsumer);
    }

    @Test
    public void literal_test() throws EvaluationException {
        Assert.assertTrue(literal(true).evaluateAsBoolean(context));
        Assert.assertFalse(literal(false).evaluateAsBoolean(context));
        Assert.assertEquals(1, literal(1).evaluateAsInteger(context));
        Assert.assertEquals(-1, literal(-1).evaluateAsInteger(context));
        Assert.assertEquals(1, literal(1).evaluateAsDecimal(context), EPSILON);
        Assert.assertEquals(-1, literal(-1).evaluateAsDecimal(context), EPSILON);
        Assert.assertEquals(1, literal(1.0).evaluateAsDecimal(context), EPSILON);
        Assert.assertEquals(-1, literal(-1.0).evaluateAsDecimal(context), EPSILON);
        Assert.assertEquals("abcdef", literal("abcdef").evaluateAsText(context));

        Assert.assertEquals("true", literal(true).getExpressionName());
        Assert.assertEquals("false", literal(false).getExpressionName());
        Assert.assertEquals("1", literal(1).getExpressionName());
        Assert.assertEquals("1.000000", literal(1.0).getExpressionName());
        Assert.assertEquals("abcdef", literal("abcdef").getExpressionName());
    }

    @Test
    public void not_test() throws Expression.EvaluationException {
        Assert.assertTrue(not(literal(false)).evaluateAsBoolean(context));
        Assert.assertFalse(not(literal(true)).evaluateAsBoolean(context));

        Assert.assertEquals("not", not(literal(true)).getExpressionName());
    }

    @Test
    public void and_test() throws Expression.EvaluationException {
        Assert.assertFalse(and(literal(false), literal(false)).evaluateAsBoolean(context));
        Assert.assertFalse(and(literal(false), literal(true)).evaluateAsBoolean(context));
        Assert.assertFalse(and(literal(true), literal(false)).evaluateAsBoolean(context));
        Assert.assertTrue(and(literal(true), literal(true)).evaluateAsBoolean(context));

        Assert.assertFalse(and(literal(false), literal(false), literal(false)).evaluateAsBoolean(context));
        Assert.assertFalse(and(literal(false), literal(false), literal(true)).evaluateAsBoolean(context));
        Assert.assertFalse(and(literal(false), literal(true), literal(false)).evaluateAsBoolean(context));
        Assert.assertFalse(and(literal(false), literal(true), literal(true)).evaluateAsBoolean(context));
        Assert.assertFalse(and(literal(true), literal(false), literal(false)).evaluateAsBoolean(context));
        Assert.assertFalse(and(literal(true), literal(false), literal(true)).evaluateAsBoolean(context));
        Assert.assertFalse(and(literal(true), literal(true), literal(false)).evaluateAsBoolean(context));
        Assert.assertTrue(and(literal(true), literal(true), literal(true)).evaluateAsBoolean(context));

        Assert.assertEquals("and", and(literal(true), literal(true)).getExpressionName());
    }

    @Test
    public void or_test() throws Expression.EvaluationException {
        Assert.assertFalse(or(literal(false), literal(false)).evaluateAsBoolean(context));
        Assert.assertTrue(or(literal(false), literal(true)).evaluateAsBoolean(context));
        Assert.assertTrue(or(literal(true), literal(false)).evaluateAsBoolean(context));
        Assert.assertTrue(or(literal(true), literal(true)).evaluateAsBoolean(context));

        Assert.assertFalse(or(literal(false), literal(false), literal(false)).evaluateAsBoolean(context));
        Assert.assertTrue(or(literal(false), literal(false), literal(true)).evaluateAsBoolean(context));
        Assert.assertTrue(or(literal(false), literal(true), literal(false)).evaluateAsBoolean(context));
        Assert.assertTrue(or(literal(false), literal(true), literal(true)).evaluateAsBoolean(context));
        Assert.assertTrue(or(literal(true), literal(false), literal(false)).evaluateAsBoolean(context));
        Assert.assertTrue(or(literal(true), literal(false), literal(true)).evaluateAsBoolean(context));
        Assert.assertTrue(or(literal(true), literal(true), literal(false)).evaluateAsBoolean(context));
        Assert.assertTrue(or(literal(true), literal(true), literal(true)).evaluateAsBoolean(context));

        Assert.assertEquals("or", or(literal(true), literal(true)).getExpressionName());
    }

    @Test
    public void add_test() throws Expression.EvaluationException {
        Assert.assertEquals(3, add(literal(1), literal(2)).evaluateAsInteger(context));
        Assert.assertEquals(-1, add(literal(1), literal(-2)).evaluateAsInteger(context));
        Assert.assertEquals(6, add(literal(1), literal(2), literal(3)).evaluateAsInteger(context));
        Assert.assertEquals(0, add(literal(1), literal(2), literal(-3)).evaluateAsInteger(context));
        Assert.assertEquals(-4, add(literal(1), literal(-2), literal(-3)).evaluateAsInteger(context));

        Assert.assertEquals(3, add(literal(1), literal(2)).evaluateAsDecimal(context), EPSILON);
        Assert.assertEquals(-1, add(literal(1), literal(-2)).evaluateAsDecimal(context), EPSILON);
        Assert.assertEquals(6, add(literal(1), literal(2), literal(3)).evaluateAsDecimal(context), EPSILON);
        Assert.assertEquals(0, add(literal(1), literal(2), literal(-3)).evaluateAsDecimal(context), EPSILON);
        Assert.assertEquals(-4, add(literal(1), literal(-2), literal(-3)).evaluateAsDecimal(context), EPSILON);

        Assert.assertEquals("add", add(literal(1), literal(2)).getExpressionName());
    }

    @Test
    public void sub_test() throws Expression.EvaluationException {
        Assert.assertEquals(-1, sub(literal(1), literal(2)).evaluateAsInteger(context));
        Assert.assertEquals(3, sub(literal(1), literal(-2)).evaluateAsInteger(context));

        Assert.assertEquals(-1, sub(literal(1), literal(2)).evaluateAsDecimal(context), EPSILON);
        Assert.assertEquals(3, sub(literal(1), literal(-2)).evaluateAsDecimal(context), EPSILON);

        Assert.assertEquals("sub", sub(literal(2), literal(1)).getExpressionName());
    }

    @Test
    public void mul_test() throws Expression.EvaluationException {
        Assert.assertEquals(6, mul(literal(2), literal(3)).evaluateAsInteger(context));
        Assert.assertEquals(-6, mul(literal(2), literal(-3)).evaluateAsInteger(context));
        Assert.assertEquals(24, mul(literal(2), literal(3), literal(4)).evaluateAsInteger(context));
        Assert.assertEquals(-24, mul(literal(2), literal(3), literal(-4)).evaluateAsInteger(context));

        Assert.assertEquals(6, mul(literal(2), literal(3)).evaluateAsDecimal(context), EPSILON);
        Assert.assertEquals(-6, mul(literal(2), literal(-3)).evaluateAsDecimal(context), EPSILON);
        Assert.assertEquals(24, mul(literal(2), literal(3), literal(4)).evaluateAsDecimal(context), EPSILON);
        Assert.assertEquals(-24, mul(literal(2), literal(3), literal(-4)).evaluateAsDecimal(context), EPSILON);

        Assert.assertEquals("mul", mul(literal(1), literal(2)).getExpressionName());
    }

    @Test
    public void div_test() throws Expression.EvaluationException {
        Assert.assertEquals(3, div(literal(6), literal(2)).evaluateAsInteger(context));
        Assert.assertEquals(-3, div(literal(6), literal(-2)).evaluateAsInteger(context));
        Assert.assertEquals(1, div(literal(6), literal(4)).evaluateAsInteger(context));

        Assert.assertEquals(3, div(literal(6), literal(2)).evaluateAsDecimal(context), EPSILON);
        Assert.assertEquals(-3, div(literal(6), literal(-2)).evaluateAsDecimal(context), EPSILON);
        Assert.assertEquals(1.5, div(literal(6), literal(4)).evaluateAsDecimal(context), EPSILON);

        Assert.assertEquals("div", div(literal(2), literal(2)).getExpressionName());
    }

    @Test
    public void mod_test() throws Expression.EvaluationException {
        Assert.assertEquals(0, mod(literal(6), literal(2)).evaluateAsInteger(context));
        Assert.assertEquals(1, mod(literal(5), literal(2)).evaluateAsInteger(context));

        Assert.assertEquals(0, mod(literal(6), literal(2)).evaluateAsDecimal(context), EPSILON);
        Assert.assertEquals(1, mod(literal(5), literal(2)).evaluateAsDecimal(context), EPSILON);

        Assert.assertEquals("mod", mod(literal(2), literal(2)).getExpressionName());
    }

    @Test
    public void starts_test() throws Expression.EvaluationException {
        Assert.assertTrue(starts(literal("abcdef"), literal("")).evaluateAsBoolean(context));
        Assert.assertTrue(starts(literal("abcdef"), literal("a")).evaluateAsBoolean(context));
        Assert.assertTrue(starts(literal("abcdef"), literal("ab")).evaluateAsBoolean(context));
        Assert.assertFalse(starts(literal("abcdef"), literal("b")).evaluateAsBoolean(context));
        Assert.assertFalse(starts(literal("abcdef"), literal("bc")).evaluateAsBoolean(context));

        Assert.assertEquals("starts", starts(literal("abcdef"), literal("ab")).getExpressionName());
    }

    @Test
    public void ends_test() throws Expression.EvaluationException {
        Assert.assertTrue(ends(literal("abcdef"), literal("")).evaluateAsBoolean(context));
        Assert.assertTrue(ends(literal("abcdef"), literal("f")).evaluateAsBoolean(context));
        Assert.assertTrue(ends(literal("abcdef"), literal("ef")).evaluateAsBoolean(context));
        Assert.assertFalse(ends(literal("abcdef"), literal("e")).evaluateAsBoolean(context));
        Assert.assertFalse(ends(literal("abcdef"), literal("de")).evaluateAsBoolean(context));

        Assert.assertEquals("ends", ends(literal("abcdef"), literal("ab")).getExpressionName());
    }

    @Test
    public void in_test() throws Expression.EvaluationException {
        Assert.assertTrue(in(literal("abcdef"), literal("")).evaluateAsBoolean(context));
        Assert.assertTrue(in(literal("abcdef"), literal("a")).evaluateAsBoolean(context));
        Assert.assertTrue(in(literal("abcdef"), literal("ab")).evaluateAsBoolean(context));
        Assert.assertTrue(in(literal("abcdef"), literal("b")).evaluateAsBoolean(context));
        Assert.assertTrue(in(literal("abcdef"), literal("bc")).evaluateAsBoolean(context));
        Assert.assertTrue(in(literal("abcdef"), literal("e")).evaluateAsBoolean(context));
        Assert.assertTrue(in(literal("abcdef"), literal("ef")).evaluateAsBoolean(context));
        Assert.assertFalse(in(literal("abcdef"), literal("ac")).evaluateAsBoolean(context));

        Assert.assertEquals("in", in(literal("abcdef"), literal("ab")).getExpressionName());
    }

    @Test
    public void substr_test() throws Expression.EvaluationException {
        Assert.assertEquals("abcdef", substr(literal("abcdef"), literal(0), literal(6)).evaluateAsText(context));
        Assert.assertEquals("abcde", substr(literal("abcdef"), literal(0), literal(5)).evaluateAsText(context));
        Assert.assertEquals("bcdef", substr(literal("abcdef"), literal(1), literal(6)).evaluateAsText(context));
        Assert.assertEquals("abcdef", substr(literal("abcdef"), literal(0), literal(-1)).evaluateAsText(context));
        Assert.assertEquals("ef", substr(literal("abcdef"), literal(-3), literal(-1)).evaluateAsText(context));
        Assert.assertEquals("f", substr(literal("abcdef"), literal(-2), literal(-1)).evaluateAsText(context));

        Assert.assertEquals("substr", substr(literal("abcdef"), literal(1), literal(2)).getExpressionName());
    }

    @Test
    public void substrl_test() throws Expression.EvaluationException {
        Assert.assertEquals("abcdef", substrl(literal("abcdef"), literal(0), literal(6)).evaluateAsText(context));
        Assert.assertEquals("abcde", substrl(literal("abcdef"), literal(0), literal(5)).evaluateAsText(context));
        Assert.assertEquals("bcdef", substrl(literal("abcdef"), literal(1), literal(5)).evaluateAsText(context));
        Assert.assertEquals("f", substrl(literal("abcdef"), literal(5), literal(1)).evaluateAsText(context));
        Assert.assertEquals("f", substrl(literal("abcdef"), literal(-2), literal(1)).evaluateAsText(context));
        Assert.assertEquals("ef", substrl(literal("abcdef"), literal(-3), literal(2)).evaluateAsText(context));

        Assert.assertEquals("substrl", substrl(literal("abcdef"), literal(1), literal(2)).getExpressionName());
    }

    @Test
    public void len_test() throws Expression.EvaluationException {
        Assert.assertEquals(6, len(literal("abcdef")).evaluateAsInteger(context));
        Assert.assertEquals(5, len(literal("abcde")).evaluateAsInteger(context));
        Assert.assertEquals(0, len(literal("")).evaluateAsInteger(context));

        Assert.assertEquals(6, len(literal("abcdef")).evaluateAsDecimal(context), EPSILON);
        Assert.assertEquals(5, len(literal("abcde")).evaluateAsDecimal(context), EPSILON);
        Assert.assertEquals(0, len(literal("")).evaluateAsDecimal(context), EPSILON);

        Assert.assertEquals("len", len(literal("abcdef")).getExpressionName());
    }

    @Test
    public void eq_test() throws Expression.EvaluationException {
        Assert.assertTrue(eq(literal(true), literal(true)).evaluateAsBoolean(context));
        Assert.assertFalse(eq(literal(true), literal(false)).evaluateAsBoolean(context));
        Assert.assertTrue(eq(literal(1), literal(1)).evaluateAsBoolean(context));
        Assert.assertFalse(eq(literal(1), literal(2)).evaluateAsBoolean(context));
        Assert.assertTrue(eq(literal(1), literal(1)).evaluateAsBoolean(context));
        Assert.assertFalse(eq(literal(1), literal(2)).evaluateAsBoolean(context));
        Assert.assertTrue(eq(literal("abcdef"), literal("abcdef")).evaluateAsBoolean(context));
        Assert.assertFalse(eq(literal("abcdef"), literal("abcd")).evaluateAsBoolean(context));

        Assert.assertEquals("eq", eq(literal(true), literal(true)).getExpressionName());
        Assert.assertEquals("eq", eq(literal(1), literal(1)).getExpressionName());
        Assert.assertEquals("eq", eq(literal(1.0), literal(1.0)).getExpressionName());
        Assert.assertEquals("eq", eq(literal("abcdef"), literal("abcdef")).getExpressionName());
    }

    @Test
    public void neq_test() throws EvaluationException {
        Assert.assertFalse(neq(literal(true), literal(true)).evaluateAsBoolean(context));
        Assert.assertTrue(neq(literal(true), literal(false)).evaluateAsBoolean(context));
        Assert.assertFalse(neq(literal(1), literal(1)).evaluateAsBoolean(context));
        Assert.assertTrue(neq(literal(1), literal(2)).evaluateAsBoolean(context));
        Assert.assertFalse(neq(literal(1), literal(1)).evaluateAsBoolean(context));
        Assert.assertTrue(neq(literal(1), literal(2)).evaluateAsBoolean(context));
        Assert.assertFalse(neq(literal("abcdef"), literal("abcdef")).evaluateAsBoolean(context));
        Assert.assertTrue(neq(literal("abcdef"), literal("abcd")).evaluateAsBoolean(context));

        Assert.assertEquals("neq", neq(literal(true), literal(true)).getExpressionName());
        Assert.assertEquals("neq", neq(literal(1), literal(1)).getExpressionName());
        Assert.assertEquals("neq", neq(literal(1.0), literal(1.0)).getExpressionName());
        Assert.assertEquals("neq", neq(literal("abcdef"), literal("abcdef")).getExpressionName());
    }

    @Test
    public void lt_test() throws EvaluationException {
        Assert.assertTrue(lt(literal(1), literal(2)).evaluateAsBoolean(context));
        Assert.assertFalse(lt(literal(2), literal(2)).evaluateAsBoolean(context));
        Assert.assertFalse(lt(literal(3), literal(2)).evaluateAsBoolean(context));
        Assert.assertTrue(lt(literal(1), literal(2)).evaluateAsBoolean(context));
        Assert.assertFalse(lt(literal(2), literal(2)).evaluateAsBoolean(context));
        Assert.assertFalse(lt(literal(3), literal(2)).evaluateAsBoolean(context));

        Assert.assertEquals("lt", lt(literal(1), literal(1)).getExpressionName());
        Assert.assertEquals("lt", lt(literal(1.0), literal(1.0)).getExpressionName());
    }

    @Test
    public void lte_test() throws EvaluationException {
        Assert.assertTrue(lte(literal(1), literal(2)).evaluateAsBoolean(context));
        Assert.assertTrue(lte(literal(2), literal(2)).evaluateAsBoolean(context));
        Assert.assertFalse(lte(literal(3), literal(2)).evaluateAsBoolean(context));
        Assert.assertTrue(lte(literal(1), literal(2)).evaluateAsBoolean(context));
        Assert.assertTrue(lte(literal(2), literal(2)).evaluateAsBoolean(context));
        Assert.assertFalse(lte(literal(3), literal(2)).evaluateAsBoolean(context));

        Assert.assertEquals("lte", lte(literal(1), literal(1)).getExpressionName());
        Assert.assertEquals("lte", lte(literal(1.0), literal(1.0)).getExpressionName());
    }

    @Test
    public void gt_test() throws EvaluationException {
        Assert.assertFalse(gt(literal(1), literal(2)).evaluateAsBoolean(context));
        Assert.assertFalse(gt(literal(2), literal(2)).evaluateAsBoolean(context));
        Assert.assertTrue(gt(literal(3), literal(2)).evaluateAsBoolean(context));
        Assert.assertFalse(gt(literal(1), literal(2)).evaluateAsBoolean(context));
        Assert.assertFalse(gt(literal(2), literal(2)).evaluateAsBoolean(context));
        Assert.assertTrue(gt(literal(3), literal(2)).evaluateAsBoolean(context));

        Assert.assertEquals("gt", gt(literal(1), literal(1)).getExpressionName());
        Assert.assertEquals("gt", gt(literal(1.0), literal(1.0)).getExpressionName());
    }

    @Test
    public void gte_test() throws EvaluationException {
        Assert.assertFalse(gte(literal(1), literal(2)).evaluateAsBoolean(context));
        Assert.assertTrue(gte(literal(2), literal(2)).evaluateAsBoolean(context));
        Assert.assertTrue(gte(literal(3), literal(2)).evaluateAsBoolean(context));
        Assert.assertFalse(gte(literal(1), literal(2)).evaluateAsBoolean(context));
        Assert.assertTrue(gte(literal(2), literal(2)).evaluateAsBoolean(context));
        Assert.assertTrue(gte(literal(3), literal(2)).evaluateAsBoolean(context));

        Assert.assertEquals("gte", gte(literal(1), literal(1)).getExpressionName());
        Assert.assertEquals("gte", gte(literal(1.0), literal(1.0)).getExpressionName());
    }

    @Test
    public void min_test() throws Expression.EvaluationException {
        Assert.assertEquals(0, min(literal(0), literal(1)).evaluateAsInteger(context));
        Assert.assertEquals(-1, min(literal(0), literal(-1)).evaluateAsInteger(context));
        Assert.assertEquals(-5, min(literal(0), literal(-1), literal(-5)).evaluateAsInteger(context));

        Assert.assertEquals(0, min(literal(0), literal(1)).evaluateAsDecimal(context), EPSILON);
        Assert.assertEquals(-1, min(literal(0), literal(-1)).evaluateAsDecimal(context), EPSILON);
        Assert.assertEquals(-5, min(literal(0), literal(-1), literal(-5)).evaluateAsDecimal(context), EPSILON);

        Assert.assertEquals("min", min(literal(1), literal(2)).getExpressionName());
    }

    @Test
    public void max_test() throws Expression.EvaluationException {
        Assert.assertEquals(1, max(literal(0), literal(1)).evaluateAsInteger(context));
        Assert.assertEquals(0, max(literal(0), literal(-1)).evaluateAsInteger(context));
        Assert.assertEquals(5, max(literal(0), literal(-1), literal(5)).evaluateAsInteger(context));

        Assert.assertEquals(1, max(literal(0), literal(1)).evaluateAsDecimal(context), EPSILON);
        Assert.assertEquals(0, max(literal(0), literal(-1)).evaluateAsDecimal(context), EPSILON);
        Assert.assertEquals(5, max(literal(0), literal(-1), literal(5)).evaluateAsDecimal(context), EPSILON);

        Assert.assertEquals("max", max(literal(1), literal(2)).getExpressionName());
    }

    @Test
    public void var_test() throws EvaluationException {
        Context context = new Context();

        Assert.assertThrows(EvaluationException.class, () -> var(literal("variable-boolean")).evaluateAsBoolean(context));
        Assert.assertThrows(EvaluationException.class, () -> var(literal("variable-integer")).evaluateAsInteger(context));
        Assert.assertThrows(EvaluationException.class, () -> var(literal("variable-decimal")).evaluateAsDecimal(context));
        Assert.assertThrows(EvaluationException.class, () -> var(literal("variable-text")).evaluateAsText(context));

        context.setVariable("variable-boolean", true);
        context.setVariable("variable-integer", 1);
        context.setVariable("variable-decimal", 1.5);
        context.setVariable("variable-text", "abcdef");

        Assert.assertTrue(var(literal("variable-boolean")).evaluateAsBoolean(context));
        Assert.assertEquals(1, var(literal("variable-integer")).evaluateAsInteger(context));
        Assert.assertEquals(1.5, var(literal("variable-decimal")).evaluateAsDecimal(context), EPSILON);
        Assert.assertEquals("abcdef", var(literal("variable-text")).evaluateAsText(context));

        context.setVariable("variable-boolean", false);
        context.setVariable("variable-integer", 2);
        context.setVariable("variable-decimal", 4.5);
        context.setVariable("variable-text", "abcdefg");

        Assert.assertFalse(var(literal("variable-boolean")).evaluateAsBoolean(context));
        Assert.assertEquals(2, var(literal("variable-integer")).evaluateAsInteger(context));
        Assert.assertEquals(4.5, var(literal("variable-decimal")).evaluateAsDecimal(context), EPSILON);
        Assert.assertEquals("abcdefg", var(literal("variable-text")).evaluateAsText(context));

        Assert.assertEquals("var", var(literal("variable")).getExpressionName());
    }

    @Test
    public void cond_test() throws EvaluationException {
        Assert.assertTrue(cond(literal(true), literal(true), literal(false)).evaluateAsBoolean(context));
        Assert.assertFalse(cond(literal(false), literal(true), literal(false)).evaluateAsBoolean(context));

        Assert.assertEquals(1, cond(literal(true), literal(1), literal(2)).evaluateAsInteger(context));
        Assert.assertEquals(2, cond(literal(false), literal(1), literal(2)).evaluateAsInteger(context));

        Assert.assertEquals(1.5, cond(literal(true), literal(1.5), literal(2.5)).evaluateAsDecimal(context), EPSILON);
        Assert.assertEquals(2.5, cond(literal(false), literal(1.5), literal(2.5)).evaluateAsDecimal(context), EPSILON);

        Assert.assertEquals("a", cond(literal(true), literal("a"), literal("b")).evaluateAsText(context));
        Assert.assertEquals("b", cond(literal(false), literal("a"), literal("b")).evaluateAsText(context));

        Assert.assertEquals("cond", cond(literal(true), literal(true), literal(false)).getExpressionName());
    }

    @Test
    public void concat_test() throws EvaluationException {
        Assert.assertEquals("abcdef", concat(literal("abc"), literal("def")).evaluateAsText(context));
        Assert.assertEquals("abc", concat(literal("abc"), literal("")).evaluateAsText(context));
        Assert.assertEquals("def", concat(literal(""), literal("def")).evaluateAsText(context));

        Assert.assertEquals("abcdefghi", concat(literal("abc"), literal("def"), literal("ghi")).evaluateAsText(context));

        Assert.assertEquals("concat", concat(literal("abc"), literal("def")).getExpressionName());
    }

    @Test
    public void debug_test() throws EvaluationException {
        Assert.assertTrue(debug(literal(true), literal("boolean")).evaluateAsBoolean(context));
        verify(debugMessageConsumer).accept("boolean : true");

        Assert.assertEquals(12, debug(literal(12), literal("integer")).evaluateAsInteger(context));
        verify(debugMessageConsumer).accept("integer : 12");

        Assert.assertEquals(15.0, debug(literal(15.0), literal("decimal")).evaluateAsDecimal(context), EPSILON);
        verify(debugMessageConsumer).accept("decimal : 15.000000");

        Assert.assertEquals("abcdef", debug(literal("abcdef"), literal("text")).evaluateAsText(context));
        verify(debugMessageConsumer).accept("text : abcdef");

        Assert.assertEquals("debug", debug(literal("abc"), literal("def")).getExpressionName());
    }
}
