package mythtitans.exprl.parser;

import mythtitans.exprl.eval.Context;
import mythtitans.exprl.eval.Expression;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static mythtitans.exprl.eval.impl.EqExpression.EvaluationException;

public class ParserTest {

    private Parser parser;
    private Context context;

    @Before
    public void init() {
        parser = new Parser();
        context = new Context();
    }

    @Test
    public void literal_test() throws Expression.EvaluationException, Parser.ParsingException {
        Assert.assertTrue(parser.parse("true").evaluateAsBoolean(context));
        Assert.assertFalse(parser.parse("false").evaluateAsBoolean(context));
        Assert.assertEquals(1, parser.parse("1").evaluateAsInteger(context));
        Assert.assertEquals(-1, parser.parse("-1").evaluateAsInteger(context));
        Assert.assertEquals(1, parser.parse("1").evaluateAsDecimal(context), 0.000001);
        Assert.assertEquals(-1, parser.parse("-1").evaluateAsDecimal(context), 0.000001);
        Assert.assertEquals(1.5, parser.parse("1.5").evaluateAsDecimal(context), 0.000001);
        Assert.assertEquals(-1.5, parser.parse("-1.5").evaluateAsDecimal(context), 0.000001);
        Assert.assertEquals("abcdef", parser.parse("'abcdef'").evaluateAsText(context));
    }

    @Test
    public void not_test() throws Expression.EvaluationException, Parser.ParsingException {
        Assert.assertTrue(parser.parse("not(false)").evaluateAsBoolean(context));
        Assert.assertFalse(parser.parse("not(true)").evaluateAsBoolean(context));
    }

    @Test
    public void and_test() throws Expression.EvaluationException, Parser.ParsingException {
        Assert.assertFalse(parser.parse("and(false, false)").evaluateAsBoolean(context));
        Assert.assertFalse(parser.parse("and(false, true)").evaluateAsBoolean(context));
        Assert.assertFalse(parser.parse("and(true, false)").evaluateAsBoolean(context));
        Assert.assertTrue(parser.parse("and(true, true)").evaluateAsBoolean(context));

        Assert.assertFalse(parser.parse("and(false, false, false)").evaluateAsBoolean(context));
        Assert.assertFalse(parser.parse("and(false, false, true)").evaluateAsBoolean(context));
        Assert.assertFalse(parser.parse("and(false, true, false)").evaluateAsBoolean(context));
        Assert.assertFalse(parser.parse("and(false, true, true)").evaluateAsBoolean(context));
        Assert.assertFalse(parser.parse("and(true, false, false)").evaluateAsBoolean(context));
        Assert.assertFalse(parser.parse("and(true, false, true)").evaluateAsBoolean(context));
        Assert.assertFalse(parser.parse("and(true, true, false)").evaluateAsBoolean(context));
        Assert.assertTrue(parser.parse("and(true, true, true)").evaluateAsBoolean(context));
    }

    @Test
    public void or_test() throws Expression.EvaluationException, Parser.ParsingException {
        Assert.assertFalse(parser.parse("or(false, false)").evaluateAsBoolean(context));
        Assert.assertTrue(parser.parse("or(false, true)").evaluateAsBoolean(context));
        Assert.assertTrue(parser.parse("or(true, false)").evaluateAsBoolean(context));
        Assert.assertTrue(parser.parse("or(true, true)").evaluateAsBoolean(context));

        Assert.assertFalse(parser.parse("or(false, false, false)").evaluateAsBoolean(context));
        Assert.assertTrue(parser.parse("or(false, false, true)").evaluateAsBoolean(context));
        Assert.assertTrue(parser.parse("or(false, true, false)").evaluateAsBoolean(context));
        Assert.assertTrue(parser.parse("or(false, true, true)").evaluateAsBoolean(context));
        Assert.assertTrue(parser.parse("or(true, false, false)").evaluateAsBoolean(context));
        Assert.assertTrue(parser.parse("or(true, false, true)").evaluateAsBoolean(context));
        Assert.assertTrue(parser.parse("or(true, true, false)").evaluateAsBoolean(context));
        Assert.assertTrue(parser.parse("or(true, true, true)").evaluateAsBoolean(context));
    }

