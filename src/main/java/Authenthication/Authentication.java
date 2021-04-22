package Authenthication;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.BorderFactory;
import System.Config.Config;
import System.Database.Query;
import at.favre.lib.crypto.bcrypt.BCrypt;

public class Authentication extends JFrame implements ActionListener {

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
        if (username != null && !username.equals("")) {
            // Check if password is set
            if (password != null && !password.equals("")) {

                // Tries to execute a SQL-query
                try {
                    Config config = Config.getInstance();

                    // Fields that are going to be fetched
                    ArrayList<String> selectFields = new ArrayList<>();
                    selectFields.add("LogonName");
                    selectFields.add("HashedPassword");

                    // Fetch data from a specific LogonName
                    HashMap<String, String> conditions = new HashMap<>();
                    conditions.put("LogonName", username);

                    // SELECT-query
                    ArrayList<HashMap<String, String>> results = Query.select("SELECT * FROM people WHERE LogonName = :LogonName", selectFields, conditions);

                    String HashedDatabasePassword = (String) results.get(0).get("HashedPassword");

                    // Check if database hash matches with entered password
                    if (BCrypt.verifyer().verify(password.toCharArray(), HashedDatabasePassword).verified) {
                        return true;
                    }
                // Catches potential SQL-query failures
                } catch (Exception throwable) {
                    return false;
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
            errorMessage.setText("Welkom");
            errorMessage.setForeground(Color.black);
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