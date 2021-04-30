package UI;

import Authenthication.User;
import javax.swing.event.ListSelectionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Provides a base panel for lists.
 */
public abstract class JPanelListBase extends JPanelRawListBase implements ListSelectionListener {

    /**
     * Creates a new list panel.
     *
     * @param user The user.
     */
    public JPanelListBase(User user) {
        super(user);
    }

    /**
     * Gets the list items.
     *
     * @return The list items.
     */
    protected abstract ArrayList<LinkedHashMap> getListItems();

    /**
     * Gets the label of an entity.
     *
     * @param listItem The entity.
     *
     * @return The label.
     */
    protected abstract String getListItemLabel(LinkedHashMap listItem);

    /**
     * Updates the preview of the list.
     *
     * @param listItem The entity.
     */
    protected abstract void updateListItemPreview(LinkedHashMap listItem);

}
