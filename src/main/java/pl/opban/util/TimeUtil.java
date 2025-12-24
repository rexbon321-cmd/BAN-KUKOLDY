package pl.opban.util;

public class TimeUtil {

    // używane przez GUI (BŁĄD BYŁ TUTAJ)
    public static long days(int d) {
        return d * 86_400_000L;
    }

    // używane przy bardziej rozbudowanym wyborze czasu
    public static long toMillis(int amount, String unit) {
        return switch (unit) {
            case "MINUTY" -> amount * 60_000L;
            case "GODZINY" -> amount * 3_600_000L;
            case "DNI" -> amount * 86_400_000L;
            case "TYGODNIE" -> amount * 604_800_000L;
            default -> 0L;
        };
    }

    // ładne formatowanie do /baninfo
    public static String format(long ms) {
        if (ms <= 0) return "PERM";

        long seconds = ms / 1000;
        if (seconds < 60) return seconds + "s";

        long minutes = seconds / 60;
        if (minutes < 60) return minutes + "m";

        long hours = minutes / 60;
        if (hours < 24) return hours + "h";

        long days = hours / 24;
        return days + "d";
    }
}
