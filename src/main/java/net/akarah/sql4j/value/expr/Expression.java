package net.akarah.sql4j.value.expr;

import net.akarah.sql4j.SqlConvertible;

import java.util.List;
import java.util.stream.Stream;

public interface Expression<T> extends SqlConvertible, IntoExpression<T> {
    default Expression<T> intoExpression() {
        return this;
    }

    default String column() { return "?column?"; }

    default Expression<T> add(Expression<T> other) {
        return () -> this.toSql() + " + " + other.toSql();
    }

    default Expression<T> sub(Expression<T> other) {
        return () -> this.toSql() + " - " + other.toSql();
    }

    default Expression<Boolean> equals(Expression<T> other) {
        return () -> this.toSql() + " = " + other.toSql();
    }

    default Expression<Boolean> notEquals(Expression<T> other) {
        return () -> this.toSql() + " <> " + other.toSql();
    }

    default Expression<Boolean> greaterThan(Expression<T> other) {
        return () -> this.toSql() + " > " + other.toSql();
    }

    default Expression<Boolean> lessThan(Expression<T> other) {
        return () -> this.toSql() + " < " + other.toSql();
    }

    default Expression<Boolean> greaterThanOrEquals(Expression<T> other) {
        return () -> this.toSql() + " >= " + other.toSql();
    }

    default Expression<Boolean> lessThanOrEquals(Expression<T> other) {
        return () -> this.toSql() + " <= " + other.toSql();
    }
}
