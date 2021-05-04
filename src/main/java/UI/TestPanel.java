package UI;

import Authenthication.User;
import UI.Panels.JPanelBase;

import javax.swing.*;
import java.awt.*;

public class TestPanel extends JPanelBase {

    public TestPanel(User user) {
        super(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle() {
        return "Test panel";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void instantiate() {
        this.addComponent(new JLabel("Gebruikersnaam"), true);
        this.addComponent(new TextField());

        this.gridBagConstraints.insets = new Insets(5, 0, 0, 0);
        this.gridBagConstraints.weightx = 0.5;
        this.addComponent(new JLabel("Wachtwoord"), true);
        this.addComponent(new TextField());
    }

}
