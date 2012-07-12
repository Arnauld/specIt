package specit.invocation.converter;

import specit.SpecItRuntimeException;
import specit.invocation.Converter;
import specit.util.Time;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Converter a String into a {@link Date}. All specified patterns are tried one by one
 * according to their definition order until one can actually parse the date.
 */
public class DateConverter implements Converter {

    private final List<DateFormat> dateFormats;

    public DateConverter(DateFormat dateFormat) {
        this(Arrays.asList(dateFormat));
    }

    public DateConverter(List<DateFormat> dateFormats) {
        this.dateFormats = dateFormats;
    }

    @Override
    public boolean canConvertTo(Class<?> requiredType) {
        return (requiredType != null)
                && requiredType.isAssignableFrom(Date.class);
    }

    @Override
    public Object fromString(Class<?> requiredType, String value) {
        if (requiredType == null) {
            throw new SpecItRuntimeException("RequiredType <null> is not assignable from Date");
        }
        if (!requiredType.isAssignableFrom(Date.class)) {
            throw new SpecItRuntimeException("Incompatible required type, expected assignable from java.util.Date");
        }
        if (value == null) {
            throw new SpecItRuntimeException("Value <null> is not a valid date value");
        }

        for (DateFormat dateFormat : dateFormats) {
            String trimmed = value.trim();
            ParsePosition pos = new ParsePosition(0);
            Date date;
            synchronized (dateFormat) {
                date = dateFormat.parse(trimmed, pos);
            }
            if (isValid(trimmed, date, pos)) {
                return date;
            }
        }
        throw new SpecItRuntimeException("Value <" + value + "> is not a valid date value");
    }

    @Override
    public String[] suggest(String prefix) {
        String trimmed = prefix.trim();

        for (DateFormat dateFormat : dateFormats) {
            synchronized (dateFormat) {
                String potential = dateFormat.format(new Date(Time.currentTimeMillis()));
                if (trimmed.length() < potential.length()) {
                    String suggest = potential.substring(trimmed.length());
                    if (isValid(dateFormat, trimmed + suggest)) {
                        return new String[]{trimmed + suggest};
                    }
                }
                else if (isValid(dateFormat, trimmed)) {
                    return new String[]{trimmed};
                }
            }
        }
        return new String[0];
    }

    private static boolean isValid(String parsed, Date date, ParsePosition pos) {
        return date != null
                // make sure there is no remaining!
                && pos.getIndex() == parsed.length();
    }

    private static boolean isValid(DateFormat dateFormat, String trimmed) {
        ParsePosition pos = new ParsePosition(0);
        Date parse = dateFormat.parse(trimmed, pos);
        return isValid(trimmed, parse, pos);
    }
}
