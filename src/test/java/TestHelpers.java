import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class TestHelpers {
    public static Connection localDbConnection() {
        try {
            Class.forName("org.postgresql.Driver");

            var props = new Properties();
            props.load(new FileInputStream("./.env"));

            var username = (String) props.remove("user");
            var password = (String) props.remove("password");
            var url = (String) props.remove("url");

            return DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testConnection() throws SQLException {
        TestHelpers.localDbConnection();
    }
}
