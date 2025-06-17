package net.akarah.sql4j.value.expr;

import net.akarah.sql4j.SqlConvertible;

public interface Value<T> extends SqlConvertible {
    default String valueSql() { return this.toSql(); };

    default String column() { return "?column?"; }

    default Value<T> add(Value<T> other) {
        return () -> this.valueSql() + " + " + other.valueSql();
    }

    default Value<T> sub(Value<T> other) {
        return () -> this.valueSql() + " - " + other.valueSql();
    }

    default Value<Boolean> equals(Value<T> other) {
        return () -> this.valueSql() + " = " + other.valueSql();
    }

    default Value<Boolean> notEquals(Value<T> other) {
        return () -> this.valueSql() + " <> " + other.valueSql();
    }

    default Value<Boolean> greaterThan(Value<T> other) {
        return () -> this.valueSql() + " > " + other.valueSql();
    }

    default Value<Boolean> lessThan(Value<T> other) {
        return () -> this.valueSql() + " < " + other.valueSql();
    }

    default Value<Boolean> greaterThanOrEquals(Value<T> other) {
        return () -> this.valueSql() + " >= " + other.valueSql();
    }

    default Value<Boolean> lessThanOrEquals(Value<T> other) {
        return () -> this.valueSql() + " <= " + other.valueSql();
    }

    default Value<Boolean> like(Value<T> other) {
        return () -> this.valueSql() + " LIKE " + other.valueSql();
    }

    default Value<Boolean> caseInsensitiveLike(Value<T> other) {
        return () -> this.valueSql() + " ILIKE " + other.valueSql();
    }
}
