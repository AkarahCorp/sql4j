package net.akarah.sql4j.instruction;

import net.akarah.sql4j.Database;
import net.akarah.sql4j.ExceptionUtils;
import net.akarah.sql4j.table.Table;
import net.akarah.sql4j.value.expr.Expression;
import net.akarah.sql4j.value.expr.IntoExpression;
import net.akarah.sql4j.value.QueryResult;

public final class Select<T> implements Instruction<T> {
    Expression<T> baseExpression;
    Expression<Table> table;

    public static <T> Select<T> on(IntoExpression<T> baseExpression) {
        var sel = new Select<T>();
        sel.baseExpression = baseExpression.intoExpression();
        return sel;
    }

    public Select<T> from(IntoExpression<Table> table) {
        this.table = table.intoExpression();
        return this;
    }

    @Override
    public String toSql() {
        var sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(baseExpression.toSql());
        if(this.table != null) {
            sb.append(" FROM ");
            sb.append(this.table.toSql());
        }
        sb.append(";");
        return sb.toString();
    }

    @Override
    public QueryResult<T> evaluate(Database database) {
        var resultSet = database.evaluate(this);
        var queryResult = QueryResult.<T>of(resultSet, (int) this.baseExpression.columns().count());

        queryResult.withFunction(
                resultSet2 ->
                            this.baseExpression.getValueFromList(
                                    this.baseExpression.columns()
                                            .map(name -> ExceptionUtils.sneakyThrows(() -> resultSet2.getObject(name)))
                                            .toList()
                            )
        );
        return queryResult;
    }
}
