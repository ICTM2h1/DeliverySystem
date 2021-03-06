package System.Database;

import System.Error.SystemError;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Provides a class for interacting with the database.
 */
public class Query {

    /**
     * Selects data from the database.
     *
     * @param query The query to be executed.
     * @param selectFields The fields to be selected from the returned data.
     *
     * @return An array list with the data of the result set.
     */
    public static ArrayList<LinkedHashMap<String, String>> select(String query, ArrayList<String> selectFields) {
        return executeQuery(query, selectFields, null);
    }

    /**
     * Selects data from the database.
     *
     * @param query The query to be executed.
     * @param selectFields The fields to be selected from the returned data.
     *
     * @return An array list with the data of the result set.
     */
    public static LinkedHashMap<String, String> selectFirst(String query, ArrayList<String> selectFields) {
        return executeQueryFirst(query, selectFields, null);
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
    public static ArrayList<LinkedHashMap<String, String>> select(String query, ArrayList<String> selectFields, LinkedHashMap<String, String> conditions) {
        return executeQuery(query, selectFields, conditions);
    }

    /**
     * Selects data from the database and returns one row.
     *
     * @param query The query to be executed.
     * @param selectFields The fields to be selected from the returned data.
     * @param conditions The conditions of the query.
     *
     * @return An hash map with the data of the result set.
     */
    public static LinkedHashMap<String, String> selectFirst(String query, ArrayList<String> selectFields, LinkedHashMap<String, String> conditions) {
        return executeQueryFirst(query, selectFields, conditions);
    }

    /**
     * Inserts data into a table inside the database.
     *
     * @param table The table to insert data in.
     * @param values The data to be inserted into the database.
     *
     * @return Whether the data was successfully inserted or not.
     */
    public static boolean insert(String table, LinkedHashMap<String, String> values) {
        LinkedHashMap<String, String> insertValues = new LinkedHashMap<>();
        StringBuilder queryColumns = new StringBuilder();
        StringBuilder queryValues = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = values.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> pair = iterator.next();

            queryColumns.append(pair.getKey()).append(iterator.hasNext() ? ", " : " ");
            queryValues.append(":").append(pair.getKey()).append(iterator.hasNext() ? ", " : " ");
            insertValues.put(pair.getKey(), pair.getValue());

            iterator.remove(); // avoids a ConcurrentModificationException
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
    public static boolean update(String table, LinkedHashMap<String, String> values, LinkedHashMap<String, String> conditions) {
        LinkedHashMap<String, String> updateValues = new LinkedHashMap<>();
        StringBuilder queryValues = new StringBuilder();
        Iterator<Map.Entry<String, String>> iteratorValues = values.entrySet().iterator();
        while (iteratorValues.hasNext()) {
            Map.Entry<String, String> pair = iteratorValues.next();

            queryValues.append(pair.getKey()).append(" = ").append(":update").append(pair.getKey()).append(iteratorValues.hasNext() ? ", " : " ");
            updateValues.put("update" + pair.getKey(), pair.getValue());

            iteratorValues.remove(); // avoids a ConcurrentModificationException
        }

        StringBuilder queryConditions = new StringBuilder();
        Iterator<Map.Entry<String, String>> iteratorConditions = conditions.entrySet().iterator();
        while (iteratorConditions.hasNext()) {
            Map.Entry<String, String> pair = iteratorConditions.next();

            queryConditions.append(pair.getKey()).append(" = ").append(":where").append(pair.getKey()).append(iteratorConditions.hasNext() ? " AND " : " ");
            updateValues.put("where" + pair.getKey(), pair.getValue());

            iteratorConditions.remove(); // avoids a ConcurrentModificationException
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
    public static boolean delete(String table, LinkedHashMap<String, String> conditions) {
        LinkedHashMap<String, String> updateValues = new LinkedHashMap<>();
        StringBuilder queryConditions = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = conditions.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> pair = iterator.next();

            queryConditions.append(pair.getKey()).append(" = ").append(":where").append(pair.getKey()).append(iterator.hasNext() ? " AND " : " ");
            updateValues.put("where" + pair.getKey(), pair.getValue());

            iterator.remove(); // avoids a ConcurrentModificationException
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
    private static ArrayList<LinkedHashMap<String, String>> executeQuery(String query, ArrayList<String> selectFields, LinkedHashMap<String, String> conditions) {
        try {
            Connection connection = new Connection();
            NamedParamStatement statement = new NamedParamStatement(connection.get(), query);
            if (conditions != null && statement.fields() != conditions.size()) {
                throw new RuntimeException("All specified conditions must be used inside the query.");
            }

            statement.setValues(conditions);
            ArrayList<LinkedHashMap<String, String>> results = resultSetToArray(selectFields, statement.executeQuery());
            statement.close();
            connection.close();

            return results;
        } catch(Exception e) {
            SystemError.handle(e, "An error occurred while executing this query.");
        }

        return null;
    }

    /**
     * Selects data from the database and returns one row.
     *
     * @param query The query to be executed.
     * @param selectFields The fields to be selected from the returned data.
     * @param conditions The conditions of the query.
     *
     * @return An hash map with the data of the result set.
     */
    private static LinkedHashMap<String, String> executeQueryFirst(String query, ArrayList<String> selectFields, LinkedHashMap<String, String> conditions) {
        try {
            Connection connection = new Connection();
            NamedParamStatement statement = new NamedParamStatement(connection.get(), query);
            if (conditions != null && statement.fields() != conditions.size()) {
                throw new RuntimeException("All specified conditions must be used inside the query.");
            }

            statement.setValues(conditions);
            LinkedHashMap<String, String> results = resultSetToFirst(selectFields, statement.executeQuery());
            statement.close();
            connection.close();

            return results;
        } catch(Exception e) {
            SystemError.handle(e, "An error occurred while executing this query.");
        }

        return null;
    }

    /**
     * Executes a query.
     *
     * @param query The query.
     * @param values The values of the query.
     *
     * @return Whether the query was executed successfully or not.
     */
    private static boolean execute(String query, LinkedHashMap<String, String> values) {
        try {
            Connection connection = new Connection();
            NamedParamStatement statement = new NamedParamStatement(connection.get(), query);
            if (statement.fields() != values.size()) {
                throw new RuntimeException("All specified values must be used inside the query.");
            }

            statement.setValues(values);
            statement.execute();
            statement.close();
            connection.close();

            return true;
        } catch(Exception e) {
            SystemError.handle(e, "An error occurred while executing this query.");
        }

        return false;
    }

    /**
     * Renders the result to array.
     *
     * @param selectFields The fields to be selected from the returned data.
     * @param resultSet The result set.
     *
     * @return An array list with the data of the result set.
     */
    private static ArrayList<LinkedHashMap<String, String>> resultSetToArray(ArrayList<String> selectFields, ResultSet resultSet) throws SQLException {
        ArrayList<LinkedHashMap<String, String>> results = new ArrayList<>();
        while (resultSet.next()) {
            LinkedHashMap<String, String> selectedValues = new LinkedHashMap<>();
            for (String field : selectFields) {
                selectedValues.put(field, resultSet.getString(field));
            }

            results.add(selectedValues);
        }

        return results;
    }

    /**
     * Renders the result to array.
     *
     * @param selectFields The fields to be selected from the returned data.
     * @param resultSet The result set.
     *
     * @return An array list with the data of the result set.
     */
    private static LinkedHashMap<String, String> resultSetToFirst(ArrayList<String> selectFields, ResultSet resultSet) throws SQLException {
        boolean result = resultSet.next();
        if (!result) {
            return null;
        }

        LinkedHashMap<String, String> selectedValues = new LinkedHashMap<>();
        for (String field : selectFields) {
            selectedValues.put(field, resultSet.getString(field));
        }

        return selectedValues;
    }

}
