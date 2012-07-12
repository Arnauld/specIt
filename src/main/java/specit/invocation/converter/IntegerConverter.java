package specit.invocation.converter;

import specit.SpecItRuntimeException;
import specit.invocation.Converter;

/**
 *
 *
 */
public class IntegerConverter implements Converter {

    @Override
    public boolean canConvertTo(Class<?> requiredType) {
        return (requiredType != null)
                && (requiredType.isAssignableFrom(int.class) || requiredType.isAssignableFrom(Integer.class));
    }

    @Override
    public Object fromString(Class<?> requiredType, String value) {
        if (requiredType == null) {
            throw new SpecItRuntimeException("RequiredType <null> is not assignable from Integer");
        }
        if (!requiredType.isAssignableFrom(int.class)
                && !requiredType.isAssignableFrom(Integer.class))
        {
            throw new SpecItRuntimeException("Incompatible required type, expected assignable from int or Integer");
        }
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
