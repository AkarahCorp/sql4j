import net.akarah.sql4j.Database;
import net.akarah.sql4j.table.Column;
import net.akarah.sql4j.table.Table;
import net.akarah.sql4j.value.Type;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

public class TestHelpers {
    public static Database DATABASE = Database.create("testing_database")
            .loadProperties(Database.Properties.loadFromFile("./.env"));

    public static Table PLAYERS_TABLE = DATABASE.createTable("players")
            .withColumn(Column.of("name", Type.text()))
            .withColumn(Column.of("age", Type.integer()))
            .withColumn(Column.of("description", Type.text().nullable()))
            .dropIfExists()
            .createIfNotExists();

    public static Connection localDbConnection() {
        return DATABASE.connection();
    }

    @Test
    public void testConnection() {
        var conn = localDbConnection();
    }
}
