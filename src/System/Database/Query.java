package System.Database;

import System.Config.Config;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Provides a class for interacting with the database.
 */
public class Query {

    private static final Config config = Config.getInstance();

    /**
     * Selects data from the database.
     *
     * @param query The query to be executed.
     * @param selectFields The fields to be selected from the returned data.
     *
     * @return An array list with the data of the result set.
     */
    public static ArrayList<HashMap<String, String>> select(String query, ArrayList<String> selectFields) {
        return executeQuery(query, selectFields, null);
    }

    /**
     * Selects data from the database.
     *
     * @param query The query to be executed.
     * @param selectFields The fields to be selected from the returned data.
     * @param conditions The conditions of the query.
     *
     * @return An array list with the data of the result set.
     */
    public static ArrayList<HashMap<String, String>> select(String query, ArrayList<String> selectFields, HashMap<String, String> conditions) {
        return executeQuery(query, selectFields, conditions);
    }

    /**
     * Inserts data into a table inside the database.
     *
     * @param table The table to insert data in.
     * @param values The data to be inserted into the database.
     *
     * @return Whether the data was successfully inserted or not.
     */
    public static boolean insert(String table, HashMap<String, String> values) {
        HashMap<String, String> insertValues = new HashMap<>();
        StringBuilder queryColumns = new StringBuilder();
        StringBuilder queryValues = new StringBuilder();
        Iterator<Map.Entry<String, String>> it = values.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> pair = it.next();

            queryColumns.append(pair.getKey()).append(it.hasNext() ? ", " : " ");
            queryValues.append(":").append(pair.getKey()).append(it.hasNext() ? ", " : " ");
            insertValues.put(pair.getKey(), pair.getValue());

            it.remove(); // avoids a ConcurrentModificationException
        }

        String query = String.format("INSERT INTO %s (%s) VALUES (%s)", table, queryColumns, queryValues);

        return execute(query, insertValues);
    }

    /**
     * Update data from one or more records inside a table from the database.
     *
     * @param table The table to update data in.
     * @param values The data to be updated.
     * @param conditions The conditions for updating the data.
     *
     * @return Whether the data was successfully updated or not.
     */
    public static boolean update(String table, HashMap<String, String> values, HashMap<String, String> conditions) {
        HashMap<String, String> updateValues = new HashMap<>();
        StringBuilder queryValues = new StringBuilder();
        Iterator<Map.Entry<String, String>> itValues = values.entrySet().iterator();
        while (itValues.hasNext()) {
            Map.Entry<String, String> pair = itValues.next();

            queryValues.append(pair.getKey()).append(" = ").append(":update").append(pair.getKey()).append(itValues.hasNext() ? ", " : " ");
            updateValues.put("update" + pair.getKey(), pair.getValue());

            itValues.remove(); // avoids a ConcurrentModificationException
        }

        StringBuilder queryConditions = new StringBuilder();
        Iterator<Map.Entry<String, String>> itConditions = conditions.entrySet().iterator();
        while (itConditions.hasNext()) {
            Map.Entry<String, String> pair = itConditions.next();

            queryConditions.append(pair.getKey()).append(" = ").append(":where").append(pair.getKey()).append(itConditions.hasNext() ? " AND " : " ");
            updateValues.put("where" + pair.getKey(), pair.getValue());

            itConditions.remove(); // avoids a ConcurrentModificationException
        }

        String query = String.format("UPDATE %s SET %s WHERE %s", table, queryValues, queryConditions);

        return execute(query, updateValues);
    }

    /**
     * Delete data from one or more records inside a table from the database.
     *
     * @param table The table to delete data from.
     * @param conditions The conditions for deleting the data.
     *
     * @return Whether the data was successfully deleted or not.
     */
    public static boolean delete(String table, HashMap<String, String> conditions) {
        HashMap<String, String> updateValues = new HashMap<>();
        StringBuilder queryConditions = new StringBuilder();
        Iterator<Map.Entry<String, String>> itConditions = conditions.entrySet().iterator();
        while (itConditions.hasNext()) {
            Map.Entry<String, String> pair = itConditions.next();

            queryConditions.append(pair.getKey()).append(" = ").append(":where").append(pair.getKey()).append(itConditions.hasNext() ? " AND " : " ");
            updateValues.put("where" + pair.getKey(), pair.getValue());

            itConditions.remove(); // avoids a ConcurrentModificationException
        }

        String query = String.format("DELETE FROM %s WHERE %s", table, queryConditions);

        return execute(query, updateValues);
    }

    /**
     * Selects data from the database.
     *
     * @param query The query to be executed.
     * @param selectFields The fields to be selected from the returned data.
     * @param conditions The conditions of the query.
     *
     * @return An array list with the data of the result set.
     */
    private static ArrayList<HashMap<String, String>> executeQuery(String query, ArrayList<String> selectFields, HashMap<String, String> conditions) {
        try {
            Connection connection = new Connection();
            NamedParamStatement stmt = new NamedParamStatement(connection.get(), query);
            if (conditions != null && stmt.fields() != conditions.size()) {
                throw new RuntimeException("All specified conditions must be used inside the query.");
            }

            stmt.setValues(conditions);
            ArrayList<HashMap<String, String>> results = resultSetToArray(selectFields, stmt.executeQuery());
            stmt.close();
            connection.close();

            return results;
        } catch(Exception e) {
            System.out.println("An error occurred while executing this query.");
            if (Boolean.parseBoolean(config.get("debug"))) {
                System.out.println(e.getMessage());
            }
            System.exit(-1);
        }

        return null;
    }

    /**
     * Executes a query.
     *
     * @param query The query.
     * @param queryValues The values of the query.
     *
     * @return Whether the query was executed successfully or not.
     */
    private static boolean execute(String query, HashMap<String, String> queryValues) {
        try {
            Connection connection = new Connection();
            NamedParamStatement stmt = new NamedParamStatement(connection.get(), query);
            if (stmt.fields() != queryValues.size()) {
                throw new RuntimeException("All specified values must be used inside the query.");
            }

            stmt.setValues(queryValues);
            stmt.execute();
            stmt.close();
            connection.close();

            return true;
        } catch(Exception e) {
            System.out.println("An error occurred while executing this query.");
            if (Boolean.parseBoolean(config.get("debug"))) {
                System.out.println(e.getMessage());
            }
            System.exit(-1);
        }

        return false;
    }

    /**
     * Renders the result to array.
     *
     * @param selectFields The fields to be selected from the returned data.
     * @param rs The result set.
     *
     * @return An array list with the data of the result set.
     */
    private static ArrayList<HashMap<String, String>> resultSetToArray(ArrayList<String> selectFields, ResultSet rs) throws SQLException {
        ArrayList<HashMap<String, String>> results = new ArrayList<>();
        while (rs.next()) {
            HashMap<String, String> selectedValues = new HashMap<>();
            for (String field : selectFields) {
                selectedValues.put(field, rs.getString(field));
            }

            results.add(selectedValues);
        }

        return results;
    }

}
