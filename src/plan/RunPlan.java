package plan;

import javax.swing.*;
import java.awt.*;
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

    /*public void start(String souborPath) {
        nactiData(souborPath);
        if (dataPlanu.isEmpty()) return;

        frame = new JFrame("TRÉNINK BĚŽÍ");
        frame.setSize(400, 400);
        frame.setLayout(new GridLayout(3, 1));
        frame.setLocationRelativeTo(null);
        Custom.background(frame);

        labelStatus = new JLabel("PŘIPRAV SE", SwingConstants.CENTER);
        labelCvik = new JLabel(dataPlanu.get(0)[0], SwingConstants.CENTER);
        labelCas = new JLabel("0", SwingConstants.CENTER);
        labelCas.setFont(new Font("Arial", Font.BOLD, 50));

        frame.add(labelStatus);
        frame.add(labelCvik);
        frame.add(labelCas);

        pripravDalsi(0);
        startTimer();

        frame.setVisible(true);
    }*/

    //
     //
    /*public void start() {

        //File vychoziSlozka = new File("progres");
        File vychoziSlozka = new File(System.getProperty("user-dir"), "progres");*/



        /*if (!vychoziSlozka.exists()) {
            vychoziSlozka.mkdir();
        }*/

        /*JFileChooser fileChooser = new JFileChooser(vychoziSlozka);
        fileChooser.setDialogTitle("Vyberte plán tréninku");

        fileChooser.setCurrentDirectory(vychoziSlozka);

        int vysledek = fileChooser.showOpenDialog(null);

        if (vysledek == JFileChooser.APPROVE_OPTION) {
            File vybranySoubor = fileChooser.getSelectedFile();
            nactiData(vybranySoubor.getAbsolutePath());
        } else {

            return;
        }

        if (dataPlanu.isEmpty()) return;


        frame = new JFrame("TRÉNINK BĚŽÍ");
        frame.setSize(400, 400);
        frame.setLayout(new GridLayout(3, 1));
        frame.setLocationRelativeTo(null);
        Custom.background(frame);

        labelStatus = new JLabel("PŘIPRAV SE", SwingConstants.CENTER);
        labelCvik = new JLabel(dataPlanu.get(0)[0], SwingConstants.CENTER);
        labelCas = new JLabel("0", SwingConstants.CENTER);
        labelCas.setFont(new Font("Arial", Font.BOLD, 50));

        frame.add(labelStatus);
        frame.add(labelCvik);
        frame.add(labelCas);

        pripravDalsi(0);
        startTimer();

        frame.setVisible(true);
    }*/
        public void start() {

            String projektCesta = System.getProperty("user.dir");
            File vychoziSlozka = new File(projektCesta, "resources");


            if (!vychoziSlozka.exists()) {
                vychoziSlozka.mkdirs();
            }

            JFileChooser fileChooser = new JFileChooser(vychoziSlozka);
            fileChooser.setDialogTitle("Vyberte plán tréninku");

            fileChooser.setCurrentDirectory(vychoziSlozka);

            int vysledek = fileChooser.showOpenDialog(null);

            if (vysledek == JFileChooser.APPROVE_OPTION) {
                File vybranySoubor = fileChooser.getSelectedFile();
                nactiData(vybranySoubor.getAbsolutePath());
            } else {
                return;
            }

            if (dataPlanu.isEmpty()) return;

            frame = new JFrame("TRÉNINK BĚŽÍ");
            frame.setSize(400, 400);
            frame.setLayout(new GridLayout(3, 1));
            frame.setLocationRelativeTo(null);
            Custom.background(frame);

            labelStatus = new JLabel("PŘIPRAV SE", SwingConstants.CENTER);
            //labelCvik = new JLabel(dataPlanu.get(0)[0], SwingConstants.CENTER);
            labelCvik = new JLabel(dataPlanu.get(0)[0], SwingConstants.CENTER);
            labelCas = new JLabel("0", SwingConstants.CENTER);
            labelCas.setFont(new Font("Arial", Font.BOLD, 50));

            frame.add(labelStatus);
            frame.add(labelCvik);
            frame.add(labelCas);

            pripravDalsi(0);
            startTimer();

            frame.setVisible(true);
        }



            private void nactiData(String cesta) {
        try (BufferedReader br = new BufferedReader(new FileReader(cesta))) {
            String radek;
            while ((radek = br.readLine()) != null) {
                dataPlanu.add(radek.split(";"));
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Chyba při načítání souboru!");
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

    /*private void prepniStav() {
        if (!jeOdpocinek) {

            jeOdpocinek = true;
            zbyvajiciCas = Integer.parseInt(dataPlanu.get(aktualniIndex)[2]);

            labelStatus.setText("ODPOČINEK");
            labelStatus.setForeground(Color.BLUE);
        } else {

            jeOdpocinek = false;
            aktualniIndex++;
            if (aktualniIndex < dataPlanu.size()) {
                pripravDalsi(aktualniIndex);
            } else {
                timer.stop();
                labelStatus.setText("HOTOVO!");
                JOptionPane.showMessageDialog(frame, "Trénink dokončen!");
                frame.dispose();
            }
        }
    }*/
    private void prepniStav() {
        if (!jeOdpocinek) {
            jeOdpocinek = true;

            String[] aktualniCvik = dataPlanu.get(aktualniIndex);

            if (aktualniCvik.length > 2 && !aktualniCvik[2].trim().isEmpty()) {
                zbyvajiciCas = Integer.parseInt(aktualniCvik[2].trim());
            } else {
                zbyvajiciCas = 0;
            }

            labelStatus.setText("ODPOČINEK");
            labelStatus.setForeground(Color.BLUE);
        } else {
            jeOdpocinek = false;
            aktualniIndex++;
            if (aktualniIndex < dataPlanu.size()) {
                pripravDalsi(aktualniIndex);
            } else {
                timer.stop();
                labelStatus.setText("HOTOVO!");
                JOptionPane.showMessageDialog(frame, "Trénink dokončen!");
                frame.dispose();
            }
        }
    }


    /*private void pripravDalsi(int index) {
        String[] radek = dataPlanu.get(index);
        labelCvik.setText(radek[0]);
        zbyvajiciCas = Integer.parseInt(radek[1]);
        labelStatus.setText("CVIČ!");
        labelStatus.setForeground(Color.RED);
    }*/
    private void pripravDalsi(int index) {
        String[] radek = dataPlanu.get(index);
        labelCvik.setText(radek[0]);

        if (radek.length > 1 && !radek[1].trim().isEmpty()) {
            zbyvajiciCas = Integer.parseInt(radek[1].trim());
        } else {
            zbyvajiciCas = 0;
        }

        labelStatus.setText("CVIČ!");
        labelStatus.setForeground(Color.RED);
    }

}

