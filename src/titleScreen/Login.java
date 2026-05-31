package titleScreen;

import app.App;
import custom.Custom;


import javax.swing.*;
import java.awt.*;

public class Login extends JFrame {
    private JLabel labelUsername = new JLabel("Enter username: ");
    private JLabel labelPassword = new JLabel("Enter password: ");
    private JTextField textUsername = new JTextField(20);
    private JPasswordField fieldPassword = new JPasswordField(20);
    private JButton buttonLogin = new JButton("Login");

    /**
     * Constructs a new Login frame.
     * Assembles the input form components using a {@link GridBagLayout}, defines window spacing,
     * and sets up event listeners for both button submission and the Enter-key trigger on the password field.
     */
    public Login() {
        super("LOGIN");


        Custom.background(this);

        JPanel newPanel = new JPanel(new GridBagLayout());

        newPanel.setOpaque(false);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);


        constraints.gridx = 0;
        constraints.gridy = 0;
        newPanel.add(labelUsername, constraints);

        constraints.gridx = 1;
        newPanel.add(textUsername, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        newPanel.add(labelPassword, constraints);

        constraints.gridx = 1;
        newPanel.add(fieldPassword, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        newPanel.add(buttonLogin, constraints);

        custom.Custom.startButton(buttonLogin);

        buttonLogin.setFocusPainted(false);

        buttonLogin.addActionListener(e -> {
            String username = textUsername.getText();
            String password = new String(fieldPassword.getPassword());


            if (username.equals("") && password.equals("")) {
                this.dispose();
                new App().showApp();
            } else {
                JOptionPane.showMessageDialog(this, "Wrong username or password!",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        fieldPassword.addActionListener(e -> {
            buttonLogin.doClick();
        });


        newPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Login Panel"));

        add(newPanel);

        pack();
        setLocationRelativeTo(null);
    }


}


