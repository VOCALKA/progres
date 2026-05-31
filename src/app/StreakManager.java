package app;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StreakManager {


    /**
     * Parses the historical log file "weights.txt" to collect all unique calendar dates that contain weight records.
     * Utilizes a {@link HashSet} collection wrapper to automatically filter out multiple logging instances committed on the same day.
     *
     * @return A unique {@link Set} collection of {@link LocalDate} objects representing valid entry dates.
     */
    private static Set<LocalDate> loadWeightRecords() {
        Set<LocalDate> dataZpisu = new HashSet<>();
        try {
            File fVahy = new File("weights.txt");
            if (fVahy.exists()) {
                List<String> radky = Files.readAllLines(fVahy.toPath());
                for (String radek : radky) {
                    String[] casti = radek.split(";");
                    if (casti.length >= 1) {
                        try {

                            dataZpisu.add(LocalDate.parse(casti[0].trim()));
                        } catch (Exception e) {

                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return dataZpisu;
    }


    /**
     * Evaluates chronological patterns backward to gauge the absolute length of the current unbroken logging streak.
     * Validates baseline anchor parameters against today's or yesterday's date, breaking immediately with a zero score
     * if both dates are vacant, or loops decrementally through calendar nodes to tally the streak sum.
     *
     * @return The integer sum total representing consecutive unbroken days of weight logs.
     */
    public static int getWeightStreak() {
        Set<LocalDate> dataZpisu = loadWeightRecords();
        LocalDate dnes = LocalDate.now();
        LocalDate vcera = dnes.minusDays(1);


        if (!dataZpisu.contains(dnes) && !dataZpisu.contains(vcera)) {
            return 0;
        }

        int streak = 0;

        LocalDate kontrolniDen = dataZpisu.contains(dnes) ? dnes : vcera;


        while (dataZpisu.contains(kontrolniDen)) {
            streak++;
            kontrolniDen = kontrolniDen.minusDays(1);
        }

        return streak;
    }
}

