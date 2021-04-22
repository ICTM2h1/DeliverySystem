package Crud;

import System.Database.Query;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Provides a class for basis crud actions.
 */
public abstract class CrudBase {

    protected ArrayList<String> selectFields = new ArrayList<>();

    protected HashMap<String, String> values = new HashMap<>();
    protected HashMap<String, String> parameters = new HashMap<>();

    protected String table;

    public CrudBase(String table) {
        this.table = table;
    }

    /**
     * Gets the first record from the database.
     *
     * @param query The query.
     *
     * @return A hash map with the selected row.
     */
    public HashMap<String, String> get(String query) {
        return Query.selectFirst(query, this.selectFields, this.parameters);
    }

    /**
     * Gets all records from the database.
     *
     * @param query The query.
     *
     * @return An array list with the selected rows.
     */
    public ArrayList<HashMap<String, String>> all(String query) {
        return Query.select(query, this.selectFields, this.parameters);
    }

    /**
     * Inserts data into the table.
     *
     * @return Whether the insert was successful or not.
     */
    public boolean insert() {
        if (this.values.isEmpty()) {
            throw new RuntimeException("Values cannot be empty while inserting data.");
        }

        boolean success = Query.insert(this.table, this.values);
        this.values.clear();
        return success;
    }

    /**
     * Updates data from the table.
     *
     * @return Whether the update was successfully or not.
     */
    public boolean update() {
        if (this.values.isEmpty() || this.parameters.isEmpty()) {
            throw new RuntimeException("Values or parameters cannot be empty while update data.");
        }

        boolean success = Query.update(this.table, this.values, this.parameters);
        this.values.clear();
        this.parameters.clear();
        return success;
    }

    /**
     * Deletes data from the table.
     *
     * @return Whether the delete was successfully or not.
     */
    public boolean delete() {
        if (this.parameters.isEmpty()) {
            throw new RuntimeException("Parameters cannot be empty while inserting data.");
        }

        boolean success = Query.delete(this.table, this.parameters);
        this.parameters.clear();
        return success;
    }

    /**
     * Adds fields to the select fields list.
     *
     * @param selectFields The fields to be selected.
     */
    public void addSelectFields(ArrayList<String> selectFields) {
        this.selectFields.addAll(selectFields);
    }

    /**
     * Adds a field to the selected fields list.
     *
     * @param field The name of the field.
     */
    public void addSelectField(String field) {
        this.selectFields.add(field);
    }

    /**
     * Adds values to the query.
     *
     * @param values The values.
     */
    public void addValues(HashMap<String, String> values) {
        this.values.putAll(values);
    }

    /**
     * Adds a value to the query.
     *
     * @param key The table column..
     * @param value The value of the filter.
     */
    public void addValue(String key, String value) {
        this.values.put(key, value);
    }

    /**
     * Adds conditions to the query.
     *
     * @param conditions The conditions.
     */
    public void addCondition(HashMap<String, String> conditions) {
        this.bindParams(conditions);
    }

    /**
     * Adds a condition to the query.
     *
     * @param key The table column..
     * @param value The value of the filter.
     */
    public void addCondition(String key, String value) {
        this.bindParam(key, value);
    }

    /**
     * Binds parameters to the query.
     *
     * @param conditions The parameters.
     */
    public void bindParams(HashMap<String, String> conditions) {
        this.parameters.putAll(conditions);
    }

    /**
     * Binds a parameter to the query.
     *
     * @param parameter The parameter to bind the value to.
     * @param value The value of the filter.
     */
    public void bindParam(String parameter, String value) {
        this.parameters.put(parameter, value);
    }

}
