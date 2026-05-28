package app;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StreakManager {


    private static Set<LocalDate> nactiDataZpisuVah() {
        Set<LocalDate> dataZpisu = new HashSet<>();
        try {
            File fVahy = new File("vahy.txt");
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


    public static int getWeightStreak() {
        Set<LocalDate> dataZpisu = nactiDataZpisuVah();
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

