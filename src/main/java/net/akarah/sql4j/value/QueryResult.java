package net.akarah.sql4j.value;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.function.Function;

public class QueryResult<T> implements AutoCloseable {
    ResultSet resultSet;
    int size = 0;
    Function<ResultSet, T> listFunction;

    private QueryResult() {}

    public static <T> QueryResult<T> of(ResultSet resultSet, int size) {
        var qr = new QueryResult<T>();
        qr.resultSet = resultSet;
        qr.size = size;
        return qr;
    }

    public void withFunction(Function<ResultSet, T> listFunction) {
        this.listFunction = listFunction;
    }

    public Optional<T> next() {
        try {
            if(this.resultSet.next()) {
                return Optional.of(this.listFunction.apply(this.resultSet));
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
