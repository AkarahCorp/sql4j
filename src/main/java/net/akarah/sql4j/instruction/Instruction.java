package net.akarah.sql4j.instruction;

import net.akarah.sql4j.Database;
import net.akarah.sql4j.SqlConvertible;
import net.akarah.sql4j.value.QueryResult;

public sealed interface Instruction<T> extends SqlConvertible permits Delete, Insert, Select, Update {
    QueryResult<T> evaluate(Database database);
}
