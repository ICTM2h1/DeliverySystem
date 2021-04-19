import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.BorderFactory;


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

        // Password
        passwordLabel = new JLabel();
        passwordLabel.setText("Wachtwoord:");
        passwordPasswordfield = new JPasswordField();

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
        c.insets = new Insets(5,0,0,0);
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 1;
        panel.add(passwordLabel, c);

        c.weightx = 0.5;
        c.gridx = 1;
        panel.add(passwordPasswordfield, c);

        // Row 2 - error message
        c.insets = new Insets(5,0,5,0);
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
        setSize(650,175);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10,  10, 10));
        setVisible(true);

    }

    /**
     * Validates user input
     *
     * @param username user input username
     * @param password user input password
     * @return boolean validation successful
     */
    private boolean validateUser(String username, char[] password) {

        String cleanUsername = username.trim();
        String cleanPassword = String.valueOf(password).trim();

        if (cleanUsername != null && !cleanUsername.equals("")) {
            if (cleanPassword != null && !cleanPassword.equals("")) {
                return cleanUsername.equals("admin") && cleanPassword.equals("123");
            }
        }
        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String username = usernameTextfield.getText();
        char[] password = passwordPasswordfield.getPassword();

        if (validateUser(username, password)) {
            errorMessage.setText("Welkom " + username);
        } else {
            errorMessage.setText("Gebruikersnaam en wachtwoord combinatie is onjuist!");
            errorMessage.setForeground(Color.red);
        }
    }
}
