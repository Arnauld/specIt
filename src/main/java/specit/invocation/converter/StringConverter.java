package specit.invocation.converter;

import specit.SpecItRuntimeException;
import specit.invocation.Converter;

/**
 *
 *
 */
public class StringConverter implements Converter {

    @Override
    public Object fromString(String value) {
        if (isValid(value)) {
            return value;
        }
        throw new SpecItRuntimeException("Value <" + value + "> is not a valid string value");
    }

    @Override
    public String[] suggest(String prefix) {
        if (isValid(prefix)) {
            return new String[]{prefix};
        }
        return new String[0];
    }

    protected boolean isValid(String prefix) {
        return prefix != null;
    }
}
