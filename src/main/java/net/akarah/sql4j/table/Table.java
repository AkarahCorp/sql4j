package net.akarah.sql4j.table;

import net.akarah.sql4j.Database;
import net.akarah.sql4j.SqlConvertible;
import net.akarah.sql4j.value.expr.Expression;
import net.akarah.sql4j.value.expr.Expressions;
import net.akarah.sql4j.value.expr.IntoExpression;

import java.util.ArrayList;
import java.util.List;

public class Table implements IntoExpression<Table> {
    Database database;
    String name;
    List<Column<?>> columns = new ArrayList<>();

    private Table() {}

    public static Table of(Database database, String name) {
        var table = new Table();
        table.name = name;
        table.database = database;
        return table;
    }

    public Database database() {
        return this.database;
    }

    public String name() {
        return this.name;
    }

    public <T> Table withColumn(Column<T> column) {
        column.table = this;
        this.columns.add(column);
        return this;
    }

    public Table dropIfExists() {
        this.database.executeStatement("DROP TABLE IF EXISTS " + this.name);
        return this;
    }

    public Table createIfNotExists() {
        var sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS ");
        sb.append(this.name);
        sb.append(" (");
        for(var column : this.columns) {
            sb.append(column.toSql(SqlConvertible.Position.VALUE));
        }
        sb.append(");");
        this.database.executeStatement(sb.toString());
        return this;
    }

    @Override
    public Expression<Table> intoExpression() {
        return Expressions.of(this);
    }
}
