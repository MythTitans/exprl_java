package mythtitans.exprl.eval.impl;

import mythtitans.exprl.eval.Expression;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class ExpressionHelper {

    public static Set<String> getLiteralVariables(final Collection<Expression> expressions) {
        return expressions.stream().flatMap(expression -> expression.getLiteralVariables().stream()).collect(Collectors.toSet());
    }

    public static Set<String> getLiteralVariables(final Expression... expressions) {
        return getLiteralVariables(Arrays.stream(expressions).collect(Collectors.toList()));
    }
}
