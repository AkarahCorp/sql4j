package net.akarah.sql4j.value;

import java.math.BigDecimal;
import java.util.Optional;

public interface Type<T> {
    String toSql();

    static Type<String> text() {
        return () -> "text";
    }

    static Type<Integer> integer() {
        return () -> "int";
    }

    static Type<Short> smallint() {
        return () -> "smallint";
    }

    static Type<Long> bigint() {
        return () -> "bigint";
    }

    static Type<Integer> serial() {
        return () -> "serial";
    }

    static Type<Short> smallSerial() {
        return () -> "smallserial";
    }

    static Type<Long> bigSerial() {
        return () -> "bigserial";
    }

    static Type<BigDecimal> numeric() {
        return () -> "numeric";
    }

    default Type<Optional<T>> nullable() {
        return new Nullable<>(this);
    }

    default boolean isNullable() {
        return false;
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
