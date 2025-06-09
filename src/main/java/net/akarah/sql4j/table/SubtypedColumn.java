package net.akarah.sql4j.table;

import net.akarah.sql4j.value.SubtypedType;
import net.akarah.sql4j.value.expr.SubtypedValue;
import net.akarah.sql4j.value.expr.Value;
import net.akarah.sql4j.value.expr.Values;

public class SubtypedColumn<I, O, T> extends Column<T> implements SubtypedValue<I, O, T> {
    SubtypedType<I, O, T> stType;

    private SubtypedColumn() {
        super();
    }

    public static <I, O, T> SubtypedColumn<I, O, T> of(String name, SubtypedType<I, O, T> type) {
        var c = new SubtypedColumn<I, O, T>();
        c.name = name;
        c.stType = type;
        c.type = type;
        return c;
    }

    @Override
    public Value<O> subscript(Value<I> value) {
        return Values.ofFunctions(
                () -> this.toSql() + "[" + value.toSql() + "]",
                this::column
        );
    }
}
