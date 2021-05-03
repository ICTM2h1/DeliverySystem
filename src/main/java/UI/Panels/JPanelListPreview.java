package UI.Panels;

import Authenthication.User;

/**
 * Provides a panel for list item previews.
 */
public class JPanelListPreview extends JPanelBase {

    private final String title;

    /**
     * Creates a new list preview.
     *
     * @param user The user.
     */
    protected JPanelListPreview(User user, String title) {
        super(user);

        this.title = title;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle() {
        return this.title;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void instantiate() {
        this.addTitleComponent();
    }

}
