package Crud;

import System.Database.Query;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Provides a class for basis crud actions.
 */
public abstract class CrudBase {

    protected ArrayList<String> selectFields = new ArrayList<>();

    protected LinkedHashMap<String, String> values = new LinkedHashMap<>(), parameters = new LinkedHashMap<>();

    protected String table, primaryKey;

    /**
     * Constructs a new crud object.
     *
     * @param table The table to be used inside the queries.
     */
    public CrudBase(String table) {
        this(table, table + "ID");
    }

    /**
     * Constructs a new crud object.
     *
     * @param table The table to be used inside the queries.
     * @param primaryKey The primary key of the table.
     */
    public CrudBase(String table, String primaryKey) {
        this.table = table;
        this.primaryKey = primaryKey;

        this.addSelectField(primaryKey);
    }

    /**
     * Gets the first record from the database for a given id.
     *
     * @param id The ID of the record to filter on.
     *
     * @return A hash map with the selected row.
     */
    public LinkedHashMap<String, String> get(int id) {
        this.addCondition(this.primaryKey, String.valueOf(id));

        return Query.selectFirst(
            String.format("SELECT * FROM %s WHERE %s = :%s", this.table, this.primaryKey, this.primaryKey),
            this.selectFields, this.parameters
        );
    }

    /**
     * Gets the first record from the database.
     *
     * @param query The query.
     *
     * @return A hash map with the selected row.
     */
    public LinkedHashMap<String, String> get(String query) {
        return Query.selectFirst(query, this.selectFields, this.parameters);
    }

    /**
     * Gets all records from the database.
     *
     * @return An array list with the selected rows.
     */
    public ArrayList<LinkedHashMap<String, String>> all() {
        return Query.select("SELECT * FROM " + this.table, this.selectFields);
    }

    /**
     * Gets all records from the database.
     *
     * @param query The query.
     *
     * @return An array list with the selected rows.
     */
    public ArrayList<LinkedHashMap<String, String>> all(String query) {
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
            throw new RuntimeException("Values or parameters cannot be empty while updating data.");
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
            throw new RuntimeException("Parameters cannot be empty while deleting data.");
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
    public void addValues(LinkedHashMap<String, String> values) {
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
    public void addCondition(LinkedHashMap<String, String> conditions) {
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
    public void bindParams(LinkedHashMap<String, String> conditions) {
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