    @Test
    public void add_test() throws Expression.EvaluationException, Parser.ParsingException {
        Assert.assertEquals(3, parser.parse("add(1, 2)").evaluateAsInteger(context));
        Assert.assertEquals(-1, parser.parse("add(1, -2)").evaluateAsInteger(context));
        Assert.assertEquals(6, parser.parse("add(1, 2, 3)").evaluateAsInteger(context));
        Assert.assertEquals(0, parser.parse("add(1, 2, -3)").evaluateAsInteger(context));
        Assert.assertEquals(-4, parser.parse("add(1, -2, -3)").evaluateAsInteger(context));

        Assert.assertEquals(3, parser.parse("add(1, 2)").evaluateAsDecimal(context), 0.000001);
        Assert.assertEquals(-1, parser.parse("add(1, -2)").evaluateAsDecimal(context), 0.000001);
        Assert.assertEquals(6, parser.parse("add(1, 2, 3)").evaluateAsDecimal(context), 0.000001);
        Assert.assertEquals(0, parser.parse("add(1, 2, -3)").evaluateAsDecimal(context), 0.000001);
        Assert.assertEquals(-4, parser.parse("add(1, -2, -3)").evaluateAsDecimal(context), 0.000001);
    }

    @Test
    public void sub_test() throws Expression.EvaluationException, Parser.ParsingException {
        Assert.assertEquals(-1, parser.parse("sub(1, 2)").evaluateAsInteger(context));
        Assert.assertEquals(3, parser.parse("sub(1, -2)").evaluateAsInteger(context));

        Assert.assertEquals(-1, parser.parse("sub(1, 2)").evaluateAsDecimal(context), 0.000001);
        Assert.assertEquals(3, parser.parse("sub(1, -2)").evaluateAsDecimal(context), 0.000001);
    }

    @Test
    public void mul_test() throws Expression.EvaluationException, Parser.ParsingException {
        Assert.assertEquals(6, parser.parse("mul(2, 3)").evaluateAsInteger(context));
        Assert.assertEquals(-6, parser.parse("mul(2, -3)").evaluateAsInteger(context));
        Assert.assertEquals(24, parser.parse("mul(2, 3, 4)").evaluateAsInteger(context));
        Assert.assertEquals(-24, parser.parse("mul(2, 3, -4)").evaluateAsInteger(context));

        Assert.assertEquals(6, parser.parse("mul(2, 3)").evaluateAsDecimal(context), 0.000001);
        Assert.assertEquals(-6, parser.parse("mul(2, -3)").evaluateAsDecimal(context), 0.000001);
        Assert.assertEquals(24, parser.parse("mul(2, 3, 4)").evaluateAsDecimal(context), 0.000001);
        Assert.assertEquals(-24, parser.parse("mul(2, 3, -4)").evaluateAsDecimal(context), 0.000001);
    }

    @Test
    public void div_test() throws Expression.EvaluationException, Parser.ParsingException {
        Assert.assertEquals(3, parser.parse("div(6, 2)").evaluateAsInteger(context));
        Assert.assertEquals(-3, parser.parse("div(6, -2)").evaluateAsInteger(context));
        Assert.assertEquals(1, parser.parse("div(6, 4)").evaluateAsInteger(context));

        Assert.assertEquals(3, parser.parse("div(6, 2)").evaluateAsDecimal(context), 0.000001);
        Assert.assertEquals(-3, parser.parse("div(6, -2)").evaluateAsDecimal(context), 0.000001);
        Assert.assertEquals(1.5, parser.parse("div(6, 4)").evaluateAsDecimal(context), 0.000001);
    }

