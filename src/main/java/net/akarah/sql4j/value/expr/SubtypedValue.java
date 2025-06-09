package net.akarah.sql4j.value.expr;

public interface SubtypedValue<I, O, T> extends Value<T> {
    public Value<O> subscript(Value<I> value);
}
