package System.Database;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

/**
 * Provides a class for interacting with the database.
 */
public class Query {

    /**
     * Holds the reference to the database object.
     */
    private static Query query;
    private static Connection connection;

    /**
     * Creates a new database instance.
     */
    private Query() {
        try {
            connection = new Connection();
        } catch(Exception e) {
            System.out.println("An error occurred while connecting to the database.");
        }
    }

    /**
     * Instantiates an instance for the database or gets an existing one.
     *
     * @return The database instance.
     */
    private static Query getInstance() {
        if (query != null) {
            return query;
        }

        query = new Query();
        return query;
    }

    public static ResultSet select(String query) {
        try {
            getInstance();
            Statement statement = connection.get().createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            connection.close();

            return resultSet;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            System.out.println("An error while executing this query.");
        }

        return null;
    }

    public static ResultSet select(String query, HashMap<String, String> conditions) {
        try {
            getInstance();
            Statement statement = connection.get().createStatement();
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
