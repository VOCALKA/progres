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
    private JLabel bmiCurrentWeightLabel;

    public Profile() {
        this.frame = new JFrame("Fitness Profil");
    }

    /**
     * Fetches, validates, and chronologically sorts historical weight metrics recorded on disk.
     * Uses an internal {@link java.util.TreeMap} layer to guarantee ascending temporal organization
     * before mapping records into safe chart-compatible collections.
     *
     * @return A sorted list of structured {@link WeightChart.WeightRecord} objects.
     */
    private List<WeightChart.WeightRecord> loadWeightsFromFile() {
        List<WeightChart.WeightRecord> weights = new ArrayList<>();

        java.util.Map<java.time.LocalDate, Double> sortedRecords = new java.util.TreeMap<>();

        try {
            java.io.File fWeight = new java.io.File("weights.txt");
            if (fWeight.exists()) {
                List<String> lines = java.nio.file.Files.readAllLines(fWeight.toPath());
                for (String line : lines) {
                    String[] parts = line.split(";");
                    if (parts.length == 2) {
                        try {
                            java.time.LocalDate date = java.time.LocalDate.parse(parts[0].trim());
                            double weight = Double.parseDouble(parts[1].trim());
                            sortedRecords.put(date, weight);
                        } catch (Exception e) {

                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        for (java.util.Map.Entry<java.time.LocalDate, Double> entry : sortedRecords.entrySet()) {
            weights.add(new WeightChart.WeightRecord(entry.getKey(), entry.getValue()));
        }

        return weights;
    }

    /**
     * Generates, layouts, and populates the core graphical components for the user profile.
     * Orchestrates form layouts via {@link GridBagLayout}, streams stored biographical configurations
     * out of local storage, and initializes real-time sliding scales synchronized with past weight trends.
     */
    public void showApp() {
        this.frame.setSize(500, 600);
        this.frame.setLayout(new BorderLayout());
        this.frame.setLocationRelativeTo(null);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Custom.background(frame);

        JTabbedPane tabbedPane = new JTabbedPane();


        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setOpaque(false);
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
        //
        genderPanel.setOpaque(false);
        male.setOpaque(false);
        male.setFocusPainted(false);
        //male.setBorderPainted(false);
        female.setOpaque(false);
        female.setFocusPainted(false);
        //female.setBorderPainted(false);
        //
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
        weightSlider.setOpaque(false);
        weightSlider.setFocusable(false);
        weightSlider.setUI(new javax.swing.plaf.basic.BasicSliderUI(weightSlider) {
            @Override
            public void paintThumb(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(0, 0, 54));
                g2d.fillRect(thumbRect.x, thumbRect.y, thumbRect.width, thumbRect.height);
            }
        });



        JLabel weightValueLabel = new JLabel("70 kg", JLabel.CENTER);
        weightSlider.addChangeListener(e -> weightValueLabel.setText(weightSlider.getValue() + " kg"));


        String loadedName = "";
        int loadedBirthYear = 2000;
        int loadedWeight = 70;

        try {
            java.io.File fProfil = new java.io.File("profil.txt");
            if (fProfil.exists()) {
                List<String> lines = java.nio.file.Files.readAllLines(fProfil.toPath());
                for (String line : lines) {
                    if (line.startsWith("Name: ")) loadedName = line.replace("Name: ", "");
                    if (line.startsWith("Year of birth: ")) loadedBirthYear = Integer.parseInt(line.replace(
                            "Year of birth: ", "").trim());
                    if (line.startsWith("Height: ")) heightField.setText(line.replace("Height: ",
                            "").trim());
                    if (line.startsWith("Gender: ")) {
                        if (line.contains("Male")) male.setSelected(true);
                        else if (line.contains("Female")) female.setSelected(true);
                    }
                }
            }

            //List<Double> allWeights = nactiVahyZeSouboru();
            List<WeightChart.WeightRecord> allWeights = loadWeightsFromFile();
            if (!allWeights.isEmpty()) {
                //loadedWeight = allWeights.get(allWeights.size() - 1).intValue();
                loadedWeight = (int) allWeights.get(allWeights.size() - 1).weight();
            }
        } catch (Exception ex) {
            System.out.println("Failed to load data: " + ex.getMessage());
        }

        nameField.setText(loadedName);
        yearSpinner.setValue(loadedBirthYear);
        weightSlider.setValue(loadedWeight);
        weightValueLabel.setText(loadedWeight + " kg");

        JPanel weightPanel = new JPanel(new BorderLayout());
        weightPanel.setOpaque(false);
        weightPanel.add(weightSlider, BorderLayout.CENTER);
        weightPanel.add(weightValueLabel, BorderLayout.SOUTH);

        gbc.gridx = 1;
        infoPanel.add(weightPanel, gbc);


        WeightChart graphPanel = new WeightChart(loadWeightsFromFile());

        JScrollPane scrollGraph = new JScrollPane(graphPanel);
        scrollGraph.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollGraph.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

        //BMI

        JPanel bmiPanel = new JPanel(new GridBagLayout());
        bmiPanel.setOpaque(false);
        GridBagConstraints gbcBmi = new GridBagConstraints();
        gbcBmi.insets = new Insets(15, 15, 15, 15);
        gbcBmi.gridx = 0;

        JLabel bmiNumberLabel = new JLabel("--.-", SwingConstants.CENTER);
        //bmiNumberLabel.setFont(new Font("Arial", Font.BOLD, 60));
        Custom.bmiNumberStyle(bmiNumberLabel);
        JLabel bmiStatusLabel = new JLabel("Loading...", SwingConstants.CENTER);
        //bmiStatusLabel.setFont(new Font("Arial", Font.BOLD, 22));
        Custom.bmiTextStyle(bmiStatusLabel);
        //JLabel bmiAktualniVahaLabel = new JLabel("Použitá váha: -- kg", SwingConstants.CENTER);
        bmiCurrentWeightLabel = new JLabel("Weight used: -- kg", SwingConstants.CENTER);
        Custom.bmiWeightUsedStyle(bmiCurrentWeightLabel);
        //bmiAktualniVahaLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        //bmiAktualniVahaLabel.setForeground(Color.GRAY);

        gbcBmi.gridy = 0; bmiPanel.add(new JLabel("YOUR CURRENT BMI:", SwingConstants.CENTER), gbcBmi);
        gbcBmi.gridy = 1; bmiPanel.add(bmiNumberLabel, gbcBmi);
        gbcBmi.gridy = 2; bmiPanel.add(bmiStatusLabel, gbcBmi);
        gbcBmi.gridy = 3; bmiPanel.add(bmiCurrentWeightLabel, gbcBmi);

        //END BMI


        //BMR
        JPanel caloriesPanel = new JPanel(new GridBagLayout());
        caloriesPanel.setOpaque(false);
        GridBagConstraints gbcKal = new GridBagConstraints();
        gbcKal.insets = new Insets(10, 10, 10, 10);
        gbcKal.fill = GridBagConstraints.HORIZONTAL;

        JLabel calorieNumberLabel = new JLabel("---- kcal", SwingConstants.CENTER);
        Custom.calorieNumberStyle(calorieNumberLabel);
        //calorieNumberLabel.setFont(new Font("Arial", Font.BOLD, 45));
        //calorieNumberLabel.setForeground(new Color(154, 17, 34));

        JLabel proteinLabel = new JLabel("Proteins: -- g");
        Custom.macroStyle(proteinLabel, "protein");
        JLabel carbsLabel = new JLabel("Carbohydrates: -- g");
        Custom.macroStyle(carbsLabel, "carbs");
        JLabel fatsLabel = new JLabel("Fats: -- g");
        Custom.macroStyle(fatsLabel, "fats");

        /*Font makroFont = new Font("Arial", Font.BOLD, 16);
        proteinLabel.setFont(makroFont); proteinLabel.setForeground(new Color(70, 130, 180));
        carbsLabel.setFont(makroFont); carbsLabel.setForeground(new Color(218, 165, 32));
        fatsLabel.setFont(makroFont); fatsLabel.setForeground(new Color(46, 139, 87));*/

        gbcKal.gridx = 0; gbcKal.gridy = 0; gbcKal.gridwidth = 2;
        caloriesPanel.add(new JLabel("RECOMMENDED DAILY INTAKE:", SwingConstants.CENTER), gbcKal);
        gbcKal.gridy = 1;
        caloriesPanel.add(calorieNumberLabel, gbcKal);

        gbcKal.gridy = 2;
        caloriesPanel.add(new JLabel("--------------------------------------------------",
                SwingConstants.CENTER), gbcKal);

        gbcKal.gridwidth = 1;
        gbcKal.gridy = 3; gbcKal.gridx = 0; caloriesPanel.add(new JLabel("Proteins (30%):"), gbcKal);
        gbcKal.gridx = 1; caloriesPanel.add(proteinLabel, gbcKal);

        gbcKal.gridy = 4; gbcKal.gridx = 0; caloriesPanel.add(new JLabel("Carbohydrates (45%):"), gbcKal);
        gbcKal.gridx = 1; caloriesPanel.add(carbsLabel, gbcKal);

        gbcKal.gridy = 5; gbcKal.gridx = 0; caloriesPanel.add(new JLabel("Fats (25%):"), gbcKal);
        gbcKal.gridx = 1; caloriesPanel.add(fatsLabel, gbcKal);
        //END BMR
        //

        tabbedPane.addTab("Profile", infoPanel);
        tabbedPane.addTab("Statistics", scrollGraph);
        //tabbedPane.addTab("Statistics", graphPanel);
        tabbedPane.addTab("BMI", bmiPanel);
        tabbedPane.addTab("Calorie", caloriesPanel);
        tabbedPane.addTab("Home", new JPanel());

        /*tabbedPane.setBackgroundAt(4, new Color(154, 17, 34));
        tabbedPane.setForegroundAt(4, Color.WHITE);*/
        Custom.styleTab(tabbedPane, 4);



        this.frame.add(tabbedPane, BorderLayout.CENTER);

        JButton saveBtn = new JButton("SAVE PROFILE");
        Custom.startButton(saveBtn);
        this.frame.add(saveBtn, BorderLayout.SOUTH);

        saveBtn.addActionListener(e -> {
            String name = nameField.getText();
            int birthYear = (int) yearSpinner.getValue();
            String height = heightField.getText();
            String gender = male.isSelected() ? "Male" : (female.isSelected() ? "Female" : "Not entered");
            int weight = weightSlider.getValue();
            String todaysDate = java.time.LocalDate.now().toString();
            UIManager.put("Button.focus", new javax.swing.plaf.ColorUIResource(new Color(0, 0, 0, 0)));

            try {
                try (java.io.FileWriter fw = new java.io.FileWriter("profil.txt")) {
                    fw.write("Name: " + name + "\n" +
                            "Year of birth: " + birthYear + "\n" +
                            "Height: " + height + "\n" +
                            "Gender: " + gender);
                }

                java.io.File weightFile = new java.io.File("weights.txt");
                Map<String, String> weightRecords = new LinkedHashMap<>();

                if (weightFile.exists()) {
                    java.nio.file.Files.lines(weightFile.toPath()).forEach(line -> {
                        String[] parts = line.split(";");
                        if (parts.length == 2) weightRecords.put(parts[0], parts[1]);
                    });
                }

                weightRecords.put(todaysDate, String.valueOf(weight));

                try (java.io.FileWriter fw = new java.io.FileWriter(weightFile)) {
                    for (var entry : weightRecords.entrySet()) {
                        fw.write(entry.getKey() + ";" + entry.getValue() + "\n");
                    }
                }

                graphPanel.setWeights(loadWeightsFromFile());

                JOptionPane.showMessageDialog(frame, "Profile saved!");
            } catch (java.io.IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error saving profile: " + ex.getMessage());
            }
        });

        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() == 4) {
                this.frame.dispose();
                new App().showApp();
            }
        });

        //

        // Listener pro BMI (Index 2)
        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() == 2) {

                bmiCurrentWeightLabel.setText("Weight used: " + weightSlider.getValue() + " kg");

                BmiCalculator.BmiResult result = BmiCalculator.countBmi();
                if (result.bmi > 0) {
                    bmiNumberLabel.setText(String.valueOf(result.bmi));
                    bmiStatusLabel.setText(result.verbalEvaluation.toUpperCase());

                    Color color = getBmiColor(result.verbalEvaluation);
                    bmiNumberLabel.setForeground(color);
                    bmiStatusLabel.setForeground(color);
                } else {
                    bmiNumberLabel.setText("??.?");
                    bmiStatusLabel.setText("Missing profile data!");
                    bmiStatusLabel.setForeground(Color.GRAY);
                }
            }
        });

        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() == 3) {
                BmrCalculator.BmrResult bmrResult = BmrCalculator.countBmr();
                if (bmrResult.recommendedIntake > 0) {
                    int totalKcal = bmrResult.recommendedIntake;
                    calorieNumberLabel.setText(totalKcal + " kcal");

                    int gBilkoviny = (int) Math.round((totalKcal * 0.30) / 4.0);
                    int gSacharidy = (int) Math.round((totalKcal * 0.45) / 4.0);
                    int gTuky = (int) Math.round((totalKcal * 0.25) / 9.0);

                    proteinLabel.setText(gBilkoviny + " g");
                    carbsLabel.setText(gSacharidy + " g");
                    fatsLabel.setText(gTuky + " g");
                } else {
                    calorieNumberLabel.setText("---- kcal");
                    proteinLabel.setText("-- g");
                    carbsLabel.setText("-- g");
                    fatsLabel.setText("-- g");
                }
            }
        });


        //

        this.frame.setVisible(true);
    }
    /**
     * Returns the appropriate visual color layer based on the evaluation status string.
     * Maps standard health risk thresholds (Underweight, Normal, Overweight, Obesity)
     * to a distinctive visual palette for clear dashboard alert feedback.
     *
     * @param stav The string literal representing the health status group categorization.
     * @return A styled {@link Color} instance matching the category, or Color.BLACK if undefined.
     */
    private Color getBmiColor(String stav) {
        if (stav == null) return Color.BLACK;
        switch (stav) {
            case "Normal": return new Color(34, 139, 34);
            case "Underweight": return new Color(255, 140, 0);
            case "Overweight": return new Color(255, 69, 0);
            case "Obesity": return Color.RED;
            default: return Color.BLACK;
        }
    }


}



