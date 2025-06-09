package net.akarah.sql4j.value.expr;

import java.util.List;

public class DynamicListValue<T> implements SubtypedValue<Integer, T, List<T>> {
    Value<List<T>> inner;

    @Override
    public Value<T> subscript(Value<Integer> value) {
        return () -> this.inner.toSql() + "[" + value.toSql() + "]";
    }

    @Override
    public String toSql() {
        return inner.toSql();
    }
}
