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
    public String getTabTitle() {
        return "Item 2";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void instantiate() {
        int defaultGridWidth = gridBagConstraints.gridwidth;

        // Row 0 - Title
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        this.add(new JLabel("Inloggen", JLabel.CENTER), gridBagConstraints);

        gridBagConstraints.gridwidth = defaultGridWidth;

        // Row 1 - Username
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        this.add(new JLabel("Gebruikersnaam"), gridBagConstraints);

        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.gridx = 1;
        this.add(new TextField(), gridBagConstraints);

        // Row 2 - Password
        gridBagConstraints.insets = new Insets(5, 0, 0, 0);
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        this.add(new JLabel("Wachtwoord"), gridBagConstraints);

        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.gridx = 1;
        this.add(new TextField(), gridBagConstraints);
    }

}
