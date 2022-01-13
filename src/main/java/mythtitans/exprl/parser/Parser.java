package mythtitans.exprl.parser;

import mythtitans.exprl.eval.Expression;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static mythtitans.exprl.eval.impl.AddExpression.add;
import static mythtitans.exprl.eval.impl.AndExpression.and;
import static mythtitans.exprl.eval.impl.ConcatExpression.concat;
import static mythtitans.exprl.eval.impl.CondExpression.cond;
import static mythtitans.exprl.eval.impl.DebugExpression.debug;
import static mythtitans.exprl.eval.impl.DivExpression.div;
import static mythtitans.exprl.eval.impl.EndsExpression.ends;
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

public class Parser {

    public static final String NOT_EXPRESSION = "not";
    public static final String AND_EXPRESSION = "and";
    public static final String OR_EXPRESSION = "or";
    public static final String ADD_EXPRESSION = "add";
    public static final String SUB_EXPRESSION = "sub";
    public static final String MUL_EXPRESSION = "mul";
    public static final String DIV_EXPRESSION = "div";
    public static final String MOD_EXPRESSION = "mod";
    public static final String STARTS_EXPRESSION = "starts";
    public static final String ENDS_EXPRESSION = "ends";
    public static final String IN_EXPRESSION = "in";
    public static final String SUBSTR_EXPRESSION = "substr";
    public static final String SUBSTRL_EXPRESSION = "substrl";
    public static final String LEN_EXPRESSION = "len";
    public static final String EQ_EXPRESSION = "eq";
    public static final String NEQ_EXPRESSION = "neq";
    public static final String LT_EXPRESSION = "lt";
    public static final String LTE_EXPRESSION = "lte";
    public static final String GT_EXPRESSION = "gt";
    public static final String GTE_EXPRESSION = "gte";
    public static final String MIN_EXPRESSION = "min";
    public static final String MAX_EXPRESSION = "max";
    public static final String VAR_EXPRESSION = "var";
    public static final String COND_EXPRESSION = "cond";
    public static final String CONCAT_EXPRESSION = "concat";
    public static final String DEBUG_EXPRESSION = "debug";

    public static final Pattern LITERAL_VARIABLE_PATTERN = Pattern.compile("^[a-zA-z][\\w.-]*$");

    public Expression parse(final String expression) throws ParsingException {
        TokenNode AST = buildAST(expression);
        return parseExpression(AST);
    }

    private TokenNode buildAST(final String expression) throws ParsingException {
        // Abstract root node to simplify building the tree
        TokenNode ast = new TokenNode("");
        TokenNode currentNode = ast;
        boolean interpret = true;

        StringBuilder tokenBuilder = new StringBuilder();
        for (int i = 0; i < expression.length(); ++i) {
            char currentCharacter = expression.charAt(i);

            if (interpret) {
                switch (currentCharacter) {
                    case '(':
                        TokenNode childNode = currentNode.addChild(tokenBuilder.toString());
                        if (childNode == currentNode) {
                            throw ParsingException.unexpectedSymbol('(', i);
                        }
                        currentNode = childNode;
                        tokenBuilder = new StringBuilder();
                        break;

                    case ')':
                        currentNode.addChild(tokenBuilder.toString());
                        currentNode = currentNode.getParent();
                        if (currentNode == null) {
                            throw ParsingException.unexpectedSymbol(')', i);
                        }
                        tokenBuilder = new StringBuilder();
                        break;

                    case ',':
                        currentNode.addChild(tokenBuilder.toString());
                        tokenBuilder = new StringBuilder();
                        break;

                    case ' ':
                        break;

                    case '\'':
                        interpret = false;
                    default:
                        tokenBuilder.append(expression.charAt(i));
                        break;
                }
            } else {
                if (currentCharacter == '\'') {
                    interpret = true;
                }

                tokenBuilder.append(expression.charAt(i));
            }
        }

        currentNode.addChild(tokenBuilder.toString());

        if (ast != currentNode) {
            throw new ParsingException("Incomplete expression.");
        }

        // Removes abstract root node
        return ast.getChildren().get(0);
    }