    @Test
    public void mod_test() throws Expression.EvaluationException, Parser.ParsingException {
        Assert.assertEquals(0, parser.parse("mod(6, 2)").evaluateAsInteger(context));
        Assert.assertEquals(1, parser.parse("mod(5, 2)").evaluateAsInteger(context));

        Assert.assertEquals(0, parser.parse("mod(6, 2)").evaluateAsDecimal(context), 0.000001);
        Assert.assertEquals(1, parser.parse("mod(5, 2)").evaluateAsDecimal(context), 0.000001);
    }

    @Test
    public void starts_test() throws Expression.EvaluationException, Parser.ParsingException {
        Assert.assertTrue(parser.parse("starts('abcdef', '')").evaluateAsBoolean(context));
        Assert.assertTrue(parser.parse("starts('abcdef', 'a')").evaluateAsBoolean(context));
        Assert.assertTrue(parser.parse("starts('abcdef', 'ab')").evaluateAsBoolean(context));
        Assert.assertFalse(parser.parse("starts('abcdef', 'b')").evaluateAsBoolean(context));
        Assert.assertFalse(parser.parse("starts('abcdef', 'bc')").evaluateAsBoolean(context));
    }

    @Test
    public void ends_test() throws Expression.EvaluationException, Parser.ParsingException {
        Assert.assertTrue(parser.parse("ends('abcdef', '')").evaluateAsBoolean(context));
        Assert.assertTrue(parser.parse("ends('abcdef', 'f')").evaluateAsBoolean(context));
        Assert.assertTrue(parser.parse("ends('abcdef', 'ef')").evaluateAsBoolean(context));
        Assert.assertFalse(parser.parse("ends('abcdef', 'e')").evaluateAsBoolean(context));
        Assert.assertFalse(parser.parse("ends('abcdef', 'de')").evaluateAsBoolean(context));
    }

    @Test
    public void in_test() throws Expression.EvaluationException, Parser.ParsingException {
        Assert.assertTrue(parser.parse("in('abcdef', '')").evaluateAsBoolean(context));
        Assert.assertTrue(parser.parse("in('abcdef', 'a')").evaluateAsBoolean(context));
        Assert.assertTrue(parser.parse("in('abcdef', 'ab')").evaluateAsBoolean(context));
        Assert.assertTrue(parser.parse("in('abcdef', 'b')").evaluateAsBoolean(context));
        Assert.assertTrue(parser.parse("in('abcdef', 'bc')").evaluateAsBoolean(context));
        Assert.assertTrue(parser.parse("in('abcdef', 'e')").evaluateAsBoolean(context));
        Assert.assertTrue(parser.parse("in('abcdef', 'ef')").evaluateAsBoolean(context));
        Assert.assertFalse(parser.parse("in('abcdef', 'ac')").evaluateAsBoolean(context));
    }

    @Test
    public void substr_test() throws Expression.EvaluationException, Parser.ParsingException {
        Assert.assertEquals("abcdef", parser.parse("substr('abcdef', 0, 6)").evaluateAsText(context));
        Assert.assertEquals("abcde", parser.parse("substr('abcdef', 0, 5)").evaluateAsText(context));
        Assert.assertEquals("bcdef", parser.parse("substr('abcdef', 1, 6)").evaluateAsText(context));
        Assert.assertEquals("abcdef", parser.parse("substr('abcdef', 0, -1)").evaluateAsText(context));
        Assert.assertEquals("ef", parser.parse("substr('abcdef', -3, -1)").evaluateAsText(context));
        Assert.assertEquals("f", parser.parse("substr('abcdef', -2, -1)").evaluateAsText(context));
    }

    @Test
    public void substrl_test() throws Expression.EvaluationException, Parser.ParsingException {
        Assert.assertEquals("abcdef", parser.parse("substrl('abcdef', 0, 6)").evaluateAsText(context));
        Assert.assertEquals("abcde", parser.parse("substrl('abcdef', 0, 5)").evaluateAsText(context));
        Assert.assertEquals("bcdef", parser.parse("substrl('abcdef', 1, 5)").evaluateAsText(context));
        Assert.assertEquals("f", parser.parse("substrl('abcdef', 5, 1)").evaluateAsText(context));
        Assert.assertEquals("f", parser.parse("substrl('abcdef', -2, 1)").evaluateAsText(context));
        Assert.assertEquals("ef", parser.parse("substrl('abcdef', -3, 2)").evaluateAsText(context));
    }

