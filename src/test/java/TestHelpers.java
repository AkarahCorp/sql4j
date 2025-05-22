import net.akarah.sql4j.Database;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

public class TestHelpers {
    public static Database DATABASE = Database.create("testing_database")
            .loadProperties(Database.Properties.loadFromFile("./.env"));

    public static Connection localDbConnection() {
        return DATABASE.connection();
    }

    @Test
    public void testConnection() {
        var conn = localDbConnection();
    }
}
