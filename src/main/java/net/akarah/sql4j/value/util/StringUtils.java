package net.akarah.sql4j.value.util;

import java.util.List;
import java.util.function.Function;

public class StringUtils {
    public static <T> String parenthesizedValues(List<T> expressions, Function<T, String> stringFunction) {
        var sb = new StringBuilder();
        sb.append("(");

        sb.append(groupedValues(expressions, stringFunction));

        sb.append(")");
        return sb.toString();
    }

    public static <T> String groupedValues(List<T> expressions, Function<T, String> stringFunction) {
        var sb = new StringBuilder();

        for(int i = 0; i < expressions.size(); i++) {
            var expr = expressions.get(i);
            sb.append(stringFunction.apply(expr));
            if(i != expressions.size() - 1) {
                sb.append(",");
            }
        }

        return sb.toString();
    }
}