    @Test
    public void len_test() throws Expression.EvaluationException, Parser.ParsingException {
        Assert.assertEquals(6, parser.parse("len('abcdef')").evaluateAsInteger(context));
        Assert.assertEquals(5, parser.parse("len('abcde')").evaluateAsInteger(context));
        Assert.assertEquals(0, parser.parse("len('')").evaluateAsInteger(context));

        Assert.assertEquals(6, parser.parse("len('abcdef')").evaluateAsDecimal(context), 0.000001);
        Assert.assertEquals(5, parser.parse("len('abcde')").evaluateAsDecimal(context), 0.000001);
        Assert.assertEquals(0, parser.parse("len('')").evaluateAsDecimal(context), 0.000001);
    }

    @Test
    public void eq_test() throws Expression.EvaluationException, Parser.ParsingException {
        Assert.assertTrue(parser.parse("eq(true, true)").evaluateAsBoolean(context));
        Assert.assertFalse(parser.parse("eq(true, false)").evaluateAsBoolean(context));
        Assert.assertTrue(parser.parse("eq(1, 1)").evaluateAsBoolean(context));
        Assert.assertFalse(parser.parse("eq(1, 2)").evaluateAsBoolean(context));
        Assert.assertTrue(parser.parse("eq(1, 1)").evaluateAsBoolean(context));
        Assert.assertFalse(parser.parse("eq(1, 2)").evaluateAsBoolean(context));
        Assert.assertTrue(parser.parse("eq('abcdef', 'abcdef')").evaluateAsBoolean(context));
        Assert.assertFalse(parser.parse("eq('abcdef', 'abcd')").evaluateAsBoolean(context));
    }

    @Test
    public void neq_test() throws EvaluationException, Parser.ParsingException {
        Assert.assertFalse(parser.parse("neq(true, true)").evaluateAsBoolean(context));
        Assert.assertTrue(parser.parse("neq(true, false)").evaluateAsBoolean(context));
        Assert.assertFalse(parser.parse("neq(1, 1)").evaluateAsBoolean(context));
        Assert.assertTrue(parser.parse("neq(1, 2)").evaluateAsBoolean(context));
        Assert.assertFalse(parser.parse("neq(1, 1)").evaluateAsBoolean(context));
        Assert.assertTrue(parser.parse("neq(1, 2)").evaluateAsBoolean(context));
        Assert.assertFalse(parser.parse("neq('abcdef', 'abcdef')").evaluateAsBoolean(context));
        Assert.assertTrue(parser.parse("neq('abcdef', 'abcd')").evaluateAsBoolean(context));
    }

    @Test
    public void lt_test() throws EvaluationException, Parser.ParsingException {
        Assert.assertTrue(parser.parse("lt(1, 2)").evaluateAsBoolean(context));
        Assert.assertFalse(parser.parse("lt(2, 2)").evaluateAsBoolean(context));
        Assert.assertFalse(parser.parse("lt(3, 2)").evaluateAsBoolean(context));
        Assert.assertTrue(parser.parse("lt(1, 2)").evaluateAsBoolean(context));
        Assert.assertFalse(parser.parse("lt(2, 2)").evaluateAsBoolean(context));
        Assert.assertFalse(parser.parse("lt(3, 2)").evaluateAsBoolean(context));
    }

    @Test
    public void lte_test() throws EvaluationException, Parser.ParsingException {
        Assert.assertTrue(parser.parse("lte(1, 2)").evaluateAsBoolean(context));
        Assert.assertTrue(parser.parse("lte(2, 2)").evaluateAsBoolean(context));
        Assert.assertFalse(parser.parse("lte(3, 2)").evaluateAsBoolean(context));
        Assert.assertTrue(parser.parse("lte(1, 2)").evaluateAsBoolean(context));
        Assert.assertTrue(parser.parse("lte(2, 2)").evaluateAsBoolean(context));
        Assert.assertFalse(parser.parse("lte(3, 2)").evaluateAsBoolean(context));
    }

