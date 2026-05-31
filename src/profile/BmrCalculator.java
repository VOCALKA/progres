package profile;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;

public class BmrCalculator {

    public static class BmrResult {
        public int bmrBasal;
        public int recommendedIntake;

        public BmrResult(int bmrBasal, int recommendedIntake) {
            this.bmrBasal = bmrBasal;
            this.recommendedIntake = recommendedIntake;
        }
    }

    /**
     * Computes the final user metabolic stats based on stored disk files.
     * Extracts bodily height metrics and biological markers, loads the correct synchronized calendar date weight,
     * adjusts mathematical formulas across gender pathways, and builds the return data structure payload.
     *
     * @return A populated {@link BmrResult} dataset containing parsed metric numbers, or zeros if inputs are invalid.
     */
    public static BmrResult countBmr() {
        int birthYear = 2000;
        double heightCm = 0;
        String gender = "Not entered";
        double weightKg = 0;


        try {
            File fProfil = new File("profil.txt");
            if (fProfil.exists()) {
                List<String> lines = Files.readAllLines(fProfil.toPath());
                for (String line : lines) {
                    if (line.startsWith("Year of birth: ")) {
                        birthYear = Integer.parseInt(line.replace("Year of birth: ", "").trim());
                    }
                    if (line.startsWith("Height: ")) {
                        String v = line.replace("Height: ", "").trim();
                        if (!v.isEmpty()) heightCm = Double.parseDouble(v);
                    }
                    if (line.startsWith("Gender: ")) {
                        gender = line.replace("Gender: ", "").trim();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Unable to load profile data for BMR: " + e.getMessage());
        }

        try {
            File fWeight = new File("weights.txt");
            if (fWeight.exists()) {
                List<String> lines = Files.readAllLines(fWeight.toPath());
                String todaysDate = LocalDate.now().toString();
                double lastRecordedWeight = 0;
                boolean todayWeightFound = false;

                for (String line : lines) {
                    String[] parts = line.split(";");
                    if (parts.length == 2) {
                        String dateFromFile = parts[0].trim();
                        double weightFromFile = Double.parseDouble(parts[1].trim());
                        lastRecordedWeight = weightFromFile;

                        if (dateFromFile.equals(todaysDate)) {
                            weightKg = weightFromFile;
                            todayWeightFound = true;
                            break;
                        }
                    }
                }
                if (!todayWeightFound) {
                    weightKg = lastRecordedWeight;
                }
            }
        } catch (Exception e) {
            System.out.println("Unable to load weight for BMR: " + e.getMessage());
        }

        if (heightCm <= 0 || weightKg <= 0) {
            return new BmrResult(0, 0);
        }

        int vek = LocalDate.now().getYear() - birthYear;


        double bmr;
        if (gender.equalsIgnoreCase("Female")) {

            bmr = (10 * weightKg) + (6.25 * heightCm) - (5 * vek) - 161;
        } else {

            bmr = (10 * weightKg) + (6.25 * heightCm) - (5 * vek) + 5;
        }

        double intake = bmr * 1.2;

        return new BmrResult((int) Math.round(bmr), (int) Math.round(intake));
    }
}

