package UI.Panels;

import java.awt.*;

/**
 * Provides a panel for list item previews.
 */
public class JPanelListPreview extends JPanelBase {

    private final String title;

    /**
     * Creates a new list preview.
     *
     * @param title The title.
     */
    protected JPanelListPreview(String title) {
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

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension getSize() {
        Dimension size = super.getSize();

        size.height -= 100;

        return size;
    }

}