    @Test
    public void gt_test() throws EvaluationException, Parser.ParsingException {
        Assert.assertFalse(parser.parse("gt(1, 2)").evaluateAsBoolean(context));
        Assert.assertFalse(parser.parse("gt(2, 2)").evaluateAsBoolean(context));
        Assert.assertTrue(parser.parse("gt(3, 2)").evaluateAsBoolean(context));
        Assert.assertFalse(parser.parse("gt(1, 2)").evaluateAsBoolean(context));
        Assert.assertFalse(parser.parse("gt(2, 2)").evaluateAsBoolean(context));
        Assert.assertTrue(parser.parse("gt(3, 2)").evaluateAsBoolean(context));
    }

    @Test
    public void gte_test() throws EvaluationException, Parser.ParsingException {
        Assert.assertFalse(parser.parse("gte(1, 2)").evaluateAsBoolean(context));
        Assert.assertTrue(parser.parse("gte(2, 2)").evaluateAsBoolean(context));
        Assert.assertTrue(parser.parse("gte(3, 2)").evaluateAsBoolean(context));
        Assert.assertFalse(parser.parse("gte(1, 2)").evaluateAsBoolean(context));
        Assert.assertTrue(parser.parse("gte(2, 2)").evaluateAsBoolean(context));
        Assert.assertTrue(parser.parse("gte(3, 2)").evaluateAsBoolean(context));
    }

    @Test
    public void min_test() throws Expression.EvaluationException, Parser.ParsingException {
        Assert.assertEquals(0, parser.parse("min(0, 1)").evaluateAsInteger(context));
        Assert.assertEquals(-1, parser.parse("min(0, -1)").evaluateAsInteger(context));
        Assert.assertEquals(-5, parser.parse("min(0, -1, -5)").evaluateAsInteger(context));

        Assert.assertEquals(0, parser.parse("min(0, 1)").evaluateAsDecimal(context), 0.000001);
        Assert.assertEquals(-1, parser.parse("min(0, -1)").evaluateAsDecimal(context), 0.000001);
        Assert.assertEquals(-5, parser.parse("min(0, -1, -5)").evaluateAsDecimal(context), 0.000001);
    }

    @Test
    public void max_test() throws Expression.EvaluationException, Parser.ParsingException {
        Assert.assertEquals(1, parser.parse("max(0, 1)").evaluateAsInteger(context));
        Assert.assertEquals(0, parser.parse("max(0, -1)").evaluateAsInteger(context));
        Assert.assertEquals(5, parser.parse("max(0, -1, 5)").evaluateAsInteger(context));

        Assert.assertEquals(1, parser.parse("max(0, 1)").evaluateAsDecimal(context), 0.000001);
        Assert.assertEquals(0, parser.parse("max(0, -1)").evaluateAsDecimal(context), 0.000001);
        Assert.assertEquals(5, parser.parse("max(0, -1, 5)").evaluateAsDecimal(context), 0.000001);
    }

    @Test
    public void var_test() throws EvaluationException, Parser.ParsingException {
        context.setVariable("variable-boolean", true);
        context.setVariable("variable-integer", 1);
        context.setVariable("variable-decimal", 1.5);
        context.setVariable("variable-text", "abcdef");

        Assert.assertTrue(parser.parse("var('variable-boolean')").evaluateAsBoolean(context));
        Assert.assertEquals(1, parser.parse("var('variable-integer')").evaluateAsInteger(context));
        Assert.assertEquals(1.5, parser.parse("var('variable-decimal')").evaluateAsDecimal(context), 0.000001);
        Assert.assertEquals("abcdef", parser.parse("var('variable-text')").evaluateAsText(context));
    }

