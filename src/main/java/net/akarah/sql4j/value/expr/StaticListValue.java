package net.akarah.sql4j.value.expr;

import net.akarah.sql4j.value.util.StringUtils;

import java.util.List;

public class StaticListValue<T> implements Value<List<T>>, SubtypedValue<Integer, T, List<T>> {
    List<Value<T>> values;

    @Override
    public String toSql() {
        return "ARRAY["
                    + StringUtils.groupedValues(
                        values,
                        Value::toSql,
                        ","
                    )
                    + "]";
    }

    protected StaticListValue(List<Value<T>> values) {
        this.values = values;
    }

    @Override
    public Value<T> subscript(Value<Integer> value) {
        return () -> this.toSql() + "[" + value + "]";
    }
}
