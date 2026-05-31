package plan;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import app.App;
import custom.Custom;

public class Plan {
    private JFrame frame;
    private ArrayList<String> cvikySeznam = new ArrayList<>();
    private JTextField nazevPlanuField, cvikField, casField, restField;

    public Plan() {
        this.frame = new JFrame("Workout Plan Creation");
    }

    public void showPlan() {
        frame.setSize(500, 600);
        frame.setLayout(new BorderLayout(10, 10));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Custom.background(frame);


        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.setOpaque(false);
        topPanel.add(new JLabel("Plan name:", SwingConstants.CENTER));
        nazevPlanuField = new JTextField();
        topPanel.add(nazevPlanuField);


        JPanel inputPanel = new JPanel(new GridLayout(6, 1, 5, 5));
        inputPanel.setOpaque(false);

        cvikField = new JTextField();
        casField = new JTextField();
        restField = new JTextField();

        inputPanel.add(new JLabel("Exercise name:"));
        inputPanel.add(cvikField);
        inputPanel.add(new JLabel("Exercise duration (seconds):"));
        inputPanel.add(casField);
        inputPanel.add(new JLabel("Rest time (seconds):"));
        inputPanel.add(restField);


        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> jList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(jList);



        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        buttonPanel.setOpaque(false);

        JButton btnAdd = new JButton("Add");
        JButton btnRun = new JButton("Load");
        JButton btnSave = new JButton("Save");
        JButton btnHome = new JButton("Home");

        /*btnAdd.addActionListener(e -> {
            String radek = cvikField.getText() + ";" + casField.getText() + ";" + restField.getText();
            cvikySeznam.add(radek);
            listModel.addElement(cvikField.getText() + " (" + casField.getText() + "s + " + restField.getText() + "s)");


            cvikField.setText("");
            casField.setText("");
            restField.setText("");
        });*/
        btnAdd.addActionListener(e -> {
            String nazevCviku = cvikField.getText().trim();
            String casText = casField.getText().trim();
            String restText = restField.getText().trim();

             if (nazevCviku.isEmpty() || casText.isEmpty() || restText.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill in all exercise fields.", "Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!casText.matches("\\d+") || !restText.matches("\\d+")) {
                JOptionPane.showMessageDialog(frame, "Please enter numbers only in the time fields!",
                        "Input error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String radek = nazevCviku + ";" + casText + ";" + restText;
            cvikySeznam.add(radek);
            listModel.addElement(nazevCviku + " (" + casText + "s + " + restText + "s)");

            cvikField.setText("");
            casField.setText("");
            restField.setText("");
        });

        jList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = jList.locationToIndex(e.getPoint());
                    if (index >= 0) {
                        listModel.remove(index);
                        cvikySeznam.remove(index);
                    }
                }
            }
        });
        btnRun.addActionListener(e -> {
            RunPlan r = new RunPlan();
            r.start();
        });


        btnSave.addActionListener(e -> {
            ulozDoSouboru();
            //frame.dispose();
            //new App().showApp();
        });
        btnHome.addActionListener(e -> {
            this.frame.dispose();
            new App().showApp();
        });

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnRun);
        buttonPanel.add(btnSave);
        buttonPanel.add(btnHome);

        Custom.startButton(btnAdd);
        Custom.startButton(btnRun);
        Custom.startButton(btnSave);
        Custom.startButton(btnHome);



        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(inputPanel, BorderLayout.CENTER);
        frame.add(scrollPane, BorderLayout.WEST);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void ulozDoSouboru() {
        if (cvikySeznam.isEmpty()) {
            JOptionPane.showMessageDialog(frame,
                    "Cannot save an empty plan! Please add some exercises first!", "Saving error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nazev = nazevPlanuField.getText().trim();
        if (nazev.isEmpty()) nazev = "unnamed_plan";

        File adresar = new File("resources");
        if (!adresar.exists()) {
            adresar.mkdirs();
        }

        File soubor = new File(adresar, nazev + ".txt");

        if (soubor.exists()) {
            int pocitadlo = 1;
            while (soubor.exists()) {
                soubor = new File(adresar, nazev + pocitadlo + ".txt");
                pocitadlo++;
            }
            nazev = soubor.getName().replace(".txt", "");
        }

        try (FileWriter writer = new FileWriter(soubor)) {
            for (String s : cvikySeznam) {
                writer.write(s + System.lineSeparator());
            }
            JOptionPane.showMessageDialog(frame, "Plan '" + nazev + "' saved to the resources folder!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error saving: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }



}
