package UI.Components;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Provides a table component.
 */
public class Table {

    private ArrayList<ArrayList<String>> data;
    private ArrayList<String> columns;

    /**
     * Creates a new table.
     */
    public Table() {
        this.data = new ArrayList<>();
        this.columns = new ArrayList<>();
    }

    /**
     * Creates a new table.
     *
     * @param columns The columns.
     */
    public Table(ArrayList<String> columns) {
        this.columns = columns;
    }

    /**
     * Creates a new table.
     *
     * @param data The data.
     * @param columns The columns.
     */
    public Table(ArrayList<ArrayList<String>> data, ArrayList<String> columns) {
        this.data = data;
        this.columns = columns;
    }

    /**
     * Renders the table to a component.
     *
     * @return Table component.
     */
    public JScrollPane render() {
        String[][] tableData = new String[this.data.size()][];
        for (int delta = 0; delta < this.data.size(); delta++) {
            ArrayList<String> row = this.data.get(delta);
            tableData[delta] = row.toArray(new String[0]);
        }

        JTable table = new JTable(tableData, this.columns.toArray());
        table.removeEditor();
        table.setBounds(30, 40, 200, 300);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setSize(500, 400);

        return scrollPane;
    }

    /**
     * Adds a column to the table header.
     *
     * @param column The name of the column.
     */
    public void addColumn(String column) {
        this.columns.add(column);
    }

    /**
     * Adds a row to the data of the table.
     *
     * @param data The data of the row.
     */
    public void addRow(ArrayList<String> data) {
        this.data.add(data);
    }

}
