package Authenthication;

import System.Error.SystemError;
import UI.Panels.JPanelBase;
import UI.Window;
import main.Main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.List;

/**
 * Provides a logout panel.
 */
public class LogoutPanel extends JPanelBase implements ActionListener {

    private JButton logoutButton;

    /**
     * Creates a new logout panel.
     *
     * @param user The user.
     */
    public LogoutPanel(User user) {
        super(user);
    }

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
