package UI.Panels;

import Authenthication.User;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Provides a base panel for lists.
 */
public abstract class JPanelListBase extends JPanelRawListBase {

    /**
     * Creates a new list panel.
     *
     * @param user The user.
     */
    public JPanelListBase(User user) {
        super(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ArrayList<Object> getRawListItems() {
        return new ArrayList<>(this.getListItems());
    }

    /**
     * Gets the list items.
     *
     * @return The list items.
     */
    protected abstract ArrayList<LinkedHashMap<String, String>> getListItems();

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getRawListItemLabel(Object listItem) {
        return this.getListItemLabel(new LinkedHashMap<>((LinkedHashMap<String, String>) listItem));
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
     * {@inheritDoc}
     */
    @Override
    protected void updateRawListItemPreview(Object listItem) {
        this.updateListItemPreview(new LinkedHashMap<>((LinkedHashMap<String, String>) listItem));
    }

    /**
     * Updates the preview of the list.
     *
     * @param listItem The entity.
     */
    protected abstract void updateListItemPreview(LinkedHashMap<String, String> listItem);

}
