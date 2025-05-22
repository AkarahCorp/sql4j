import net.akarah.sql4j.Database;
import net.akarah.sql4j.table.Column;
import net.akarah.sql4j.table.Table;
import net.akarah.sql4j.value.Type;

public class TestHelpers {
    public static Database DATABASE = Database.create("testing_database")
            .loadProperties(Database.Properties.loadFromFile("./.env"));

    public static Column<String> PLAYER_NAME = Column.of("name", Type.text());
    public static Column<Integer> PLAYER_AGE = Column.of("age", Type.integer());

    public static Table PLAYERS_TABLE = DATABASE.createTable("players")
            .withColumn(PLAYER_NAME)
            .withColumn(PLAYER_AGE)
            .dropIfExists()
            .createIfNotExists();

    QueryTests queryTests = new QueryTests();
}
