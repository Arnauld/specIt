package specit.util;

public final class Equals {

    private Equals() {}

    public static <T> boolean areEquals(T o1, T o2) {
        if(o1==o2) {
            return true;
        }
        else if(o1==null || o2==null) {
            return false;
        }
        else {
            return o1.equals(o2);
        }
    }
}
