package net.akarah.sql4j.instruction;

import net.akarah.sql4j.Database;
import net.akarah.sql4j.table.Column;
import net.akarah.sql4j.table.Table;
import net.akarah.sql4j.value.QueryResult;
import net.akarah.sql4j.value.expr.Value;
import net.akarah.sql4j.value.tuple.Tuple;
import net.akarah.sql4j.value.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public final class Update<T> implements Instruction<T> {
    Table.Impl table;
    List<Tuple.Of2<Column<?>, Value<?>>> columnsToSet = new ArrayList<>();
    List<Value<Boolean>> where = new ArrayList<>();

    public static <T> Update<T> table(Table.Impl table) {
        var sel = new Update<T>();
        sel.table = table;
        return sel;
    }

    public <U> Update<T> setColumn(Column<U> column, Value<U> value) {
        this.columnsToSet.add(new Tuple.Of2<>(column, value));
        return this;
    }

    public Update<T> where(Value<Boolean> value) {
        this.where.add(value);
        return this;
    }

    @Override
    public String toSql() {
        var sb = new StringBuilder();
        sb.append("UPDATE ");
        sb.append(this.table.name());
        if(!this.columnsToSet.isEmpty()) {
            sb.append(" SET ");
            sb.append(StringUtils.groupedValues(
                    columnsToSet,
                    column -> column.a().name() + " = " + column.b().toSql(),
                    " && "
            ));
        }
        if(!this.where.isEmpty()) {
            sb.append(" WHERE ");
            sb.append(StringUtils.groupedValues(
                    this.where,
                    x -> "(" + x.toSql() + ")",
                    " AND "
            ));
        }
        sb.append(";");
        return sb.toString();
    }

    @Override
    public QueryResult<T> evaluate(Database database) {
        database.execute(this);
        return QueryResult.empty();
    }
}
