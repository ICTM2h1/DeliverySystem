package UI.Dialogs;

import DeliveryRoute.DeliveryOrderPoint;
import UI.Panels.JPanelBase;
import UI.Panels.JPanelListPreview;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Provides a base panel for raw lists.
 */
public abstract class JDialogRawListBase extends JDialogBase implements ListSelectionListener {

    public static ArrayList<Object> listItemsCopy;
    public Object startingPoint;

    protected ArrayList<Object> listItems;

    protected JPanelListPreview preview;
    protected JList<String> list;
    protected JSplitPane splitPane;

    public JDialogRawListBase(JFrame frame, boolean modal, ArrayList<Object> rawListItems, Object startingPoint, String[] labels, String title) {
        super(frame, modal, 740, 581);
        this.setTitle(title);

        this.startingPoint = startingPoint;
        this.listItems = rawListItems;
        listItemsCopy = rawListItems;

        if (this.listItems == null || this.listItems.isEmpty()) {
            return;
        }

        this.list = new JList<>(labels);
        this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.list.setSelectedIndex(this.getSelectedItemIndex());
        this.list.addListSelectionListener(this);
        this.list.setSelectionBackground(Color.BLUE);
        this.list.setSelectionForeground(Color.WHITE);
        this.list.setCellRenderer(this.getListCellRenderer());

        this.preview = new JPanelListPreview(null);
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
        listScrollPane.setMinimumSize(new Dimension(150, panelHeight - 100));
        previewScrollPane.setMinimumSize(new Dimension(panelWidth - 100, panelHeight - 100));

        // Provide a preferred size for the split pane.
        this.splitPane.setPreferredSize(new Dimension(panelWidth - 100, panelHeight - 100));

        this.updateRawListItemPreview(this.listItems.get(this.list.getSelectedIndex()));

        this.addListComponent(this.splitPane);
        this.setVisible(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
        JList<String> list = (JList<String>) e.getSource();

        this.updateRawPreview(this.listItems.get(list.getSelectedIndex()));
    }

    /**
     * Updates the preview.
     *
     * @param listItem The updated list item.
     */
    protected void updateRawPreview(Object listItem) {
        this.preview.removeAll();
        this.preview.instantiate();
        this.updateRawListItemPreview(listItem);
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
     * Gets the list cell renderer.
     *
     * @return The list cell renderer.
     */
    protected ListCellRenderer<Object> getListCellRenderer() {
        return new DefaultListCellRenderer();
    }

    /**
     * Gets the index of the selected list item.
     *
     * @return The list item index.
     */
    protected int getSelectedItemIndex() {
        return 0;
    }

    /**
     * Updates the preview of the list.
     *
     * @param listItem The entity.
     */
    protected abstract void updateRawListItemPreview(Object listItem);

}
