package profile;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;

public class BmiCalculator {

    public static class BmiResult {
        public double bmi;
        public String verbalEvaluation;

        public BmiResult(double bmi, String verbalEvaluation) {
            this.bmi = bmi;
            this.verbalEvaluation = verbalEvaluation;
        }
    }

    /**
     * Performs the full lifecycle of fetching user metrics and calculating the current BMI.
     * Parses profile data for dimensions, locates the appropriate weight data point,
     * handles missing-data safeguards, computes chronological age, and triggers categorization.
     *
     * @return A populated {@link BmiResult} object containing the final stats, or an empty result with an error string.
     */
    public static BmiResult countBmi() {
        String name = "";
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
            System.out.println("Unable to load profile data for BMI: " + e.getMessage());
        }
        //WEIGHT
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
            System.out.println("Unable to load weight for BMI: " + e.getMessage());
        }
        //



        if (heightCm <= 0 || weightKg <= 0) {
            return new BmiResult(0, "Required data (height/weight) is missing.");
        }


        double heightMeters = heightCm / 100.0;
        double bmi = weightKg / (heightMeters * heightMeters);
        bmi = Math.round(bmi * 10.0) / 10.0;


        int age = LocalDate.now().getYear() - birthYear;
        String evaluation = evaluateBmi(bmi, gender, age);

        return new BmiResult(bmi, evaluation);
    }

    /**
     * Determines the specific BMI classification segment for a user.
     * Dynamically shifts evaluation thresholds using standard medical variances
     * based on biological gender, and accounts for geriatric metabolism adjustments (+1.0 threshold)
     * if the user's age is greater than 60.
     *
     * @param bmi    The rounded calculation value of the index.
     * @param gender The specified bio-gender category ("Male"/"Female").
     * @param age    The chronological age of the user in years.
     * @return A string literal matching the result status ("Underweight", "Normal", "Overweight", "Obesity").
     */
    private static String evaluateBmi(double bmi, String gender, int age) {


        double underweightLimit = gender.equalsIgnoreCase("Female") ? 18.5 : 20.0;
        double normalLimit = gender.equalsIgnoreCase("Female") ? 24.0 : 25.0;
        double overweightLimit = gender.equalsIgnoreCase("Female") ? 29.0 : 30.0;

        if (age > 60) {
            underweightLimit += 1.0;
            normalLimit += 1.0;
            overweightLimit += 1.0;
        }

        if (bmi < underweightLimit) {
            return "Underweight";
        } else if (bmi < normalLimit) {
            return "Normal";
        } else if (bmi < overweightLimit) {
            return "Overweight";
        } else {
            return "Obesity";
        }
    }

}

