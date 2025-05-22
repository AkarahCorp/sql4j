package net.akarah.sql4j.value;

public interface IntoExpression<T> {
    Expression<T> intoExpression();
}
