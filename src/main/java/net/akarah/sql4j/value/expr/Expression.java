package net.akarah.sql4j.value.expr;

import net.akarah.sql4j.SqlConvertible;

import java.util.List;
import java.util.stream.Stream;

public interface Expression<T> extends SqlConvertible, IntoExpression<T> {
    default Expression<T> intoExpression() {
        return this;
    }

    default Stream<String> columns() { return Stream.of("?column?"); }

    @SuppressWarnings("unchecked")
    default T getValueFromList(List<Object> objects) { return (T) objects.getFirst(); }

    default Expression<T> add(Expression<T> other) {
        var value = this;
        return position -> value.toSql(position) + " + " + other.toSql(position);
    }
}
