package net.akarah.sql4j;

import net.akarah.sql4j.instruction.Instruction;
import net.akarah.sql4j.table.Table;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
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

    public void executeStatement(String statement) {
        var newStatement = statement.replace("\n", "").replace(",)", ")");
        try {
            this.connection().prepareStatement(newStatement).execute();
        } catch (SQLException exception) {
            System.out.println("Statement failed: " + newStatement);
            throw new RuntimeException(exception);
        }
    }

    public Table createTable(String name) {
        return Table.of(this, name);
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

    // TODO: make the ResultSet api type-safe
    public ResultSet evaluate(Instruction<?> expr) {
        var fmtStmt = expr.toSql().replace("\n", "").replace(",)", ")");
        try {
            var stmt = this.connection().prepareStatement(fmtStmt);
            var r = stmt.executeQuery();
            System.out.println(fmtStmt);
            return r;
        } catch (SQLException e) {
            System.out.println("failed: " + fmtStmt);
            throw new RuntimeException(e);
        }
    }

    public void execute(Instruction<?> instruction) {
        var fmtStmt = instruction.toSql().replace("\n", "").replace(",)", ")");

        try {
            var stmt = this.connection().prepareStatement(fmtStmt);
            stmt.execute();
            System.out.println(fmtStmt);
        } catch (SQLException e) {
            System.out.println("failed: " + fmtStmt);
            throw new RuntimeException(e);
        }
    }
}
