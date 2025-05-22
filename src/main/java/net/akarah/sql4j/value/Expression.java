package net.akarah.sql4j.value;

import net.akarah.sql4j.SqlConvertible;
import net.akarah.sql4j.table.Column;
import net.akarah.sql4j.table.Table;
import org.postgresql.util.PGobject;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public interface Expression<T> extends SqlConvertible, IntoExpression<T> {
    default Expression<T> intoExpression() {
        return this;
    }
    default Stream<String> columns() { return Stream.of("?column?"); }

    @SuppressWarnings("unchecked")
    default T getValueFromList(List<Object> objects) { return (T) objects.getFirst(); }

    static Expression<Integer> of(int value) {
        return position -> Integer.toString(value);
    }

    static Expression<String> of(String value) {
        return position -> '"' + value + '"';
    }

    static <T1, T2> Expression<Tuple.Of2<T1, T2>> of(IntoExpression<T1> a, IntoExpression<T2> b) {
        return new TupleExpr<>(List.of(a.intoExpression(), b.intoExpression()));
    }

    static <T1, T2, T3> Expression<Tuple.Of3<T1, T2, T3>> of(IntoExpression<T1> a, IntoExpression<T2> b, IntoExpression<T3> c) {
        return new TupleExpr<>(List.of(a.intoExpression(), b.intoExpression(), c.intoExpression()));
    }

    static <T> Expression<Column<T>> of(Column<T> column) {
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

    static Expression<Table> of(Table table) {
        return position -> table.name();
    }

    default Expression<T> add(Expression<T> other) {
        var value = this;
        return position -> value.toSql(position) + " + " + other.toSql(position);
    }

    record TupleExpr<T extends Tuple>(List<Expression<?>> expressions) implements Expression<T> {
        @Override
        public String toSql(Position position) {
            var sb = new StringBuilder();
            if(position.equals(Position.VALUE)) {
                sb.append("VALUES (");
            }

            for(var expr : expressions) {
                sb.append(expr.toSql(position));
                if(expressions.getLast() != expr) {
                    sb.append(", ");
                }
            }

            if(position.equals(Position.VALUE)) {
                sb.append(")");
            }
            return sb.toString();
        }

        @Override
        public Stream<String> columns() {
            return this.expressions.stream()
                    .flatMap(Expression::columns);
        }

        @Override
        @SuppressWarnings("unchecked")
        public T getValueFromList(List<Object> objects) {
            return switch (objects.size()) {
                case 1 -> (T) objects.getFirst();
                case 2 -> (T) new Tuple.Of2<>(objects.getFirst(), objects.get(1));
                case 3 -> (T) new Tuple.Of3<>(objects.getFirst(), objects.get(1), objects.get(2));
                default -> throw new RuntimeException("not possible");
            };
        }
    }
}
