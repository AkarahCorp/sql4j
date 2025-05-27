package net.akarah.sql4j.value;

import java.math.BigDecimal;
import java.util.Optional;

public sealed interface Type<T> {
    String toSql();
    T convert(Object object);

    static Type<String> text() {
        return new Type.Text();
    }

    static Type<Integer> integer() {
        return new Int();
    }

    static Type<Short> smallint() {
        return new SmallInt();
    }

    static Type<Long> bigint() {
        return new BigInt();
    }

    static Type<Integer> serial() {
        return new Serial();
    }

    static Type<Short> smallSerial() {
        return new SmallSerial();
    }

    static Type<Long> bigSerial() {
        return new BigSerial();
    }

    static Type<BigDecimal> numeric() {
        return new Numeric();
    }

    default Type<Optional<T>> nullable() {
        return new Nullable<>(this);
    }

    default boolean isNullable() {
        return false;
    }

    record Text() implements Type<String> {
        @Override
        public String toSql() {
            return "text";
        }

        @Override
        public String convert(Object object) {
            return (String) object;
        }
    }

    record SmallInt() implements Type<Short> {
        @Override
        public String toSql() {
            return "smallint";
        }

        @Override
        public Short convert(Object object) {
            return (Short) object;
        }
    }

    record BigInt() implements Type<Long> {
        @Override
        public String toSql() {
            return "bigint";
        }

        @Override
        public Long convert(Object object) {
            return (Long) object;
        }
    }

    record Int() implements Type<Integer> {
        @Override
        public String toSql() {
            return "integer";
        }

        @Override
        public Integer convert(Object object) {
            return (Integer) object;
        }
    }

    record SmallSerial() implements Type<Short> {
        @Override
        public String toSql() {
            return "smallserial";
        }

        @Override
        public Short convert(Object object) {
            return (Short) object;
        }
    }

    record Serial() implements Type<Integer> {
        @Override
        public String toSql() {
            return "serial";
        }

        @Override
        public Integer convert(Object object) {
            return (Integer) object;
        }
    }

    record BigSerial() implements Type<Long> {
        @Override
        public String toSql() {
            return "bigserial";
        }

        @Override
        public Long convert(Object object) {
            return (Long) object;
        }
    }

    record Numeric() implements Type<BigDecimal> {
        @Override
        public String toSql() {
            return "numeric";
        }

        @Override
        public BigDecimal convert(Object object) {
            return (BigDecimal) object;
        }
    }

    record Nullable<T>(Type<T> inner) implements Type<Optional<T>> {
        @Override
        public String toSql() {
            return inner.toSql();
        }

        @Override
        public Optional<T> convert(Object object) {
            if(object == null) {
                return Optional.empty();
            }
            return Optional.of(inner.convert(object));
        }

        @Override
        public boolean isNullable() {
            return true;
        }
    }
}
