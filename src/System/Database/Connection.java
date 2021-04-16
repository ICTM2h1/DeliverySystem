package System.Database;

import System.Config.Config;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Connection {

    private static Config config = Config.getInstance();
    private java.sql.Connection connection;

    /**
     * Creates connection to the database.
     */
    public Connection() {
        try {
            Class.forName(config.get("database_driver"));
            this.connection = DriverManager.getConnection(config.get("database_path"), config.get("database_username"), config.get("database_password"));
        } catch(SQLException | ClassNotFoundException e) {
            System.out.println("An error occurred while connecting to the database.");
            this.connection = null;
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