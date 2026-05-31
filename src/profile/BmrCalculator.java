package profile;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;

public class BmrCalculator {

    public static class BmrVysledek {
        public int bmrBazalni;
        public int doporucenyPrijem;

        public BmrVysledek(int bmrBazalni, int doporucenyPrijem) {
            this.bmrBazalni = bmrBazalni;
            this.doporucenyPrijem = doporucenyPrijem;
        }
    }

    public static BmrVysledek spocitejBmr() {
        int rokNarozeni = 2000;
        double vyskaCm = 0;
        String pohlavi = "Not entered";
        double vahaKg = 0;


        try {
            File fProfil = new File("profil.txt");
            if (fProfil.exists()) {
                List<String> radky = Files.readAllLines(fProfil.toPath());
                for (String radek : radky) {
                    if (radek.startsWith("Year of birth: ")) {
                        rokNarozeni = Integer.parseInt(radek.replace("Year of birth: ", "").trim());
                    }
                    if (radek.startsWith("Height: ")) {
                        String v = radek.replace("Height: ", "").trim();
                        if (!v.isEmpty()) vyskaCm = Double.parseDouble(v);
                    }
                    if (radek.startsWith("Gender: ")) {
                        pohlavi = radek.replace("Gender: ", "").trim();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Unable to load profile data for BMR: " + e.getMessage());
        }

        try {
            File fVahy = new File("vahy.txt");
            if (fVahy.exists()) {
                List<String> radky = Files.readAllLines(fVahy.toPath());
                String dnesniDatum = LocalDate.now().toString();
                double posledniZaznamenanaVaha = 0;
                boolean nalezenaDnesniVaha = false;

                for (String radek : radky) {
                    String[] casti = radek.split(";");
                    if (casti.length == 2) {
                        String datumZeSouboru = casti[0].trim();
                        double vahaZeSouboru = Double.parseDouble(casti[1].trim());
                        posledniZaznamenanaVaha = vahaZeSouboru;

                        if (datumZeSouboru.equals(dnesniDatum)) {
                            vahaKg = vahaZeSouboru;
                            nalezenaDnesniVaha = true;
                            break;
                        }
                    }
                }
                if (!nalezenaDnesniVaha) {
                    vahaKg = posledniZaznamenanaVaha;
                }
            }
        } catch (Exception e) {
            System.out.println("Unable to load weight for BMR: " + e.getMessage());
        }

        if (vyskaCm <= 0 || vahaKg <= 0) {
            return new BmrVysledek(0, 0);
        }

        int vek = LocalDate.now().getYear() - rokNarozeni;


        double bmr;
        if (pohlavi.equalsIgnoreCase("Female")) {

            bmr = (10 * vahaKg) + (6.25 * vyskaCm) - (5 * vek) - 161;
        } else {

            bmr = (10 * vahaKg) + (6.25 * vyskaCm) - (5 * vek) + 5;
        }

        double prijem = bmr * 1.2;

        return new BmrVysledek((int) Math.round(bmr), (int) Math.round(prijem));
    }
}

