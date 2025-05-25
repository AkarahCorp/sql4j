package net.akarah.sql4j.table;

import net.akarah.sql4j.Database;
import net.akarah.sql4j.value.expr.Value;
import net.akarah.sql4j.value.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Table implements Value<Table> {
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
        sb.append("CREATE TABLE IF NOT EXISTS ")
                .append(this.name)
                .append(StringUtils.parenthesizedValues(this.columns, Column::toDefinition))
                .append(";");
        for(var column : this.columns) {
            sb.append("ALTER TABLE ")
                    .append(this.name)
                    .append(" ADD COLUMN IF NOT EXISTS ")
                    .append(column.toDefinition())
                    .append(";");
        }
        this.database.executeStatement(sb.toString());
        return this;
    }

    @Override
    public String toSql() {
        return this.name();
    }
}
