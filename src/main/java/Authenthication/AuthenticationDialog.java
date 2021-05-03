package Authenthication;

import Crud.People;
import System.Config.Config;
import UI.Panels.JPanelBase;
import at.favre.lib.crypto.bcrypt.BCrypt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;

/**
 * Provides a dialog modal for authentication.
 */
public class AuthenticationDialog extends JDialog implements ActionListener {

    private User user;
    private LinkedHashMap<String, String> userResultSet;

    private final JLabel usernameLabel, passwordLabel, errorMessage;
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JButton loginButton;

    /**
     * Creates a new authentication dialog.
     *
     * @param frame The frame.
     * @param modal Determines if it is a modal?
     */
    public AuthenticationDialog(JFrame frame, boolean modal) {
        super(frame, modal);
        Config config = Config.getInstance();

        this.usernameLabel = new JLabel("Gebruikersnaam:");
        this.usernameField = new JTextField();
        this.usernameField.addActionListener(enterPressedUsername);

        this.passwordLabel = new JLabel("Wachtwoord:");
        this.passwordField = new JPasswordField();
        this.passwordField.addActionListener(enterPressedPassword);

        this.errorMessage = new JLabel("", JLabel.CENTER);
        this.loginButton = new JButton("Aanmelden");

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        int defaultGridWidth = gridBagConstraints.gridwidth;

        // Row 0 - Title
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        panel.add(new JLabel("Inloggen", JLabel.CENTER), gridBagConstraints);

        gridBagConstraints.gridwidth = defaultGridWidth;
        // Row 1 - Username
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panel.add(usernameLabel, gridBagConstraints);

        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.gridx = 1;
        panel.add(usernameField, gridBagConstraints);

        // Row 2 - Password
        gridBagConstraints.insets = new Insets(5, 0, 0, 0);
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        panel.add(passwordLabel, gridBagConstraints);

        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.gridx = 1;
        panel.add(passwordField, gridBagConstraints);

        // Row 3 - error message
        gridBagConstraints.insets = new Insets(5, 0, 5, 0);
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        panel.add(errorMessage, gridBagConstraints);

        // Row 4 - Login button
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        panel.add(loginButton, gridBagConstraints);

        // Display panel
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.loginButton.addActionListener(this);
        this.add(panel, BorderLayout.CENTER);
        this.setTitle(String.format("%s", config.get("app_title")).replace("+", " "));
        this.setSize(650, 175);
        panel.setBorder(JPanelBase.getDefaultBorder());
        this.setVisible(true);
    }

    /**
     * Validates user input.
     *
     * @return boolean validation successful
     */
    private boolean validateUser() {
        String username = this.usernameField.getText().trim();
        String password = String.valueOf(this.passwordField.getPassword()).trim();

        // Check if username is set
        if (!username.isEmpty()) {
            // Check if password is set
            if (!password.isEmpty()) {
                People people = new People();
                LinkedHashMap<String, String> results = people.getByUsername(username);
                if (results == null) {
                    return false;
                }

                String HashedDatabasePassword = results.get("HashedPassword");

                // Check if database hash matches with entered password
                if (BCrypt.verifyer().verify(password.toCharArray(), HashedDatabasePassword).verified) {
                    this.userResultSet = results;
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Handles the whole login part (validating and assigning data).
     */
    private void handleLogin() {
        if (validateUser()) {
            UserRole userRole = UserRole.valueOf(Integer.parseInt(this.userResultSet.get("Role")));

            // Check if user role is valid.
            if (userRole != null) {
                String username = this.userResultSet.get("FullName");
                String HashedDatabasePassword = this.userResultSet.get("HashedPassword");

                this.user = userRole.createUser(username, HashedDatabasePassword, userRole);
                dispose();
            } else {
                this.errorMessage.setText("Gebruiker niet ingesteld als bezorger of beheerder!");
                this.errorMessage.setForeground(Color.red);
            }
        } else {
            this.errorMessage.setText("Gebruikersnaam en wachtwoord combinatie is onjuist!");
            this.errorMessage.setForeground(Color.red);
        }
    }

    /**
     * Determines whether the user is authenticated or not.
     *
     * @return If user is authenticated.
     */
    public boolean isAuthenticated() {
        return this.user != null;
    }

    /**
     * Gets the user.
     *
     * @return The logged in user.
     */
    public User getUser() {
        return user;
    }

    /**
     * Detects login button click.
     *
     * @param e ActionEvent clicked button.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.loginButton) {
            handleLogin();
        }
    }

    /**
     * Enter key on focus username field acts as a TAB-key.
     */
    Action enterPressedUsername = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (usernameField.getText().trim().length() > 0) {
                passwordField.requestFocus();
            }
        }
    };

    /**
     * Enter key on focus password field acts as a submit button.
     */
    Action enterPressedPassword = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            handleLogin();
        }
    };
}