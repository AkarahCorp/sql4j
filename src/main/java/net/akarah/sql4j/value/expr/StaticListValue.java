package net.akarah.sql4j.value.expr;

import net.akarah.sql4j.value.Type;
import net.akarah.sql4j.value.util.StringUtils;

import java.util.List;

public class StaticListValue<T> implements Value<List<T>>, SubtypedValue<Integer, T, List<T>> {
    List<Value<T>> values;
    Type<T> subtype;

    @Override
    public String toSql() {
        var sb = new StringBuilder();
        sb.append("ARRAY[")
                .append(StringUtils.groupedValues(values, Value::toSql, ","))
                .append("]");
        if(this.subtype != null) {
            sb.append("::")
                    .append(this.subtype.toSql())
                    .append("[]");
        }
        return sb.toString();
    }

    protected StaticListValue(List<Value<T>> values, Type<T> subtype) {
        this.values = values;
        this.subtype = subtype;
    }

    @Override
    public Value<T> subscript(Value<Integer> value) {
        return () -> this.valueSql() + "[" + value + "]";
    }
}
