package specit.mapping.converter;

import specit.mapping.Converter;

import java.text.DateFormat;
import java.text.ParseException;

/**
 *
 *
 */
public class DateConverter implements Converter {

    private final DateFormat dateFormat;

    public DateConverter(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    @Override
    public Object fromString(String value) {
        synchronized (dateFormat) {
            String trimmed = value.trim();
            try {
                return dateFormat.parse(trimmed);
            } catch (ParseException e) {
            }
        }
        throw new IllegalArgumentException("Value <" + value + "> is not a valid date value");
    }

    @Override
    public String[] suggest(String prefix) {
        if(isValid(prefix))
            return new String[] {prefix};
        return new String[0];
    }

    protected boolean isValid(String prefix) {
        synchronized (dateFormat) {
            String trimmed = prefix.trim();
            try {
                return dateFormat.parse(trimmed)!=null;
            } catch (ParseException e) {
                return false;
            }
        }
    }
}
