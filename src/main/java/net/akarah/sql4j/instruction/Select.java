package net.akarah.sql4j.instruction;

import net.akarah.sql4j.Database;
import net.akarah.sql4j.ExceptionUtils;
import net.akarah.sql4j.table.Table;
import net.akarah.sql4j.value.expr.Expression;
import net.akarah.sql4j.value.expr.IntoExpression;
import net.akarah.sql4j.value.QueryResult;
import net.akarah.sql4j.value.tuple.Tuple;
import net.akarah.sql4j.value.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public final class Select<T> implements Instruction<T> {
    List<Expression<?>> baseExpressions;
    Expression<Table> table;
    List<Expression<Boolean>> conditions = new ArrayList<>();
    Expression<?> orderBy;
    String ordering; // either ASC or DESC
    Expression<Integer> limit;
    Expression<Integer> offset;

    public static <T> Select<T> on(IntoExpression<T> baseExpression) {
        var sel = new Select<T>();
        sel.baseExpressions = List.of(baseExpression.intoExpression());
        return sel;
    }

    public static <T1, T2> Select<Tuple.Of2<T1, T2>> on(
            IntoExpression<T1> baseExpression1,
            IntoExpression<T2> baseExpression2
    ) {
        var sel = new Select<Tuple.Of2<T1, T2>>();
        sel.baseExpressions = List.of(
                baseExpression1.intoExpression(),
                baseExpression2.intoExpression()
        );
        return sel;
    }

    public Select<T> from(IntoExpression<Table> table) {
        this.table = table.intoExpression();
        return this;
    }

    public Select<T> where(IntoExpression<Boolean> condition) {
        this.conditions.add(condition.intoExpression());
        return this;
    }

    public Select<T> orderByAscending(IntoExpression<?> expression) {
        this.orderBy = expression.intoExpression();
        this.ordering = "ASC";
        return this;
    }

    public Select<T> orderByDescending(IntoExpression<?> expression) {
        this.orderBy = expression.intoExpression();
        this.ordering = "DESC";
        return this;
    }

    public Select<T> limit(IntoExpression<Integer> expression) {
        this.limit = expression.intoExpression();
        return this;
    }

    public Select<T> offset(IntoExpression<Integer> expression) {
        this.offset = expression.intoExpression();
        return this;
    }

    @Override
    public String toSql() {
        var sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(StringUtils.groupedValues(baseExpressions, Expression::toSql));
        if(this.table != null) {
            sb.append(" FROM ");
            sb.append(this.table.toSql());
        }
        for(var condition : this.conditions) {
            sb.append(" WHERE ");
            sb.append(condition.toSql());
        }
        if(this.orderBy != null) {
            sb.append(" ORDER BY ");
            sb.append(this.orderBy.toSql());
            sb.append(" ");
            sb.append(this.ordering);
        }
        if(this.limit != null) {
            sb.append(" LIMIT ");
            sb.append(this.limit.toSql());
        }
        if(this.offset != null) {
            sb.append(" OFFSET ");
            sb.append(this.offset.toSql());
        }
        sb.append(";");
        return sb.toString();
    }

    @Override
    public QueryResult<T> evaluate(Database database) {
        var resultSet = database.evaluate(this);
        var queryResult = QueryResult.<T>of(resultSet, (int) this.baseExpressions.size());

        queryResult.withFunction(
                resultSet2 -> {
                    var objectList = this.baseExpressions.stream()
                            .map(Expression::column)
                            .map(name -> ExceptionUtils.sneakyThrows(() -> resultSet2.getObject(name)))
                            .toList();
                    return getValueFromList(objectList);
                }

        );
        return queryResult;
    }


    @SuppressWarnings("unchecked")
    public static <T> T getValueFromList(List<Object> objects) {
        return switch (objects.size()) {
            case 1 -> (T) objects.getFirst();
            case 2 -> (T) new Tuple.Of2<>(objects.getFirst(), objects.get(1));
            case 3 -> (T) new Tuple.Of3<>(objects.getFirst(), objects.get(1), objects.get(2));
            default -> throw new RuntimeException("not possible, too many columns :P");
        };
    }
}
