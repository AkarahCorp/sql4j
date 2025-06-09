package net.akarah.sql4j.table;

import net.akarah.sql4j.SqlConvertible;
import net.akarah.sql4j.value.SubtypedType;
import net.akarah.sql4j.value.expr.Value;
import net.akarah.sql4j.value.Type;

public class Column<T> implements SqlConvertible, Value<T> {
    Table.Impl table;
    String name;
    Type<T> type;
    Value<T> defaultValue;

    Column() {
    }

    public String name() {
        return this.name;
    }

    public String tabledName() {
        return this.table.name() + "." + this.name();
    }

    public String toDefinition() {
        return name
                + " " + type.toSql()
                + (type.isNullable() ? "" : " NOT NULL")
                + (defaultValue == null ? "" : " DEFAULT " + defaultValue.valueSql());
    }

    public String toSql() {
        return this.tabledName();
    }

    public Column<T> defaultValue(Value<T> defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    @Override
    public String column() {
        return this.name();
    }

    public static <I, O, T> SubtypedColumn<I, O, T> of(String name, SubtypedType<I, O, T> type) {
        return SubtypedColumn.of(name, type);
    }

    public static <T> Column<T> of(String name, Type<T> type) {
        var c = new Column<T>();
        c.name = name;
        c.type = type;
        return c;
    }

    public Table table() {
        return this.table;
    }
}
