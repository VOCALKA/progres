package profile;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;

public class BmiCalculator {

    public static class BmiVysledek {
        public double bmi;
        public String slovniHodnoceni;

        public BmiVysledek(double bmi, String slovniHodnoceni) {
            this.bmi = bmi;
            this.slovniHodnoceni = slovniHodnoceni;
        }
    }

    public static BmiVysledek spocitejBmi() {
        String jmeno = "";
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
            System.out.println("Unable to load profile data for BMI: " + e.getMessage());
        }
        //WEIGHT
        /*try {
            File fVahy = new File("vahy.txt");
            if (fVahy.exists()) {
                List<String> radky = Files.readAllLines(fVahy.toPath());
                if (!radky.isEmpty()) {
                    String posledniRadek = radky.get(radky.size() - 1);
                    String[] casti = posledniRadek.split(";");
                    if (casti.length == 2) {
                        vahaKg = Double.parseDouble(casti[1]);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Chyba při čtení váhy pro BMI: " + e.getMessage());
        }*/
        // 2. Načtení váhy z vahy.txt (přednostně pro dnešní den)
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
            System.out.println("Unable to load weight for BMI: " + e.getMessage());
        }
        //



        if (vyskaCm <= 0 || vahaKg <= 0) {
            return new BmiVysledek(0, "Required data (height/weight) is missing.");
        }


        double vyskaMetry = vyskaCm / 100.0;
        double bmi = vahaKg / (vyskaMetry * vyskaMetry);
        bmi = Math.round(bmi * 10.0) / 10.0;


        int vek = LocalDate.now().getYear() - rokNarozeni;
        String hodnoceni = vyhodnotBmi(bmi, pohlavi, vek);

        return new BmiVysledek(bmi, hodnoceni);
    }

    /*private static String vyhodnotBmi(double bmi, String pohlavi, int vek) {

        double podvahaHranice = pohlavi.equalsIgnoreCase("Female") ? 18.5 : 19.0;
        double normaHranice = pohlavi.equalsIgnoreCase("Female") ? 24.0 : 25.0;
        double nadvahaHranice = pohlavi.equalsIgnoreCase("Female") ? 29.0 : 30.0;


        if (vek > 60) {
            podvahaHranice += 1.0;
            normaHranice += 1.0;
            nadvahaHranice += 1.0;
        }

        if (bmi < podvahaHranice) return "Podváha";
        if (bmi < normaHranice) return "Normální váha";
        if (bmi < nadvahaHranice) return "Nadváha";
        return "Obezita";
    }*/
    private static String vyhodnotBmi(double bmi, String pohlavi, int vek) {


        double podvahaHranice = pohlavi.equalsIgnoreCase("Female") ? 18.5 : 20.0;
        double normaHranice = pohlavi.equalsIgnoreCase("Female") ? 24.0 : 25.0;
        double nadvahaHranice = pohlavi.equalsIgnoreCase("Female") ? 29.0 : 30.0;

        if (vek > 60) {
            podvahaHranice += 1.0;
            normaHranice += 1.0;
            nadvahaHranice += 1.0;
        }

        if (bmi < podvahaHranice) {
            return "Underweight";
        } else if (bmi < normaHranice) {
            return "Normal";
        } else if (bmi < nadvahaHranice) {
            return "Overweight";
        } else {
            return "Obesity";
        }
    }

}

