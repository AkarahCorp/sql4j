package net.akarah.sql4j.value;

import com.google.gson.JsonElement;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface Type<T> {
    String toSql();

    static Type<String> text() {
        return () -> "text";
    }

    static Type<Integer> integer() {
        return () -> "int";
    }

    static Type<Integer> smallint() {
        return () -> "smallint";
    }

    static Type<Long> bigint() {
        return () -> "bigint";
    }

    static Type<Integer> serial() {
        return () -> "serial";
    }

    static Type<Integer> smallSerial() {
        return () -> "smallserial";
    }

    static Type<Long> bigSerial() {
        return () -> "bigserial";
    }

    static Type<BigDecimal> numeric() {
        return () -> "numeric";
    }

    static Type<JsonElement> json() {
        return () -> "json";
    }

    static Type<JsonElement> jsonb() {
        return () -> "jsonb";
    }

    static Type<Boolean> bool() {
        return () -> "boolean";
    }

    static <T, T2 extends Type<T>> SubtypedType<Integer, T, List<T>> listOf(T2 type) {
        return () -> type.toSql() + " ARRAY";
    }

    default Type<Optional<T>> nullable() {
        return new Nullable<>(this);
    }

    default boolean isNullable() {
        return false;
    }

    default SubtypedType<Integer, T, List<T>> listOf() {
        return Type.listOf(this);
    }

    record Nullable<T>(Type<T> inner) implements Type<Optional<T>> {
        @Override
        public String toSql() {
            return inner.toSql();
        }

        @Override
        public boolean isNullable() {
            return true;
        }
    }
}
