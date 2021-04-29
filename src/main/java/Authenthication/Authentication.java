package Authenthication;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.BorderFactory;

import Home.AdminHome;
import Home.DelivererHome;
import System.Database.Query;
import at.favre.lib.crypto.bcrypt.BCrypt;

public class Authentication extends JFrame implements ActionListener {

    private HashMap<String, String> loginResultset;
    private JPanel panel;
    private JLabel usernameLabel, passwordLabel, errorMessage;
    private JTextField usernameTextfield;
    private JPasswordField passwordPasswordfield;
    private JButton loginButton;

    public static void main(String[] args) {
        new Authentication();
    }

    Authentication() {
        // Username
        usernameLabel = new JLabel();
        usernameLabel.setText("Gebruikersnaam:");
        usernameTextfield = new JTextField();
        usernameTextfield.addActionListener(enterPressedUsername);

        // Password
        passwordLabel = new JLabel();
        passwordLabel.setText("Wachtwoord:");
        passwordPasswordfield = new JPasswordField();
        passwordPasswordfield.addActionListener(enterPressedPassword);

        // Submit
        errorMessage = new JLabel();
        loginButton = new JButton("Aanmelden");

        panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        // Row 0 - Username
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 0;
        panel.add(usernameLabel, c);

        c.weightx = 0.5;
        c.gridx = 1;
        panel.add(usernameTextfield, c);

        // Row 1 - Password
        c.insets = new Insets(5, 0, 0, 0);
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 1;
        panel.add(passwordLabel, c);

        c.weightx = 0.5;
        c.gridx = 1;
        panel.add(passwordPasswordfield, c);

        // Row 2 - error message
        c.insets = new Insets(5, 0, 5, 0);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 2;
        panel.add(errorMessage, c);

        // Row 3 - Login button
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 3;
        panel.add(loginButton, c);

        // Display panel
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginButton.addActionListener(this);
        add(panel, BorderLayout.CENTER);
        setTitle("Awesomely NerdyGadgets - Routebepaling portaal");
        setSize(650, 175);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setVisible(true);

    }

    /**
     * Validates user input.
     *
     * @return boolean validation successful
     */
    private boolean validateUser() {
        String username = usernameTextfield.getText().trim();
        String password = String.valueOf(passwordPasswordfield.getPassword()).trim();

        // Check if username is set
        if (!username.isEmpty()) {
            // Check if password is set
            if (!password.isEmpty()) {
                // Fields that are going to be fetched
                ArrayList<String> selectFields = new ArrayList<>();
                selectFields.add("LogonName");
                selectFields.add("HashedPassword");
                selectFields.add("FullName");
                selectFields.add("isSalesperson");

                // Fetch data from a specific LogonName
                HashMap<String, String> conditions = new HashMap<>();
                conditions.put("LogonName", username);

                // SELECT-query
                HashMap<String, String> results = Query.selectFirst("SELECT * FROM people WHERE LogonName = :LogonName", selectFields, conditions);

                if (results == null) {
                    return false;
                }

                String HashedDatabasePassword = results.get("HashedPassword");

                // Check if database hash matches with entered password
                if (BCrypt.verifyer().verify(password.toCharArray(), HashedDatabasePassword).verified) {
                    this.loginResultset = results;
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
            UserRole userRole = UserRole.valueOf(Integer.parseInt(loginResultset.get("isSalesperson")));

            // Check if userrole is valid.
            if (userRole != null) {
                String username = loginResultset.get("FullName");
                String HashedDatabasePassword = loginResultset.get("HashedPassword");

                // Create a new user (Deliverer or Admin)
                User user = userRole.createUser(username, HashedDatabasePassword, userRole);

                if (userRole.isAdmin()) {
                    AdminHome ah = new AdminHome();
                } else if (userRole.isDeliverer()) {
                    DelivererHome dh = new DelivererHome();
                }
                dispose();
            } else {
                errorMessage.setText("Gebruiker niet ingesteld als bezorger of beheerder!");
                errorMessage.setForeground(Color.red);
            }
        } else {
            errorMessage.setText("Gebruikersnaam en wachtwoord combinatie is onjuist!");
            errorMessage.setForeground(Color.red);
        }
    }

    /**
     * Detects login button click.
     *
     * @param e ActionEvent clicked button.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            handleLogin();
        }
    }

    /**
     * Enter key on focus username field acts as a TAB-key.
     */
    Action enterPressedUsername = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (usernameTextfield.getText().trim().length() > 0)
                passwordPasswordfield.requestFocus();
        }
    };

    /**
     * Enter key on focus passwordfield acts as a submit button.
     */
    Action enterPressedPassword = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            handleLogin();
        }
    };
}