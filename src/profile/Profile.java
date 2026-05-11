package profile;

import app.App;
import custom.Custom;
import custom.RoundedButton;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

public class Profile {
    private JFrame frame;

    public Profile() {
        this.frame = new JFrame("Fitness Profil");
    }


    private List<Double> nactiVahyZeSouboru() {
        List<Double> vahy = new ArrayList<>();
        try {
            java.io.File fVahy = new java.io.File("vahy.txt");
            if (fVahy.exists()) {
                List<String> radky = java.nio.file.Files.readAllLines(fVahy.toPath());
                for (String radek : radky) {
                    String[] casti = radek.split(";");
                    if (casti.length == 2) {
                        vahy.add(Double.parseDouble(casti[1]));
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return vahy;
    }

    public void showApp() {
        this.frame.setSize(500, 600);
        this.frame.setLayout(new BorderLayout());
        this.frame.setLocationRelativeTo(null);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Custom.background(frame);

        JTabbedPane tabbedPane = new JTabbedPane();


        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        infoPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        JTextField nameField = new JTextField(15);
        infoPanel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        infoPanel.add(new JLabel("Year of Birth:"), gbc);
        gbc.gridx = 1;
        SpinnerModel yearModel = new SpinnerNumberModel(2000, 1900, 2024, 1);
        JSpinner yearSpinner = new JSpinner(yearModel);
        infoPanel.add(yearSpinner, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        infoPanel.add(new JLabel("Height (cm):"), gbc);
        gbc.gridx = 1;
        JTextField heightField = new JTextField(5);
        infoPanel.add(heightField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        infoPanel.add(new JLabel("Gender:"), gbc);
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JRadioButton male = new JRadioButton("Male");
        JRadioButton female = new JRadioButton("Female");
        ButtonGroup group = new ButtonGroup();
        group.add(male); group.add(female);
        genderPanel.add(male); genderPanel.add(female);
        gbc.gridx = 1;
        infoPanel.add(genderPanel, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        infoPanel.add(new JLabel("Weight (kg):"), gbc);

        JSlider weightSlider = new JSlider(JSlider.HORIZONTAL, 30, 150, 70);
        weightSlider.setMajorTickSpacing(20);
        weightSlider.setMinorTickSpacing(5);
        weightSlider.setPaintTicks(true);
        weightSlider.setPaintLabels(true);

        JLabel weightValueLabel = new JLabel("70 kg", JLabel.CENTER);
        weightSlider.addChangeListener(e -> weightValueLabel.setText(weightSlider.getValue() + " kg"));


        String nacteneJmeno = "";
        int nactenyRok = 2000;
        int nactenaVaha = 70;

        try {
            java.io.File fProfil = new java.io.File("profil.txt");
            if (fProfil.exists()) {
                List<String> radky = java.nio.file.Files.readAllLines(fProfil.toPath());
                for (String radek : radky) {
                    if (radek.startsWith("Jmeno: ")) nacteneJmeno = radek.replace("Jmeno: ", "");
                    if (radek.startsWith("Rok narozeni: ")) nactenyRok = Integer.parseInt(radek.replace("Rok narozeni: ", "").trim());
                    if (radek.startsWith("Vyska: ")) heightField.setText(radek.replace("Vyska: ", "").trim());
                    if (radek.startsWith("Gender: ")) {
                        if (radek.contains("Male")) male.setSelected(true);
                        else if (radek.contains("Female")) female.setSelected(true);
                    }
                }
            }

            List<Double> vsechnyVahy = nactiVahyZeSouboru();
            if (!vsechnyVahy.isEmpty()) {
                nactenaVaha = vsechnyVahy.get(vsechnyVahy.size() - 1).intValue();
            }
        } catch (Exception ex) {
            System.out.println("Nepodařilo se načíst data: " + ex.getMessage());
        }

        nameField.setText(nacteneJmeno);
        yearSpinner.setValue(nactenyRok);
        weightSlider.setValue(nactenaVaha);
        weightValueLabel.setText(nactenaVaha + " kg");

        JPanel weightPanel = new JPanel(new BorderLayout());
        weightPanel.add(weightSlider, BorderLayout.CENTER);
        weightPanel.add(weightValueLabel, BorderLayout.SOUTH);

        gbc.gridx = 1;
        infoPanel.add(weightPanel, gbc);


        WeightChart graphPanel = new WeightChart(nactiVahyZeSouboru());

        //
        //
        JScrollPane scrollGraph = new JScrollPane(graphPanel);
        scrollGraph.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollGraph.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        //
         //

        tabbedPane.addTab("Profile", infoPanel);
        tabbedPane.addTab("Statistics", scrollGraph);
        //tabbedPane.addTab("Statistics", graphPanel);
        tabbedPane.addTab("Home", new JPanel());

        tabbedPane.setBackgroundAt(2, new Color(154, 17, 34));
        tabbedPane.setForegroundAt(2, Color.WHITE);

        this.frame.add(tabbedPane, BorderLayout.CENTER);

        RoundedButton saveBtn = new RoundedButton("SAVE PROFILE");
        Custom.startButton(saveBtn);
        this.frame.add(saveBtn, BorderLayout.SOUTH);

        saveBtn.addActionListener(e -> {
            String jmeno = nameField.getText();
            int rokNarozeni = (int) yearSpinner.getValue();
            String vyska = heightField.getText();
            String pohlavi = male.isSelected() ? "Male" : (female.isSelected() ? "Female" : "Nezadáno");
            int vaha = weightSlider.getValue();
            String dnesniDatum = java.time.LocalDate.now().toString();

            try {
                try (java.io.FileWriter fw = new java.io.FileWriter("profil.txt")) {
                    fw.write("Jmeno: " + jmeno + "\n" +
                            "Rok narozeni: " + rokNarozeni + "\n" +
                            "Vyska: " + vyska + "\n" +
                            "Gender: " + pohlavi);
                }

                java.io.File souborVahy = new java.io.File("vahy.txt");
                Map<String, String> zaznamyVah = new LinkedHashMap<>();

                if (souborVahy.exists()) {
                    java.nio.file.Files.lines(souborVahy.toPath()).forEach(line -> {
                        String[] parts = line.split(";");
                        if (parts.length == 2) zaznamyVah.put(parts[0], parts[1]);
                    });
                }

                zaznamyVah.put(dnesniDatum, String.valueOf(vaha));

                try (java.io.FileWriter fw = new java.io.FileWriter(souborVahy)) {
                    for (var entry : zaznamyVah.entrySet()) {
                        fw.write(entry.getKey() + ";" + entry.getValue() + "\n");
                    }
                }

                graphPanel.setWeights(nactiVahyZeSouboru());

                JOptionPane.showMessageDialog(frame, "Profil i váha byly uloženy!");
            } catch (java.io.IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Chyba při ukládání: " + ex.getMessage());
            }
        });

        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() == 2) {
                this.frame.dispose();
                new App().showApp();
            }
        });

        this.frame.setVisible(true);
    }
}



