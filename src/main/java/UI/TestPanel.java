package UI;

import Authenthication.User;
import UI.Panel.JPanelBase;

import javax.swing.*;

public class TestPanel extends JPanelBase {

    public TestPanel(User user) {
        super(user);
        this.add(new JTextField("TextField", 20));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle() {
        return "Item 2";
    }

}
