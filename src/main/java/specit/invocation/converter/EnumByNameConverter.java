package specit.invocation.converter;

import specit.SpecItRuntimeException;
import specit.invocation.Converter;

/**
 *
 *
 */
public class EnumByNameConverter implements Converter {

    @Override
    public boolean canConvertTo(Class<?> requiredType) {
        return (requiredType != null)
                && requiredType.isEnum();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object fromString(Class<?> requiredType, String value) {
        if (requiredType == null) {
            throw new SpecItRuntimeException("RequiredType <null> is required!");
        }
        if(!requiredType.isEnum()) {
            throw new SpecItRuntimeException("RequiredType <null> must refers to an enum!");
        }

        // cast to remove annoying generic issue!
        return Enum.valueOf((Class)requiredType, value);
    }

    @Override
    public String[] suggest(String input) {
        return new String[0];
    }
}
