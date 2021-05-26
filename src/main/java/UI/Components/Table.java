package UI.Components;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.ArrayList;

/**
 * Provides a table component.
 */
public class Table {

    private JTable table;

    private final int width, height;
    private static boolean isEditable = false;

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
     * Initializes the table.
     */
    public void initializeTable() {
        String[][] tableData = new String[this.data.size()][];
        for (int delta = 0; delta < this.data.size(); delta++) {
            ArrayList<String> row = this.data.get(delta);
            tableData[delta] = row.toArray(new String[0]);
        }

        this.table = new JTable(tableData, this.columns.toArray()) {
            /**
             * {@inheritDoc}
             */
            @Override
            public boolean isCellEditable(int row, int column) {
                return Table.isEditable;
            }
        };
    }

    /**
     * Renders the table to a component.
     *
     * @return Table component.
     */
    public Component render() {
        if (this.table == null) {
            this.initializeTable();
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(this.width, this.height));

        return scrollPane;
    }

    /**
     * Sets column width.
     *
     * @param index The index of the column.
     * @param width The width of the column.
     */
    public void setColumnWidth(int index, int width) {
        this.table.getColumnModel().getColumn(index).setMaxWidth(width);
    }

    /**
     * Sets the cell renderer.
     *
     * @param index The index of the column.
     * @param tableCellRenderer The cell renderer.
     */
    public void setCellRenderer(int index, TableCellRenderer tableCellRenderer) {
        this.table.getColumnModel().getColumn(index).setCellRenderer(tableCellRenderer);
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
