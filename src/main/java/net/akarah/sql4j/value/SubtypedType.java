package net.akarah.sql4j.value;

public interface SubtypedType<I, O, T> extends Type<T> {
    String toSql();
}
