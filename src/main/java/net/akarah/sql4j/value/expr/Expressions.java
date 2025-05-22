package net.akarah.sql4j.value.expr;

import net.akarah.sql4j.table.Column;
import net.akarah.sql4j.table.Table;
import net.akarah.sql4j.value.tuple.Tuple;
import net.akarah.sql4j.value.tuple.TupleExpr;

import java.util.List;
import java.util.stream.Stream;

public final class Expressions {
    private Expressions() {}

    public static Expression<Integer> of(int value) {
        return position -> Integer.toString(value);
    }

    public static Expression<String> of(String value) {
        return position -> "'" + value + "'";
    }

    public static <T1, T2> Expression<Tuple.Of2<T1, T2>> of(IntoExpression<T1> a, IntoExpression<T2> b) {
        return new TupleExpr<>(List.of(a.intoExpression(), b.intoExpression()));
    }

    public static <T1, T2, T3> Expression<Tuple.Of3<T1, T2, T3>> of(IntoExpression<T1> a, IntoExpression<T2> b, IntoExpression<T3> c) {
        return new TupleExpr<>(List.of(a.intoExpression(), b.intoExpression(), c.intoExpression()));
    }

    public static <T> Expression<T> of(Column<T> column) {
        return new Expression<>() {
            @Override
            public String toSql(Position position) {
                return column.tabledName();
            }

            @Override
            public Stream<String> columns() {
                return Stream.of(column.name());
            }
        };
    }

    public static Expression<Table> of(Table table) {
        return position -> table.name();
    }
}
