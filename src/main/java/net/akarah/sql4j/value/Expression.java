package net.akarah.sql4j.value;

import net.akarah.sql4j.SqlConvertible;

public sealed interface Expression<T> extends SqlConvertible {
    static Expression<Integer> of(int value) {
        return new Int(value);
    }

    static Expression<String> of(String value) {
        return new Text(value);
    }

    default Expression<T> add(Expression<T> other) {
        return new Add<>(this, other);
    }

    record All() implements Expression<Object> {
        @Override
        public String toSql() {
            return "*";
        }
    }

    record Int(int value) implements Expression<Integer> {
        @Override
        public String toSql() {
            return Integer.toString(this.value);
        }
    }

    record Text(String value) implements Expression<String> {
        @Override
        public String toSql() {
            return "'" + value + "'";
        }
    }

    record Add<T>(Expression<T> left, Expression<T> right) implements Expression<T> {
        @Override
        public String toSql() {
            return left.toSql() + " + " + right.toSql();
        }
    }
}
