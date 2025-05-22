package net.akarah.sql4j.value;

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

    default Type<Optional<T>> nullable() {
        return new Nullable<>(this);
    }

    default boolean isNullable() {
        return false;
    }

    record Text() implements Type<String> {
        @Override
        public String toSql() {
            return "TEXT";
        }

        @Override
        public String convert(Object object) {
            return (String)  object;
        }
    }

    record Int() implements Type<Integer> {
        @Override
        public String toSql() {
            return "INTEGER";
        }

        @Override
        public Integer convert(Object object) {
            return (Integer)  object;
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
