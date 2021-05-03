package UI.Components;

import javax.swing.*;
import java.util.ArrayList;

public class Table {

    private ArrayList<ArrayList<String>> data;
    private ArrayList<String> columns;

    public Table() {
        this.data = new ArrayList<>();
        this.columns = new ArrayList<>();
    }

    public Table(ArrayList<String> columns) {
        this.columns = columns;
    }

    public Table(ArrayList<ArrayList<String>> data, ArrayList<String> columns) {
        this.data = data;
        this.columns = columns;
    }

    public JScrollPane render() {
        String[][] tableData = new String[this.data.size()][];
        for (int i = 0; i < this.data.size(); i++) {
            ArrayList<String> row = this.data.get(i);
            tableData[i] = row.toArray(new String[0]);
        }

        JTable table = new JTable(tableData, this.columns.toArray());
        table.removeEditor();
        table.setBounds(30, 40, 200, 300);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setSize(500, 400);

        return scrollPane;
    }

    public void addColumn(String column) {
        this.columns.add(column);
    }

    public void addRow(ArrayList<String> data) {
        this.data.add(data);
    }

}
