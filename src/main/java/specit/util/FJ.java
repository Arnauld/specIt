package specit.util;

import java.util.List;

/**
 *
 *
 */
public final class FJ {

    private FJ() {
    }

    public interface F<P, R> {
        R f(P param);
    }

    public static <T> int count(Iterable<? extends T> values, F<? super T, Boolean> filter) {
        int count = 0;
        for (T value : values) {
            if (filter.f(value)) {
                count++;
            }
        }
        return count;
    }

    public static <R,T> List<T> map(Iterable<? extends R> values, F<? super R,T> mapper) {
        List<T> mappedValues = New.arrayList();
        for(R param : values) {
            mappedValues.add(mapper.f(param));
        }
        return mappedValues;
    }
}
