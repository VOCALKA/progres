/*package profile;

import custom.Custom;
import custom.RoundedButton;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;



public class Profile {
    private JFrame frame;


    public Profile() {
        this.frame = new JFrame("PROFILE");
    }

    public void showApp(){
        this.frame.setSize(500, 500);
        this.frame.setLayout(new BorderLayout());
        this.frame.setLocationRelativeTo(null);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.frame.setVisible(true);
    }
}*/
package profile;

import custom.Custom;
import custom.RoundedButton;

import javax.swing.*;
import java.awt.*;

public class Profile {
    private JFrame frame;

    public Profile() {
        this.frame = new JFrame("Fitness Profil");
    }

    public void showApp() {
        this.frame.setSize(500, 600);
        this.frame.setLayout(new BorderLayout());
        this.frame.setLocationRelativeTo(null);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        infoPanel.add(new JLabel("Jméno:"), gbc);
        gbc.gridx = 1;
        JTextField nameField = new JTextField(15);
        infoPanel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        infoPanel.add(new JLabel("Rok narození:"), gbc);
        gbc.gridx = 1;
        SpinnerModel yearModel = new SpinnerNumberModel(2000, 1900, 2024, 1);
        JSpinner yearSpinner = new JSpinner(yearModel);
        infoPanel.add(yearSpinner, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        infoPanel.add(new JLabel("Výška (cm):"), gbc);
        gbc.gridx = 1;
        JTextField heightField = new JTextField(5);
        infoPanel.add(heightField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        infoPanel.add(new JLabel("Pohlaví:"), gbc);
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JRadioButton male = new JRadioButton("Muž");
        JRadioButton female = new JRadioButton("Žena");
        ButtonGroup group = new ButtonGroup();
        group.add(male); group.add(female);
        genderPanel.add(male); genderPanel.add(female);
        gbc.gridx = 1;
        infoPanel.add(genderPanel, gbc);


        gbc.gridx = 0; gbc.gridy = 4;
        infoPanel.add(new JLabel("Váha (kg):"), gbc);

        JSlider weightSlider = new JSlider(JSlider.HORIZONTAL, 30, 150, 70);
        weightSlider.setMajorTickSpacing(20);
        weightSlider.setMinorTickSpacing(5);
        weightSlider.setPaintTicks(true);
        weightSlider.setPaintLabels(true);

        JLabel weightValueLabel = new JLabel("70 kg", JLabel.CENTER);
        weightSlider.addChangeListener(e -> weightValueLabel.setText(weightSlider.getValue() + " kg"));

        JPanel weightPanel = new JPanel(new BorderLayout());
        weightPanel.add(weightSlider, BorderLayout.CENTER);
        weightPanel.add(weightValueLabel, BorderLayout.SOUTH);

        gbc.gridx = 1;
        infoPanel.add(weightPanel, gbc);


        tabbedPane.addTab("Osobní údaje", infoPanel);
        tabbedPane.addTab("Statistiky", new JPanel());

        this.frame.add(tabbedPane, BorderLayout.CENTER);

        RoundedButton saveBtn = new RoundedButton("ULOŽIT PROFIL");
        Custom.startButton(saveBtn);
        this.frame.add(saveBtn, BorderLayout.SOUTH);

        saveBtn.addActionListener(e -> {
            String data = "Jméno: " + nameField.getText() +
                    ", Váha: " + weightSlider.getValue() + "kg";

            try (java.io.FileWriter writer = new java.io.FileWriter("profil.txt")) {
                writer.write(data);
                JOptionPane.showMessageDialog(frame, "Profil byl uložen do souboru!");
            } catch (java.io.IOException ex) {
                ex.printStackTrace();
            }
        });


        this.frame.setVisible(true);
    }

}


