package net.akarah.sql4j.value.expr;

import net.akarah.sql4j.table.Column;

public class Functions {
    public static <T> Value<Long> count(Column<T> value) {
        return Values.ofFunctions(
                () -> "COUNT(" + value.valueSql() + ")",
                () -> "count"
        );
    }

    public static <T> Value<Long> sum(Column<T> value) {
        return Values.ofFunctions(
                () -> "SUM(" + value.valueSql() + ")",
                () -> "sum"
        );
    }

    public static <T extends Number> Value<T> abs(Value<T> value) {
        return Values.ofFunctions(
                () -> "abs(" + value.valueSql() + ")",
                () -> "abs"
        );
    }

    public static <T extends Number> Value<T> sqrt(Value<T> value) {
        return Values.ofFunctions(
                () -> "sqrt(" + value.valueSql() + ")",
                () -> "sqrt"
        );
    }

    public static <T extends Number> Value<T> ceil(Value<T> value) {
        return Values.ofFunctions(
                () -> "ceil(" + value.valueSql() + ")",
                () -> "ceil"
        );
    }

    public static <T extends Number> Value<T> floor(Value<T> value) {
        return Values.ofFunctions(
                () -> "floor(" + value.valueSql() + ")",
                () -> "floor"
        );
    }

    public static <T extends Number> Value<T> degrees(Value<T> value) {
        return Values.ofFunctions(
                () -> "degrees(" + value.valueSql() + ")",
                () -> "degrees"
        );
    }

    public static <T extends Number> Value<T> truncate(Value<T> value, Value<Integer> bits) {
        return Values.ofFunctions(
                () -> "trunc(" + value.valueSql() + ", " + bits.valueSql() + ")",
                () -> "trunc"
        );
    }

    public static Value<Double> random() {
        return Values.ofFunctions(
                () -> "random()",
                () -> "random"
        );
    }

    public static <T extends Number> Value<T> exp(Value<T> value) {
        return Values.ofFunctions(
                () -> "exp(" + value.toSql() + ")",
                () -> "exp"
        );
    }
}
