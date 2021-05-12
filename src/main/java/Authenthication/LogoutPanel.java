package Authenthication;

import UI.Panels.JPanelBase;
import main.Main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Provides a logout panel.
 */
public class LogoutPanel extends JPanelBase implements ActionListener {

    private JButton logoutButton;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle() {
        return "Uitloggen";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void instantiate() {
        this.logoutButton = new JButton("Uitloggen");
        this.logoutButton.addActionListener(this);
        this.addComponent(logoutButton, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.logoutButton) {
            Main.restart();
        }
    }

}
