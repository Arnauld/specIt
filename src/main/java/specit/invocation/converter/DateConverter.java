package specit.invocation.converter;

import specit.SpecItRuntimeException;
import specit.invocation.Converter;
import specit.util.Time;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.util.Date;

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
        if (value == null) {
            throw new SpecItRuntimeException("Value <null> is not a valid date value");
        }

        synchronized (dateFormat) {
            String trimmed = value.trim();
            ParsePosition pos = new ParsePosition(0);
            Date parse = dateFormat.parse(trimmed, pos);
            if (parse != null
                    // make sure there is no remaining!
                    && pos.getIndex() == trimmed.length())
            {
                return parse;
            }
            throw new SpecItRuntimeException("Value <" + value + "> is not a valid date value");
        }
    }

    @Override
    public String[] suggest(String prefix) {
        String trimmed = prefix.trim();

        String potential = dateFormat.format(new Date(Time.currentTimeMillis()));
        if (trimmed.length() < potential.length()) {
            String suggest = potential.substring(trimmed.length());
            if (isValid(trimmed + suggest)) {
                return new String[]{trimmed + suggest};
            }
        }
        else if (isValid(trimmed)) {
            return new String[]{trimmed};
        }
        return new String[0];
    }

    protected boolean isValid(String trimmed) {
        synchronized (dateFormat) {
            ParsePosition pos = new ParsePosition(0);
            Date parse = dateFormat.parse(trimmed, pos);
            return parse != null
                    // make sure there is no remaining!
                    && pos.getIndex() == trimmed.length();
        }
    }
}
