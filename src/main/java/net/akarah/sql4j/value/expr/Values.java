package net.akarah.sql4j.value.expr;

import com.google.gson.JsonElement;
import net.akarah.sql4j.value.tuple.Tuple;
import net.akarah.sql4j.value.util.StringUtils;

import java.util.List;
import java.util.function.Supplier;

public final class Values {
    private Values() {}

    public static Value<Integer> of(int value) {
        return () -> Integer.toString(value);
    }

    public static Value<Long> of(long value) {
        return () -> Long.toString(value);
    }

    public static Value<String> of(String value) {
        return () -> "'" + value + "'";
    }

    public static Value<JsonElement> of(JsonElement jsonElement) {
        return () -> "'" + jsonElement.toString() + "'";
    }

    public static <T> StaticListValue<T> of(List<Value<T>> values) {
        return new StaticListValue<>(values);
    }

    public static <T1, T2> Value<Tuple.Of2<T1, T2>> of(Value<T1> a, Value<T2> b) {
        return () -> StringUtils.parenthesizedValues(
                List.of(a, b),
                Value::toSql
        );
    }

    public static <T1, T2, T3> Value<Tuple.Of3<T1, T2, T3>> of(Value<T1> a, Value<T2> b, Value<T3> c) {
        return () -> StringUtils.parenthesizedValues(
                List.of(a, b, c),
                Value::toSql
        );
    }

    public static <T> Value<T> ofFunctions(Supplier<String> toSqlFunc) {
        return toSqlFunc::get;
    }

    public static <T> Value<T> ofFunctions(Supplier<String> toSqlFunc, Supplier<String> column) {
        return new Value<>() {
            @Override
            public String toSql() {
                return toSqlFunc.get();
            }

            @Override
            public String column() {
                return column.get();
            }
        };
    }
}
