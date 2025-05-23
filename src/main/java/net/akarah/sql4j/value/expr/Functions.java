package net.akarah.sql4j.value.expr;

import net.akarah.sql4j.table.Column;

public class Functions {
    public static <T> Value<Long> count(Column<T> value) {
        return Values.of(
                () -> "COUNT(" + value.toSql() + ")",
                () -> "count"
        );
    }

    public static <T> Value<Long> sum(Column<T> value) {
        return Values.of(
                () -> "SUM(" + value.toSql() + ")",
                () -> "sum"
        );
    }

    public static <T extends Number> Value<T> abs(Value<T> value) {
        return Values.of(
                () -> "abs(" + value.toSql() + ")",
                () -> "abs"
        );
    }

    public static <T extends Number> Value<T> sqrt(Value<T> value) {
        return Values.of(
                () -> "sqrt(" + value.toSql() + ")",
                () -> "sqrt"
        );
    }

    public static <T extends Number> Value<T> ceil(Value<T> value) {
        return Values.of(
                () -> "ceil(" + value.toSql() + ")",
                () -> "ceil"
        );
    }

    public static <T extends Number> Value<T> floor(Value<T> value) {
        return Values.of(
                () -> "floor(" + value.toSql() + ")",
                () -> "floor"
        );
    }

    public static <T extends Number> Value<T> degrees(Value<T> value) {
        return Values.of(
                () -> "degrees(" + value.toSql() + ")",
                () -> "degrees"
        );
    }

    public static <T extends Number> Value<T> truncate(Value<T> value, Value<Integer> bits) {
        return Values.of(
                () -> "trunc(" + value.toSql() + ", " + bits.toSql() + ")",
                () -> "trunc"
        );
    }

    public static Value<Double> random() {
        return Values.of(
                () -> "random()",
                () -> "random"
        );
    }

    public static <T extends Number> Value<T> exp(Value<T> value) {
        return Values.of(
                () -> "exp(" + value.toSql() + ")",
                () -> "exp"
        );
    }
}
