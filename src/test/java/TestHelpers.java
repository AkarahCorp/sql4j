import net.akarah.sql4j.Database;
import net.akarah.sql4j.table.Column;
import net.akarah.sql4j.table.SubtypedColumn;
import net.akarah.sql4j.table.Table;
import net.akarah.sql4j.value.Type;

import java.util.List;

public class TestHelpers {
    public static Database DATABASE = Database.create("testing_database")
            .loadProperties(Database.Properties.loadFromFile("./.env"));

    public static Column<String> PLAYER_NAME = Column.of("name", Type.text());
    public static Column<Integer> PLAYER_AGE = Column.of("age", Type.integer());
    public static Column<Integer> PLAYER_ID = Column.of("id", Type.serial());
    public static SubtypedColumn<Integer, String, List<String>> PLAYER_NICKNAMES = Column.of("nicknames", Type.text().listOf());

    public static Table.Impl PLAYERS_TABLE = DATABASE.createTable("players")
            .withColumn(PLAYER_NAME)
            .withColumn(PLAYER_AGE)
            .withColumn(PLAYER_ID)
            .withColumn(PLAYER_NICKNAMES)
            .dropIfExists()
            .createIfNotExists();

    QueryTests queryTests = new QueryTests();
}
