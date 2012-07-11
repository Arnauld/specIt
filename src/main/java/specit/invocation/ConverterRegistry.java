package specit.invocation;

import specit.util.New;

import java.util.Map;
import java.util.Set;

/**
 *
 *
 */
public class ConverterRegistry {

    private Map<Class<? extends Converter>, Converter> converterInstances = New.hashMap();
    private Map<Class<?>, Converter> converterPerType = New.hashMap();
    private Set<Converter> converters = New.linkedHashSet();

    public void registerConverter(Class<?> toType, Converter converter) {
        converterPerType.put(toType, converter);
    }

    public void registerConverter(Converter converter) {
        converters.add(converter);
    }

    public Converter getConverter(Class<? extends Converter> converterClass) throws ConverterException {
        Converter converter = converterInstances.get(converterClass);
        if (converter == null) {
            try {
                converter = converterClass.newInstance();
            }
            catch (InstantiationException e) {
                throw new ConverterException(
                        "Failed to instantiate new converter from class <" + converterClass + ">", e);
            }
            catch (IllegalAccessException e) {
                throw new ConverterException(
                        "Failed to instantiate new converter from class <" + converterClass + ">", e);
            }
            converterInstances.put(converterClass, converter);
        }
        return converter;
    }

    public Converter getConverterForType(Class<?> toType) throws ConverterException {
        Converter converter = converterPerType.get(toType);
        if (converter == null) {
            for(Converter c : converters) {
                if(c.canConvertTo(toType))
                    return c;
            }
            throw new ConverterException("No converter defined for type <" + toType + ">");
        }
        return converter;
    }
}