    private Expression parseExpression(final TokenNode ast) throws ParsingException {
        List<Expression> arguments = new ArrayList<>();

        List<TokenNode> children = ast.getChildren();
        for (TokenNode child : children) {
            arguments.add(parseExpression(child));
        }

        if (!arguments.isEmpty()) {
            return parseExpression(ast.getToken(), arguments);
        }

        return parseLiteral(ast.getToken());
    }

    private Expression parseExpression(final String expression, final List<Expression> arguments) throws ParsingException {
        return switch (expression) {
            case NOT_EXPRESSION, LEN_EXPRESSION, VAR_EXPRESSION -> parseUnaryExpression(expression, arguments);
            case EQ_EXPRESSION, NEQ_EXPRESSION, LT_EXPRESSION, LTE_EXPRESSION, GT_EXPRESSION, GTE_EXPRESSION, SUB_EXPRESSION, DIV_EXPRESSION, MOD_EXPRESSION, STARTS_EXPRESSION, ENDS_EXPRESSION, IN_EXPRESSION, DEBUG_EXPRESSION -> parseBinaryExpression(
                    expression,
                    arguments
            );
            case AND_EXPRESSION, OR_EXPRESSION, ADD_EXPRESSION, MUL_EXPRESSION, MIN_EXPRESSION, MAX_EXPRESSION, CONCAT_EXPRESSION -> parseBiOrNaryExpression(
                    expression,
                    arguments
            );
            case SUBSTR_EXPRESSION, SUBSTRL_EXPRESSION, COND_EXPRESSION -> parseTernaryExpression(expression, arguments);
            default -> throw new ParsingException(String.format("Unrecognized expression [%s].", expression));
        };
    }

    private Expression parseLiteral(final String expression) throws ParsingException {
        boolean startsAsText = expression.startsWith("'");
        boolean endsAsText = expression.endsWith("'");
        if (startsAsText || endsAsText) {
            if (!(startsAsText && endsAsText)) {
                throw new ParsingException(String.format("Invalid text literal [%s].", expression));
            }

            return literal(expression.substring(1, expression.length() - 1));
        }

        if (expression.equals("true") || expression.equals("false")) {
            return literal(Boolean.parseBoolean(expression));
        }

        try {
            return literal(Long.parseLong(expression));
        } catch (NumberFormatException e) {
            // Ignored
        }

        try {
            return literal(Double.parseDouble(expression));
        } catch (NumberFormatException e) {
            // Ignored
        }

        if (LITERAL_VARIABLE_PATTERN.matcher(expression).matches()) {
            return var(literal(expression));
        }

        throw new ParsingException(String.format("Invalid literal [%s].", expression));
    }

    private Expression parseUnaryExpression(final String expressionName, final List<Expression> arguments) throws ParsingException {
        if (arguments.size() != 1) {
            throw ParsingException.invalidArgumentsCount(expressionName, 1, arguments.size());
        }

        Expression arg = arguments.get(0);

        return switch (expressionName) {
            case NOT_EXPRESSION -> not(arg);
            case LEN_EXPRESSION -> len(arg);
            case VAR_EXPRESSION -> var(arg);
            default -> throw new ParsingException(String.format("Unrecognized expression [%s].", expressionName));
        };
    }

