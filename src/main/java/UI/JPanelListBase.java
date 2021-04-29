package UI;

import Authenthication.User;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Provides a base panel for lists.
 */
public abstract class JPanelListBase extends JPanelBase implements ListSelectionListener {

    protected ArrayList<HashMap<String, String>> entities;

    protected JPanel preview;
    protected final JList<String> list;
    protected JSplitPane splitPane;

    /**
     * Creates a new list panel.
     *
     * @param entities The entities.
     */
    public JPanelListBase(ArrayList<HashMap<String, String>> entities, User user) {
        super(user);
        this.entities = entities;

        this.list = new JList<>(this.getListLabels());
        this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.list.setSelectedIndex(0);
        this.list.addListSelectionListener(this);

        JScrollPane listScrollPane = new JScrollPane(this.list);
        this.preview = new JPanel();

        JScrollPane previewScrollPane = new JScrollPane(this.preview);

        // Create a split pane with the two scroll panes in it.
        this.splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScrollPane, previewScrollPane);
        this.splitPane.setOneTouchExpandable(true);
        this.splitPane.setDividerLocation(150);

        // Provide minimum sizes for the two components in the split pane.
        Dimension minimumSize = new Dimension(100, 50);
        listScrollPane.setMinimumSize(minimumSize);
        previewScrollPane.setMinimumSize(minimumSize);

        // Provide a preferred size for the split pane.
        this.splitPane.setPreferredSize(new Dimension(400, 200));
        this.updatePreview(this.entities.get(this.list.getSelectedIndex()));

        this.add(this.splitPane);
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        JList<String> list = (JList<String>) e.getSource();

        this.preview.removeAll();
        this.updatePreview(this.entities.get(list.getSelectedIndex()));
        this.preview.updateUI();
    }

    /**
     * Gets the labels of the list items.
     *
     * @return The list item labels.
     */
    protected String[] getListLabels() {
        String[] labels = new String[this.entities.size()];
        for (int delta = 0; delta < this.entities.size(); delta++) {
            HashMap<String, String> entity = this.entities.get(delta);
            labels[delta] = this.getEntityLabel(entity);
        }

        return labels;
    }

    /**
     * Gets the label of an entity.
     *
     * @param entity The entity.
     *
     * @return The label.
     */
    protected abstract String getEntityLabel(HashMap<String, String> entity);

    /**
     * Updates the preview of the list.
     *
     * @param entity The entity.
     */
    protected abstract void updatePreview(HashMap<String, String> entity);

}
