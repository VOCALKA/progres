package plan;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.*;
import custom.Custom;
import javax.swing.Timer;

public class RunPlan {
    private JFrame frame;
    private JLabel labelCvik, labelCas, labelStatus;
    private ArrayList<String[]> dataPlanu = new ArrayList<>();
    private int aktualniIndex = 0;
    private int zbyvajiciCas;
    private boolean jeOdpocinek = false;
    private Timer timer;

        public void start() {

            String projektCesta = System.getProperty("user.dir");
            File vychoziSlozka = new File(projektCesta, "resources");


            if (!vychoziSlozka.exists()) {
                vychoziSlozka.mkdirs();
            }

            JFileChooser fileChooser = new JFileChooser(vychoziSlozka);
            fileChooser.setDialogTitle("Select a workout plan");

            fileChooser.setCurrentDirectory(vychoziSlozka);

            int vysledek = fileChooser.showOpenDialog(null);

            if (vysledek == JFileChooser.APPROVE_OPTION) {
                File vybranySoubor = fileChooser.getSelectedFile();
                nactiData(vybranySoubor.getAbsolutePath());
            } else {
                return;
            }

            if (dataPlanu.isEmpty()) return;

            frame = new JFrame("Workout running");
            frame.setSize(400, 400);
            frame.setLayout(new GridLayout(3, 1));
            frame.setLocationRelativeTo(null);
            Custom.background(frame);

            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    if (timer != null) {
                        timer.stop();
                    }
                }
            });

            labelStatus = new JLabel("Get ready", SwingConstants.CENTER);
            labelCvik = new JLabel(dataPlanu.get(0)[0], SwingConstants.CENTER);
            labelCas = new JLabel("0", SwingConstants.CENTER);
            //labelCas.setFont(new Font("Arial", Font.BOLD, 50));

            Custom.cvikStyle(labelCvik);
            Custom.casStyle(labelCas);

            frame.add(labelStatus);
            frame.add(labelCvik);
            frame.add(labelCas);

            pripravDalsi(0);
            startTimer();


            frame.setAlwaysOnTop(true);
            frame.setVisible(true);
        }



            private void nactiData(String cesta) {
        try (BufferedReader br = new BufferedReader(new FileReader(cesta))) {
            String radek;
            while ((radek = br.readLine()) != null) {
                dataPlanu.add(radek.split(";"));
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading file!");
        }
    }

    private void startTimer() {
        timer = new Timer(1000, e -> {
            zbyvajiciCas--;
            labelCas.setText(String.valueOf(zbyvajiciCas));

            if (zbyvajiciCas <= 0) {
                prepniStav();
            }
        });
        timer.start();
    }

    private void prepniStav() {
        if (!jeOdpocinek) {
            jeOdpocinek = true;

            String[] aktualniCvik = dataPlanu.get(aktualniIndex);

            if (aktualniCvik.length > 2 && !aktualniCvik[2].trim().isEmpty()) {
                zbyvajiciCas = Integer.parseInt(aktualniCvik[2].trim());
            } else {
                zbyvajiciCas = 0;
            }

            labelStatus.setText("Rest time");
            //labelStatus.setForeground(Color.BLUE);
            Custom.stavStyle(labelStatus, Color.BLUE);
        } else {
            jeOdpocinek = false;
            aktualniIndex++;
            if (aktualniIndex < dataPlanu.size()) {
                pripravDalsi(aktualniIndex);
            } else {
                timer.stop();
                labelStatus.setText("Completed!");
                JOptionPane.showMessageDialog(frame, "Workout completed!");
                frame.dispose();
            }
        }
    }

    private void pripravDalsi(int index) {
        String[] radek = dataPlanu.get(index);
        labelCvik.setText(radek[0]);

        if (radek.length > 1 && !radek[1].trim().isEmpty()) {
            zbyvajiciCas = Integer.parseInt(radek[1].trim());
        } else {
            zbyvajiciCas = 0;
        }

        labelStatus.setText("Work!");
        //labelStatus.setForeground(Color.RED);
        Custom.stavStyle(labelStatus, Color.RED);
    }

}

