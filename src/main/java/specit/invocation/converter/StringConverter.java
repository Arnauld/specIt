package specit.invocation.converter;

import specit.SpecItRuntimeException;
import specit.invocation.Converter;

/**
 *
 *
 */
public class StringConverter implements Converter {

    @Override
    public boolean canConvertTo(Class<?> requiredType) {
        return (requiredType != null)
                && requiredType.isAssignableFrom(String.class);
    }

    @Override
    public Object fromString(Class<?> requiredType, String value) {
        if (requiredType == null) {
            throw new SpecItRuntimeException("RequiredType <null> is not assignable from String");
        }
        if (!requiredType.isAssignableFrom(String.class)) {
            throw new SpecItRuntimeException("Incompatible required type, expected assignable from String");
        }
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
