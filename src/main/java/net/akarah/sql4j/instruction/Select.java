package net.akarah.sql4j.instruction;

import net.akarah.sql4j.Database;
import net.akarah.sql4j.ExceptionUtils;
import net.akarah.sql4j.table.Table;
import net.akarah.sql4j.value.Conversions;
import net.akarah.sql4j.value.expr.Value;
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

    public static <T> Select<T> on(Value<T> baseExpression) {
        var sel = new Select<T>();
        sel.baseValues = List.of(baseExpression);
        return sel;
    }

    public static <T1, T2> Select<Tuple.Of2<T1, T2>> on(
            Value<T1> baseExpression1,
            Value<T2> baseExpression2
    ) {
        var sel = new Select<Tuple.Of2<T1, T2>>();
        sel.baseValues = List.of(
                baseExpression1,
                baseExpression2
        );
        return sel;
    }

    public static <T1, T2, T3> Select<Tuple.Of3<T1, T2, T3>> on(
            Value<T1> baseExpression1,
            Value<T2> baseExpression2,
            Value<T3> baseExpression3
    ) {
        var sel = new Select<Tuple.Of3<T1, T2, T3>>();
        sel.baseValues = List.of(
                baseExpression1,
                baseExpression2,
                baseExpression3
        );
        return sel;
    }

    public static <T1, T2, T3, T4> Select<Tuple.Of4<T1, T2, T3, T4>> on(
            Value<T1> baseExpression1,
            Value<T2> baseExpression2,
            Value<T3> baseExpression3,
            Value<T4> baseExpression4
    ) {
        var sel = new Select<Tuple.Of4<T1, T2, T3, T4>>();
        sel.baseValues = List.of(
                baseExpression1,
                baseExpression2,
                baseExpression3,
                baseExpression4
        );
        return sel;
    }

    public Select<T> from(Value<Table> table) {
        this.table = table;
        return this;
    }

    public Select<T> where(Value<Boolean> condition) {
        this.conditions.add(condition);
        return this;
    }

    public Select<T> orderByAscending(Value<?> expression) {
        this.orderBy = expression;
        this.ordering = "ASC";
        return this;
    }

    public Select<T> orderByDescending(Value<?> expression) {
        this.orderBy = expression;
        this.ordering = "DESC";
        return this;
    }

    public Select<T> limit(Value<Long> expression) {
        this.limit = expression;
        return this;
    }

    public Select<T> offset(Value<Long> expression) {
        this.offset = expression;
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
        if(!this.conditions.isEmpty()) {
            sb.append(" WHERE ");
            sb.append(
                    StringUtils.groupedValues(
                            this.conditions,
                            x -> "(" + x.toSql() + ")",
                            " AND "
                    )
            );
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
                    return Conversions.getValueFromList(objectList);
                }

        );
        return queryResult;
    }
}
