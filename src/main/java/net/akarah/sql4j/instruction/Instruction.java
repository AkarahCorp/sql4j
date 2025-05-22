package net.akarah.sql4j.instruction;

import net.akarah.sql4j.Database;
import net.akarah.sql4j.SqlConvertible;
import net.akarah.sql4j.value.QueryResult;

public interface Instruction<T> extends SqlConvertible {
    QueryResult<T> evaluate(Database database);
}
