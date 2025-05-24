package net.akarah.sql4j.value.expr;

import net.akarah.sql4j.SqlConvertible;

public interface Value<T> extends SqlConvertible {
    default String column() { return "?column?"; }

    default Value<T> add(Value<T> other) {
        return () -> this.toSql() + " + " + other.toSql();
    }

    default Value<T> sub(Value<T> other) {
        return () -> this.toSql() + " - " + other.toSql();
    }

    default Value<Boolean> equals(Value<T> other) {
        return () -> this.toSql() + " = " + other.toSql();
    }

    default Value<Boolean> notEquals(Value<T> other) {
        return () -> this.toSql() + " <> " + other.toSql();
    }

    default Value<Boolean> greaterThan(Value<T> other) {
        return () -> this.toSql() + " > " + other.toSql();
    }

    default Value<Boolean> lessThan(Value<T> other) {
        return () -> this.toSql() + " < " + other.toSql();
    }

    default Value<Boolean> greaterThanOrEquals(Value<T> other) {
        return () -> this.toSql() + " >= " + other.toSql();
    }

    default Value<Boolean> lessThanOrEquals(Value<T> other) {
        return () -> this.toSql() + " <= " + other.toSql();
    }
}
