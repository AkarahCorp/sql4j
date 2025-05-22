package net.akarah.sql4j;

public interface SqlConvertible {
    String toSql(Position position);

    enum Position {
        SELECTOR,
        VALUE;
    }
}
