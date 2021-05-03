package UI.Panels;

import Authenthication.User;

import java.awt.*;

/**
 * Provides a panel for list item previews.
 */
public class JPanelListPreview extends JPanelBase {

    /**
     * Creates a new list preview.
     *
     * @param user The user.
     */
    protected JPanelListPreview(User user) {
        super(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void instantiate() {

    }

}
