package specit.util;

/**
 *
 *
 */
public class Time {

    private static long currentTimeMillis = -1;

    public static void timeFreeze(long fixedCurrentTimeMillis) {
        Time.currentTimeMillis = fixedCurrentTimeMillis;
    }

    public static void timeUnFreeze() {
        Time.currentTimeMillis = -1;
    }

    public static long currentTimeMillis() {
        if (currentTimeMillis == -1) {
            return System.currentTimeMillis();
        }
        return currentTimeMillis;
    }
}