    @Test
    public void cond_test() throws EvaluationException, Parser.ParsingException {
        Assert.assertTrue(parser.parse("cond(true, true, false)").evaluateAsBoolean(context));
        Assert.assertFalse(parser.parse("cond(false, true, false)").evaluateAsBoolean(context));

        Assert.assertEquals(1, parser.parse("cond(true, 1, 2)").evaluateAsInteger(context));
        Assert.assertEquals(2, parser.parse("cond(false, 1, 2)").evaluateAsInteger(context));

        Assert.assertEquals(1.5, parser.parse("cond(true, 1.5, 2.5)").evaluateAsDecimal(context), 0.000001);
        Assert.assertEquals(2.5, parser.parse("cond(false, 1.5, 2.5)").evaluateAsDecimal(context), 0.000001);

        Assert.assertEquals("a", parser.parse("cond(true, 'a', 'b')").evaluateAsText(context));
        Assert.assertEquals("b", parser.parse("cond(false, 'a', 'b')").evaluateAsText(context));
    }

    @Test
    public void concat_test() throws EvaluationException, Parser.ParsingException {
        Assert.assertEquals("abcdef", parser.parse("concat('abc', 'def')").evaluateAsText(context));
        Assert.assertEquals("abc", parser.parse("concat('abc', '')").evaluateAsText(context));
        Assert.assertEquals("def", parser.parse("concat('', 'def')").evaluateAsText(context));

        Assert.assertEquals("abcdefghi", parser.parse("concat('abc', 'def', 'ghi')").evaluateAsText(context));
    }

    @Test
    public void complex_expression_test() throws EvaluationException, Parser.ParsingException {
        Assert.assertEquals("abcdef", parser.parse("cond(and(true, true), 'abcdef', '')").evaluateAsText(context));
        Assert.assertEquals("", parser.parse("cond(and(true, false), 'abcdef', '')").evaluateAsText(context));

        Assert.assertEquals(3, parser.parse("len(substr('test', 0, sub(len('test'), 1)))").evaluateAsInteger(context));
        Assert.assertEquals(3, parser.parse("len(substr('test', add(0, 1), -1))").evaluateAsInteger(context));
    }

    @Test
    public void text_literal_escape_test() throws EvaluationException, Parser.ParsingException {
        Assert.assertEquals("add(1, 2)", parser.parse("'add(1, 2)'").evaluateAsText(context));
        Assert.assertEquals(9, parser.parse("len('add(1, 2)')").evaluateAsInteger(context));
    }

    @Test
    public void invalid_syntax_produces_parsing_error() {
        Assert.assertThrows(Parser.ParsingException.class, () -> parser.parse("'test"));
        Assert.assertThrows(Parser.ParsingException.class, () -> parser.parse("test'"));
        Assert.assertThrows(Parser.ParsingException.class, () -> parser.parse("("));
        Assert.assertThrows(Parser.ParsingException.class, () -> parser.parse("(("));
        Assert.assertThrows(Parser.ParsingException.class, () -> parser.parse(")"));
        Assert.assertThrows(Parser.ParsingException.class, () -> parser.parse("))"));
        Assert.assertThrows(Parser.ParsingException.class, () -> parser.parse("()"));
        Assert.assertThrows(Parser.ParsingException.class, () -> parser.parse("i(2)"));
        Assert.assertThrows(Parser.ParsingException.class, () -> parser.parse("42a"));
        Assert.assertThrows(Parser.ParsingException.class, () -> parser.parse("add(1,"));
        Assert.assertThrows(Parser.ParsingException.class, () -> parser.parse("add(1, 2"));
        Assert.assertThrows(Parser.ParsingException.class, () -> parser.parse("42'test'"));
        Assert.assertThrows(Parser.ParsingException.class, () -> parser.parse("'test'42"));
        Assert.assertThrows(Parser.ParsingException.class, () -> parser.parse("not(true, true)"));
        Assert.assertThrows(Parser.ParsingException.class, () -> parser.parse("and(true)"));
    }
}
