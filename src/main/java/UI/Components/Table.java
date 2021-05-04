package UI.Components;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Provides a table component.
 */
public class Table {

    protected final int width, height;
    protected static boolean isEditable = false;

    private final ArrayList<ArrayList<String>> data;
    private final ArrayList<String> columns;

    /**
     * Creates a new table.
     */
    public Table() {
        this(500, 350);
    }

    /**
     * Creates a new table.
     */
    public Table(int width, int height) {
        this.data = new ArrayList<>();
        this.columns = new ArrayList<>();
        this.width = width;
        this.height = height;
    }

    /**
     * Renders the table to a component.
     *
     * @return Table component.
     */
    public Component render() {
        String[][] tableData = new String[this.data.size()][];
        for (int delta = 0; delta < this.data.size(); delta++) {
            ArrayList<String> row = this.data.get(delta);
            tableData[delta] = row.toArray(new String[0]);
        }

        JTable table = new JTable(tableData, this.columns.toArray()) {
            /**
             * {@inheritDoc}
             */
            @Override
            public boolean isCellEditable(int row, int column) {
                return Table.isEditable;
            }
        };

        table.setPreferredSize(new Dimension(this.width, this.height - 50));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(this.width, this.height));

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

    /**
     * Makes a table editable.
     */
    public void setEditable() {
        isEditable = true;
    }

}
