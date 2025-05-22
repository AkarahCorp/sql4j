package net.akarah.sql4j.instruction;

import net.akarah.sql4j.Database;
import net.akarah.sql4j.table.Column;
import net.akarah.sql4j.table.Table;
import net.akarah.sql4j.value.expr.Expression;
import net.akarah.sql4j.value.QueryResult;

import java.util.ArrayList;
import java.util.List;

public final class Insert implements Instruction<Void> {
    Table table;
    List<Column<?>> columns = new ArrayList<>();
    List<Expression<?>> values = new ArrayList<>();

    public static Insert into(Table table) {
        var insert = new Insert();
        insert.table = table;
        return insert;
    }

    public <T> Insert withValue(Column<T> column, Expression<T> value) {
        this.columns.add(column);
        this.values.add(value);
        return this;
    }

    @Override
    public QueryResult<Void> evaluate(Database database) {
        database.execute(this);
        return QueryResult.empty();
    }

    @Override
    public String toSql(Position position) {
        var sb = new StringBuilder();
        sb.append("INSERT INTO ");
        sb.append(this.table.name());

        sb.append("(");
        for(var expr : this.columns) {
            sb.append(expr.name());
            if(this.columns.getLast() != expr) {
                sb.append(",");
            }
        }

        sb.append(") VALUES (");
        for(var expr : this.values) {
            sb.append(expr.toSql(Position.VALUE));
            if(this.values.getLast() != expr) {
                sb.append(",");
            }
        }
        sb.append(");");
        return sb.toString();
    }
}
