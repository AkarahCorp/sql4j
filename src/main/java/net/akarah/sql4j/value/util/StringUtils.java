package net.akarah.sql4j.value.util;

import net.akarah.sql4j.SqlConvertible;
import net.akarah.sql4j.value.expr.Expression;

import java.util.List;
import java.util.function.Function;

public class StringUtils {
    public static <T> String parenthesizedValues(List<T> expressions, Function<T, String> stringFunction) {
        var sb = new StringBuilder();
        sb.append("(");

        for(int i = 0; i < expressions.size(); i++) {
            var expr = expressions.get(i);
            sb.append(stringFunction.apply(expr));
            if(i != expressions.size() - 1) {
                sb.append(",");
            }
        }

        sb.append(")");
        return sb.toString();
    }
}
