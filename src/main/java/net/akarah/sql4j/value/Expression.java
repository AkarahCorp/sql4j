package net.akarah.sql4j.value;

import net.akarah.sql4j.SqlConvertible;
import net.akarah.sql4j.table.Column;
import net.akarah.sql4j.table.Table;

import java.util.List;

public interface Expression<T> extends SqlConvertible, IntoExpression<T> {
    default Expression<T> intoExpression() {
        return this;
    }

    static Expression<Integer> of(int value) {
        return () -> Integer.toString(value);
    }

    static Expression<String> of(String value) {
        return () -> '"' + value + '"';
    }

    static <T1, T2> Expression<Tuple.Of2<T1, T2>> of(IntoExpression<T1> a, IntoExpression<T2> b) {
        return new TupleExpr<>(List.of(a.intoExpression(), b.intoExpression()));
    }

    static <T1, T2, T3> Expression<Tuple.Of3<T1, T2, T3>> of(IntoExpression<T1> a, IntoExpression<T2> b, IntoExpression<T3> c) {
        return new TupleExpr<>(List.of(a.intoExpression(), b.intoExpression(), c.intoExpression()));
    }

    static <T> Expression<Column<T>> of(Column<T> column) {
        return column::tabledName;
    }

    static <T> Expression<Table> of(Table table) {
        return table::name;
    }

    default Expression<T> add(Expression<T> other) {
        var value = this;
        return () -> value.toSql() + " + " + other.toSql();
    }

    record TupleExpr<T extends Tuple>(List<Expression<?>> expressions) implements Expression<T> {
        @Override
        public String toSql() {
            var sb = new StringBuilder();
            sb.append("(");
            for(var expr : expressions) {
                sb.append(expr.toSql());
                sb.append(",");
            }
            sb.append(")");
            return sb.toString();
        }
    }
}
