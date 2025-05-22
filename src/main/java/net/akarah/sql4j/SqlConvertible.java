package net.akarah.sql4j;

public interface SqlConvertible {
    String toSql();

    enum Position {
        SELECTOR,
        VALUE;
    }
}
