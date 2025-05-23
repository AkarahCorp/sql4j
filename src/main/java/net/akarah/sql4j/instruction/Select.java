package net.akarah.sql4j.instruction;

import net.akarah.sql4j.Database;
import net.akarah.sql4j.ExceptionUtils;
import net.akarah.sql4j.table.Table;
import net.akarah.sql4j.value.expr.Value;
import net.akarah.sql4j.value.expr.IntoValue;
import net.akarah.sql4j.value.QueryResult;
import net.akarah.sql4j.value.tuple.Tuple;
import net.akarah.sql4j.value.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public final class Select<T> implements Instruction<T>, Value<T> {
    List<Value<?>> baseValues;
    Value<Table> table;
    List<Value<Boolean>> conditions = new ArrayList<>();
    Value<?> orderBy;
    String ordering; // either ASC or DESC
    Value<Long> limit;
    Value<Long> offset;

    public static <T> Select<T> on(IntoValue<T> baseExpression) {
        var sel = new Select<T>();
        sel.baseValues = List.of(baseExpression.intoValue());
        return sel;
    }

    public static <T1, T2> Select<Tuple.Of2<T1, T2>> on(
            IntoValue<T1> baseExpression1,
            IntoValue<T2> baseExpression2
    ) {
        var sel = new Select<Tuple.Of2<T1, T2>>();
        sel.baseValues = List.of(
                baseExpression1.intoValue(),
                baseExpression2.intoValue()
        );
        return sel;
    }

    public Select<T> from(IntoValue<Table> table) {
        this.table = table.intoValue();
        return this;
    }

    public Select<T> where(IntoValue<Boolean> condition) {
        this.conditions.add(condition.intoValue());
        return this;
    }

    public Select<T> orderByAscending(IntoValue<?> expression) {
        this.orderBy = expression.intoValue();
        this.ordering = "ASC";
        return this;
    }

    public Select<T> orderByDescending(IntoValue<?> expression) {
        this.orderBy = expression.intoValue();
        this.ordering = "DESC";
        return this;
    }

    public Select<T> limit(IntoValue<Long> expression) {
        this.limit = expression.intoValue();
        return this;
    }

    public Select<T> offset(IntoValue<Long> expression) {
        this.offset = expression.intoValue();
        return this;
    }

    @Override
    public String toSql() {
        var sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(StringUtils.groupedValues(baseValues, Value::toSql));
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
        var queryResult = QueryResult.<T>of(resultSet, this.baseValues.size());

        queryResult.withFunction(
                resultSet2 -> {
                    var objectList = this.baseValues.stream()
                            .map(Value::column)
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
