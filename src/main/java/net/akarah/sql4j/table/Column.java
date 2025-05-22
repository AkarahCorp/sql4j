package net.akarah.sql4j.table;

import net.akarah.sql4j.SqlConvertible;
import net.akarah.sql4j.value.Type;

public class Column<T> implements SqlConvertible {
    String name;
    Type<T> type;

    private Column() {
    }

    public String toSql() {
        return name
                + " " + type.toSql()
                + (type.isNullable() ? "" : " NOT NULL")
                + ",";
    }

    public static <T> Column<T> of(String name, Type<T> type) {
        var c = new Column<T>();
        c.name = name;
        c.type = type;
        return c;
    }
}
