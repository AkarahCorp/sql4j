package net.akarah.sql4j.value.expr;

import net.akarah.sql4j.table.Column;
import net.akarah.sql4j.table.Table;
import net.akarah.sql4j.value.tuple.Tuple;
import net.akarah.sql4j.value.util.StringUtils;

import java.util.List;

public final class Values {
    private Values() {}

    public static Value<Integer> of(int value) {
        return () -> Integer.toString(value);
    }

    public static Value<String> of(String value) {
        return () -> "'" + value + "'";
    }

    public static <T1, T2> Value<Tuple.Of2<T1, T2>> of(IntoValue<T1> a, IntoValue<T2> b) {
        return () -> StringUtils.parenthesizedValues(
                List.of(a.intoValue(), b.intoValue()),
                Value::toSql
        );
    }

    public static <T1, T2, T3> Value<Tuple.Of3<T1, T2, T3>> of(IntoValue<T1> a, IntoValue<T2> b, IntoValue<T3> c) {
        return () -> StringUtils.parenthesizedValues(
                List.of(a.intoValue(), b.intoValue(), c.intoValue()),
                Value::toSql
        );
    }

    public static <T> Value<T> of(Column<T> column) {
        return new Value<>() {
            @Override
            public String toSql() {
                return column.name();
            }

            @Override
            public String column() {
                return column.name();
            }
        };
    }

    public static Value<Table> of(Table table) {
        return table::name;
    }
}
