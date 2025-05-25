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

public final class Delete<T> implements Instruction<T> {
    Table table;
    List<Value<Boolean>> where = new ArrayList<>();

    public static <T> Delete<T> from(Table table) {
        var sel = new Delete<T>();
        sel.table = table;
        return sel;
    }

    public Delete<T> where(Value<Boolean> value) {
        this.where.add(value);
        return this;
    }

    @Override
    public String toSql() {
        var sb = new StringBuilder();
        sb.append("DELETE ");
        sb.append(this.table.name());
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