    private Expression parseBinaryExpression(final String expressionName, final List<Expression> arguments) throws ParsingException {
        if (arguments.size() != 2) {
            throw ParsingException.invalidArgumentsCount(expressionName, 2, arguments.size());
        }

        Expression argA = arguments.get(0);
        Expression argB = arguments.get(1);

        return switch (expressionName) {
            case SUB_EXPRESSION -> sub(argA, argB);
            case DIV_EXPRESSION -> div(argA, argB);
            case MOD_EXPRESSION -> mod(argA, argB);
            case STARTS_EXPRESSION -> starts(argA, argB);
            case ENDS_EXPRESSION -> ends(argA, argB);
            case IN_EXPRESSION -> in(argA, argB);
            case EQ_EXPRESSION -> eq(argA, argB);
            case NEQ_EXPRESSION -> neq(argA, argB);
            case LT_EXPRESSION -> lt(argA, argB);
            case LTE_EXPRESSION -> lte(argA, argB);
            case GT_EXPRESSION -> gt(argA, argB);
            case GTE_EXPRESSION -> gte(argA, argB);
            case DEBUG_EXPRESSION -> debug(argA, argB);
            default -> throw new ParsingException(String.format("Unrecognized expression [%s].", expressionName));
        };
    }

    private Expression parseBiOrNaryExpression(final String expressionName, final List<Expression> arguments) throws ParsingException {
        if (arguments.size() < 2) {
            throw new ParsingException(String.format(
                    "Invalid arguments count for [%s], expected at least [%d] but got [%d].",
                    expressionName,
                    2,
                    arguments.size()
            ));
        }

        Expression argA = arguments.get(0);
        Expression argB = arguments.get(1);
        Expression[] args = arguments.subList(2, arguments.size()).toArray(new Expression[0]);

        return switch (expressionName) {
            case AND_EXPRESSION -> and(argA, argB, args);
            case OR_EXPRESSION -> or(argA, argB, args);
            case ADD_EXPRESSION -> add(argA, argB, args);
            case MUL_EXPRESSION -> mul(argA, argB, args);
            case MIN_EXPRESSION -> min(argA, argB, args);
            case MAX_EXPRESSION -> max(argA, argB, args);
            case CONCAT_EXPRESSION -> concat(argA, argB, args);
            default -> throw new ParsingException(String.format("Unrecognized expression [%s].", expressionName));
        };
    }

    private Expression parseTernaryExpression(final String expressionName, final List<Expression> arguments) throws ParsingException {
        if (arguments.size() != 3) {
            throw ParsingException.invalidArgumentsCount(expressionName, 3, arguments.size());
        }

        Expression argA = arguments.get(0);
        Expression argB = arguments.get(1);
        Expression argC = arguments.get(2);

        return switch (expressionName) {
            case SUBSTR_EXPRESSION -> substr(argA, argB, argC);
            case SUBSTRL_EXPRESSION -> substrl(argA, argB, argC);
            case COND_EXPRESSION -> cond(argA, argB, argC);
            default -> throw new ParsingException(String.format("Unrecognized expression [%s].", expressionName));
        };
    }

    public static class ParsingException extends Exception {

        public ParsingException(final String message) {
            super(message);
        }

        public static ParsingException invalidArgumentsCount(final String token, final int expectedCount, final int actualCount) {
            return new ParsingException(String.format(
                    "Invalid arguments count for [%s], expected [%d] but got [%d].",
                    token,
                    expectedCount,
                    actualCount
            ));
        }

        public static ParsingException unexpectedSymbol(final char symbol, final int index) {
            return new ParsingException(String.format("Unexpected symbol [%s] at index [%d].", symbol, index));
        }
    }

    private static class TokenNode {

        private final String token;
        private final List<TokenNode> children;
        private final TokenNode parent;

        public TokenNode(final String token) {
            this(token, null);
        }

        private TokenNode(final String token, final TokenNode parent) {
            this.token = token;
            this.children = new ArrayList<>();
            this.parent = parent;
        }

        public TokenNode addChild(final String token) {
            if (token.isEmpty()) {
                return this;
            }

            TokenNode child = new TokenNode(token, this);
            children.add(child);
            return child;
        }

        public String getToken() {
            return token;
        }

        public List<TokenNode> getChildren() {
            return children;
        }

        public TokenNode getParent() {
            return parent;
        }
    }
}
