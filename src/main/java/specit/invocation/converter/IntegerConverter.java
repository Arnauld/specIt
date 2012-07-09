package specit.invocation.converter;

import specit.SpecItRuntimeException;
import specit.invocation.Converter;

/**
 *
 *
 */
public class IntegerConverter implements Converter {

    @Override
    public Object fromString(String value) {
        if (value == null) {
            throw new SpecItRuntimeException("Value <null> is not a valid integer value");
        }

        if (isValid(value)) {
            return Integer.parseInt(value);
        }
        throw new SpecItRuntimeException("Value <" + value + "> is not a valid integer value");
    }

    @Override
    public String[] suggest(String prefix) {
        if (isValid(prefix)) {
            return new String[]{prefix};
        }
        return new String[0];
    }

    protected boolean isValid(String prefix) {
        String trimmed = prefix.trim();
        return trimmed.matches("\\d+");
    }
}
