package net.akarah.sql4j.table;

import net.akarah.sql4j.Database;
import net.akarah.sql4j.value.expr.Value;
import net.akarah.sql4j.value.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public interface Table extends Value<Table> {


    static Table.Builder of(Database database, String name) {
        var table = new Table.Builder();
        table.name = name;
        table.database = database;
        return table;
    }

    class Builder {
        Database database;
        String name;
        List<Column<?>> columns = new ArrayList<>();

        public Builder dropIfExists() {
            this.database.executeStatement("DROP TABLE IF EXISTS " + this.name);
            return this;
        }

        public Table.Impl createIfNotExists() {
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
            return this.build();
        }

        public <T> Builder withColumn(Column<T> column) {
            this.columns.add(column);
            return this;
        }

        public Table.Impl build() {
            var impl = new Impl();

            for(var column : this.columns) {
                column.table = impl;
            }

            impl.database = this.database;
            impl.name = this.name;
            impl.columns = this.columns;

            return impl;
        }
    }

    class Impl implements Table {
        Database database;
        String name;
        List<Column<?>> columns = new ArrayList<>();

        private Impl() {}

        public String name() {
            return this.name;
        }

        @Override
        public String toSql() {
            return this.name();
        }

        public <T> Table.InnerJoinOn<T> innerJoinOn(
                Table.Impl other,
                Column<T> leftColumn,
                Column<T> rightColumn
        ) {
            return new InnerJoinOn<T>(this, other, leftColumn, rightColumn);
        }
    }

    record InnerJoinOn<T>(Table.Impl left, Table.Impl right, Column<T> leftColumn, Column<T> rightColumn) implements Table {

        @Override
        public String toSql() {
            return left.name() + " INNER JOIN " + right.name() + " ON " + leftColumn.tabledName() + " = " + rightColumn.tabledName();
        }
    }
}
