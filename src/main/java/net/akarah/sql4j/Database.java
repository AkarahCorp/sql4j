package net.akarah.sql4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    Connection connection;

    String databaseName;
    String hostname;

    String username;
    String password;

    private Database() {}

    public static Database create(String databaseName) {
        var db = new Database();
        db.databaseName = databaseName;
        return db;
    }

    public Database hostname(String hostname) {
        this.hostname = hostname;
        return this;
    }

    public Database username(String username) {
        this.username = username;
        return this;
    }

    public Database password(String password) {
        this.password = password;
        return this;
    }

    public Database loadProperties(Database.Properties properties) {
        this.hostname = properties.hostname();
        this.username = properties.username();
        this.password = properties.password();
        return this;
    }

    public Connection connection() {
        try {
            Class.forName("org.postgresql.Driver");
            if(this.connection == null) {
                this.connection = DriverManager.getConnection("jdbc:postgresql://" + this.hostname + "/" + this.databaseName, username, password);
            }
            return this.connection;
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public record Properties(String hostname, String username, String password) {
        public static Database.Properties loadFromFile(String fileName) {
            var props = new java.util.Properties();
            try {
                props.load(new FileInputStream(fileName));
            } catch (IOException e) {
                // this exception should not happen unless smth is horribly wrong
                throw new RuntimeException(e);
            }

            var username = (String) props.remove("user");
            var password = (String) props.remove("password");
            var hostname = (String) props.remove("hostname");

            return new Database.Properties(hostname, username, password);
        }
    }
}
