import app.App;

import javax.swing.*;
import java.awt.*;

public class Login extends JFrame {
    private JLabel labelUsername = new JLabel("Enter username: ");
    private JLabel labelPassword = new JLabel("Enter password: ");
    private JTextField textUsername = new JTextField(20);
    private JPasswordField fieldPassword = new JPasswordField(20);
    private JButton buttonLogin = new JButton("Login");

    public Login() {
        super("LOGIN");


        JPanel newPanel = new JPanel(new GridBagLayout());

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

        buttonLogin.addActionListener(e -> {
            String username = textUsername.getText();
            String password = new String(fieldPassword.getPassword());


            if (username.equals("admin") && password.equals("1234")) {
                this.dispose();
                new App().showApp();
            } else {
                JOptionPane.showMessageDialog(this, "Špatné jméno nebo heslo!",
                        "Chyba", JOptionPane.ERROR_MESSAGE);
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


