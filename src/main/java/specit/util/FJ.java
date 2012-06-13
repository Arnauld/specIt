package specit.util;

import java.util.List;

/**
 *
 *
 */
public class FJ {

    public interface F<P,R> {
        R f(P param);
    }

    public static <T> int count(Iterable<? extends T> values, F<? super T, Boolean> filter) {
        int count = 0;
        for(T value : values) {
            if(filter.f(value))
                count++;
        }
        return count;
    }
}
