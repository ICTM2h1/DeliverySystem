package System.Database;

import System.Config.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

/**
 * Provides a class for interacting with the database.
 */
public class Database {

    /**
     * Holds the reference to the database object.
     */
    private static Database database;
    private static Config config = Config.getInstance();
    private static Connection connection;

    /**
     * Creates a new database instance.
     */
    private Database() {
        try {
            Class.forName(config.get("database_driver"));
            connection = DriverManager.getConnection(config.get("database_path"), config.get("database_username"), config.get("database_password"));
        } catch(Exception e) {
            System.out.println(e.getMessage());
            System.out.println("An error occurred while connecting to the database.");
        }
    }

    /**
     * Instantiates an instance for the database or gets an existing one.
     *
     * @return The database instance.
     */
    private static Database getInstance() {
        if (database != null) {
            return database;
        }

        database = new Database();
        return database;
    }

    public static ResultSet select(String query) {
        try {
            getInstance();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            connection.close();

            return resultSet;
        } catch(Exception e) {
            System.out.println("An error while executing this query.");
        }

        return null;
    }

    public static ResultSet select(String query, HashMap<String, String> conditions) {
        try {
            getInstance();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            connection.close();

            return resultSet;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            System.out.println("An error while executing this query.");
        }

        return null;
    }

}
