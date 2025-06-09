package net.akarah.sql4j.value;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public sealed interface QueryResult<T> extends AutoCloseable {
    Optional<T> next();

    @Override
    void close();

    static <T> QueryResult.Impl<T> of(ResultSet resultSet, int size) {
        var qr = new QueryResult.Impl<T>();
        qr.resultSet = resultSet;
        qr.size = size;
        return qr;
    }

    static <T> QueryResult.Empty<T> empty() {
        return new Empty<>();
    }

    final class Empty<T> implements QueryResult<T>, AutoCloseable {
        @Override
        public Optional<T> next() {
            return Optional.empty();
        }

        @Override
        public void close() {

        }
    }

    final class Impl<T> implements QueryResult<T>, AutoCloseable {
        ResultSet resultSet;
        int size = 0;
        Function<ResultSet, T> listFunction;

        private Impl() {}

        public void withFunction(Function<ResultSet, T> listFunction) {
            this.listFunction = listFunction;
        }

        public Optional<T> next() {
            try {
                if(this.resultSet.next()) {
                    return Optional.ofNullable(this.listFunction.apply(this.resultSet));
                } else {
                    return Optional.empty();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void close() {
            try {
                resultSet.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
