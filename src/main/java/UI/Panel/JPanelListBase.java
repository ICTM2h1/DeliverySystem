package UI.Panel;

import Authenthication.User;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Provides a base panel for lists.
 */
public abstract class JPanelListBase extends JPanelBase implements ListSelectionListener {

    protected ArrayList<LinkedHashMap<String, String>> listItems;

    protected JPanel preview;
    protected JList<String> list;
    protected JSplitPane splitPane;

    /**
     * Creates a new panel list object.
     */
    public JPanelListBase(User user) {
        super(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void instantiate() {
        this.listItems = this.getListItems();

        this.list = new JList<>(this.getListLabels());
        this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.list.setSelectedIndex(this.getSelectedListItemIndex());
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
        this.updateListItemPreview(this.listItems.get(this.list.getSelectedIndex()));

        this.add(this.splitPane);
    }

    /**
     * Gets the list items.
     *
     * @return The list items.
     */
    protected abstract ArrayList<LinkedHashMap<String, String>> getListItems();

    /**
     * Gets the index of the default selected list item.
     *
     * @return The index of list item.
     */
    protected int getSelectedListItemIndex() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
        JList<String> list = (JList<String>) e.getSource();

        this.preview.removeAll();
        this.updateListItemPreview(this.listItems.get(list.getSelectedIndex()));
        this.preview.updateUI();
    }

    /**
     * Gets the labels of the list items.
     *
     * @return The list item labels.
     */
    protected String[] getListLabels() {
        String[] labels = new String[this.listItems.size()];
        for (int delta = 0; delta < this.listItems.size(); delta++) {
            LinkedHashMap<String, String> entity = this.listItems.get(delta);
            labels[delta] = this.getListItemLabel(entity);
        }

        return labels;
    }

    /**
     * Gets the label of an entity.
     *
     * @param listItem The entity.
     *
     * @return The label.
     */
    protected abstract String getListItemLabel(LinkedHashMap<String, String> listItem);

    /**
     * Updates the preview of the list.
     *
     * @param listItem The entity.
     */
    protected abstract void updateListItemPreview(LinkedHashMap<String, String> listItem);

}
