package net.akarah.sql4j.value.expr;

public interface IntoExpression<T> {
    Expression<T> intoExpression();
}
