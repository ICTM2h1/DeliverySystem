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

    protected ArrayList<HashMap<String, String>> listItems;

    protected JPanel preview;
    protected JList<String> list;
    protected JSplitPane splitPane;

    /**
     * Creates a new list panel.
     *
     * @param user The user.
     */
    public JPanelListBase(User user) {
        super(user);
    }

    @Override
    public void instantiate() {
        this.listItems = this.getListItems();

        this.list = new JList<>(this.getListLabels());
        this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.list.setSelectedIndex(this.getSelectedItemIndex());
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
            HashMap<String, String> entity = this.listItems.get(delta);
            labels[delta] = this.getListItemLabel(entity);
        }

        return labels;
    }

    /**
     * Gets the list items.
     *
     * @return The list items.
     */
    protected abstract ArrayList<HashMap<String, String>> getListItems();

    /**
     * Gets the index of the selected list item.
     *
     * @return The list item index.
     */
    protected int getSelectedItemIndex() {
        return 0;
    }

    /**
     * Gets the label of an entity.
     *
     * @param listItem The entity.
     *
     * @return The label.
     */
    protected abstract String getListItemLabel(HashMap<String, String> listItem);

    /**
     * Updates the preview of the list.
     *
     * @param listItem The entity.
     */
    protected abstract void updateListItemPreview(HashMap<String, String> listItem);

}
