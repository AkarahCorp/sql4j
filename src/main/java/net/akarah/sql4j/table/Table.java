package net.akarah.sql4j.table;

import net.akarah.sql4j.Database;
import net.akarah.sql4j.value.expr.Value;
import net.akarah.sql4j.value.expr.Values;
import net.akarah.sql4j.value.expr.IntoValue;
import net.akarah.sql4j.value.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Table implements IntoValue<Table>, Value<Table> {
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
        String sb = "CREATE TABLE IF NOT EXISTS " +
                this.name +
                StringUtils.parenthesizedValues(this.columns, Column::toDefinition) +
                ";";
        this.database.executeStatement(sb);
        return this;
    }

    @Override
    public Value<Table> intoValue() {
        return Values.of(this);
    }

    @Override
    public String toSql() {
        return this.name();
    }
}
