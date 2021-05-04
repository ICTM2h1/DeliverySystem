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

    public static ArrayList<Object> listItemsCopy;
    protected ArrayList<Object> listItems = new ArrayList<>();

    protected JPanelListPreview preview;
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
     * {@inheritDoc}
     */
    @Override
    public void instantiate() {
        ArrayList<Object> rawListItems = this.getRawListItems();
        if (rawListItems != null && !rawListItems.isEmpty()) {
            this.listItems = rawListItems;
            listItemsCopy = listItems;
        }

        if (this.listItems == null || this.listItems.isEmpty()) {
            this.addNoResultsComponent();
            return;
        }


        this.list = new JList<>(this.getListLabels());
        this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.list.setSelectedIndex(this.getSelectedItemIndex());
        this.list.addListSelectionListener(this);
        this.list.setSelectionBackground(Color.BLUE);
        this.list.setSelectionForeground(Color.WHITE);
        this.list.setCellRenderer(this.getListCellRenderer());

        this.preview = new JPanelListPreview(this.user, this.getListPreviewTitle());
        this.preview.instantiate();
        this.preview.updateUI();

        JScrollPane listScrollPane = new JScrollPane(this.list);
        JScrollPane previewScrollPane = new JScrollPane(this.preview);
        if (!this.hasVerticalScrollbar()) {
            previewScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        }

        // Create a split pane with the two scroll panes in it.
        this.splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScrollPane, previewScrollPane);
        this.splitPane.setOneTouchExpandable(true);
        this.splitPane.setDividerLocation(150);

        // Provide minimum sizes for the two components in the split pane.
        listScrollPane.setMinimumSize(new Dimension(150, this.panelHeight - 50));
        previewScrollPane.setMinimumSize(new Dimension(this.panelWidth, this.panelHeight - 50));

        // Provide a preferred size for the split pane.
        this.splitPane.setPreferredSize(new Dimension(this.panelWidth, this.panelHeight - 50));

        this.updateRawListItemPreview(this.listItems.get(this.list.getSelectedIndex()));

        this.addListComponent(this.splitPane);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
        JList<String> list = (JList<String>) e.getSource();

        this.preview.removeAll();
        this.preview.instantiate();
        this.updateRawListItemPreview(this.listItems.get(list.getSelectedIndex()));
        this.preview.updateUI();
    }

    /**
     * Adds the list component.
     *
     * @param splitPane The split pane.
     */
    protected void addListComponent(JSplitPane splitPane) {
        this.addComponent(splitPane, true);
    }

    /**
     * Determines if the split pane has a vertical scrollbar.
     *
     * @return Whether the panel has a vertical scrollbar or not.
     */
    protected boolean hasVerticalScrollbar() {
        return true;
    }

    /**
     * Gets the title of the list preview.
     *
     * @return The title.
     */
    protected String getListPreviewTitle() {
        return this.getTitle();
    }

    /**
     * Gets the no results text.
     *
     * @return The no results text.
     */
    protected String getNoResultsText() {
        return "Er zijn geen gegevens gevonden.";
    }

    /**
     * Adds a no results component.
     */
    protected void addNoResultsComponent() {
        this.addComponent(new JLabel(this.getNoResultsText(), JLabel.CENTER), true);
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
     * Gets the list cell renderer.
     *
     * @return The list cell renderer.
     */
    protected ListCellRenderer<Object> getListCellRenderer() {
        return new DefaultListCellRenderer();
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
