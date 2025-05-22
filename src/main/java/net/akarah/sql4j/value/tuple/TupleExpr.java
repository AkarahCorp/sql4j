package net.akarah.sql4j.value.tuple;

import net.akarah.sql4j.SqlConvertible;
import net.akarah.sql4j.value.expr.Expression;
import net.akarah.sql4j.value.util.StringUtils;

import java.util.List;
import java.util.stream.Stream;

public record TupleExpr<T extends Tuple>(List<Expression<?>> expressions) implements Expression<T> {
    @Override
    public String toSql() {
        return StringUtils.parenthesizedValues(this.expressions, SqlConvertible::toSql);
    }
}
