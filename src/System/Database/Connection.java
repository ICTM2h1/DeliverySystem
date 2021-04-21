package System.Database;

import System.Config.Config;
import System.Error.SystemError;

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
            this.connection = DriverManager.getConnection(config.get("database_fpath"), config.get("database_username"), config.get("database_password"));
        } catch(SQLException e) {
            this.connection = null;
            SystemError.handle(e, "An error occurred while connecting to the database.");
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
            SystemError.handle(s, "An error occurred while closing the connection.");
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