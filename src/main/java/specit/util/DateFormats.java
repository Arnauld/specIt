package specit.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

/**
 *
 *
 */
public class DateFormats {

    public static List<DateFormat> toDateFormats(String...patterns) {
        return FJ.map(Arrays.asList(patterns), new FJ.F<String,DateFormat>() {
            @Override
            public DateFormat f(String pattern) {
                return new SimpleDateFormat(pattern);
            }
        });
    }
}
