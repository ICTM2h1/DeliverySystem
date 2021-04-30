package UI;

import Authenthication.User;
import UI.Panels.JPanelBase;

import javax.swing.*;

public class TestPanel extends JPanelBase {

    public TestPanel(User user) {
        super(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle() {
        return "Item 2";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void instantiate() {
        this.add(new JTextField("TextField", 20));
    }

}
