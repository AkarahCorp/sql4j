package net.akarah.sql4j.value.tuple;

import net.akarah.sql4j.value.expr.Expression;

import java.util.List;
import java.util.stream.Stream;

public record TupleExpr<T extends Tuple>(List<Expression<?>> expressions) implements Expression<T> {
    @Override
    public String toSql(Position position) {
        var sb = new StringBuilder();
        if (position.equals(Position.VALUE)) {
            sb.append("VALUES (");
        }

        for (var expr : expressions) {
            sb.append(expr.toSql(position));
            if (expressions.getLast() != expr) {
                sb.append(", ");
            }
        }

        if (position.equals(Position.VALUE)) {
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
