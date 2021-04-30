package UI.Panels;

import Authenthication.User;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.ArrayList;

/**
 * Provides a base panel for raw lists.
 */
public abstract class JPanelRawListBase extends JPanelBase implements ListSelectionListener {

    protected ArrayList<Object> listItems;

    protected JPanel preview;
    protected JList<String> list;
    protected JSplitPane splitPane;

    /**
     * Creates a new list panel.
     *
     * @param user The user.
     */
    public JPanelRawListBase(User user) {
        super(user);
    }

    /**
     * Gets the title of the panel.
     *
     * @return The title.
     */
    public abstract String getTitle();

    /**
     * {@inheritDoc}
     */
    @Override
    public void instantiate() {
        this.listItems = this.getRawListItems();

        Insets defaultInsets = this.gridBagConstraints.insets;
        int defaultGridWidth = this.gridBagConstraints.gridwidth;

        // Row 0 - Title
        this.gridBagConstraints.insets = new Insets(5, 0, 15, 0);
        this.gridBagConstraints.gridx = 0;
        this.gridBagConstraints.gridy = 0;
        this.gridBagConstraints.gridwidth = 2;
        this.add(new JLabel(this.getTitle(), JLabel.CENTER), this.gridBagConstraints);

        this.gridBagConstraints.insets = defaultInsets;
        this.gridBagConstraints.gridwidth = defaultGridWidth;

        this.list = new JList<>(this.getListLabels());
        this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.list.setSelectedIndex(this.getSelectedItemIndex());
        this.list.addListSelectionListener(this);

        this.preview = new JPanel();
        JScrollPane listScrollPane = new JScrollPane(this.list);
        JScrollPane previewScrollPane = new JScrollPane(this.preview);

        // Create a split pane with the two scroll panes in it.
        this.splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScrollPane, previewScrollPane);
        this.splitPane.setOneTouchExpandable(true);
        this.splitPane.setDividerLocation(150);

        // Provide minimum sizes for the two components in the split pane.
        listScrollPane.setMinimumSize(new Dimension(100, this.panelHeight - 50));
        previewScrollPane.setMinimumSize(new Dimension(this.panelWidth, this.panelHeight - 50));

        // Provide a preferred size for the split pane.
        this.splitPane.setPreferredSize(new Dimension(this.panelWidth, this.panelHeight - 50));

        this.updateRawListItemPreview(this.listItems.get(this.list.getSelectedIndex()));

        this.gridBagConstraints.gridx = 0;
        this.gridBagConstraints.gridy = 1;
        this.add(this.splitPane, gridBagConstraints);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
        JList<String> list = (JList<String>) e.getSource();

        this.preview.removeAll();
        this.updateRawListItemPreview(this.listItems.get(list.getSelectedIndex()));
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
            labels[delta] = this.getRawListItemLabel(this.listItems.get(delta));
        }

        return labels;
    }

    /**
     * Gets the list items.
     *
     * @return The list items.
     */
    protected abstract ArrayList<Object> getRawListItems();

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
    protected abstract String getRawListItemLabel(Object listItem);

    /**
     * Updates the preview of the list.
     *
     * @param listItem The entity.
     */
    protected abstract void updateRawListItemPreview(Object listItem);

}
