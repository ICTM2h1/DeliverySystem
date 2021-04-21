package System.Database;

import System.Config.Config;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Provides a class for connecting with the database.
 */
public class Connection {

    private static final Config config = Config.getInstance();
    private java.sql.Connection connection;

    /**
     * Creates connection to the database.
     */
    public Connection() {
        try {
            this.connection = DriverManager.getConnection(config.get("database_path"), config.get("database_username"), config.get("database_password"));
        } catch(SQLException e) {
            System.out.println("An error occurred while connecting to the database.");
            if (Boolean.parseBoolean(config.get("debug"))) {
                System.out.println(e.getMessage());
            }
            this.connection = null;
            System.exit(-1);
        }
    }

    /**
     * Returns connection.
     *
     * @return connection to the database
     */
    public java.sql.Connection get() {
        return this.connection;
    }

    /**
     * Closes the connection.
     */
    public void close() {
        try {
            this.connection.close();
        } catch (SQLException s) {
            System.out.println("An error occurred while closing the connection.");
            if (Boolean.parseBoolean(config.get("debug"))) {
                System.out.println(s.getMessage());
            }
            System.exit(-1);
        }
    }

    /**
     * Checks if connection is open.
     *
     * @return if connection is open.
     */
    public boolean isConnected() {
        try {
            return this.connection != null && !this.connection.isClosed();
        } catch (SQLException s) {
            return false;
        }
    }

}