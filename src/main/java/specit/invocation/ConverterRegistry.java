package specit.invocation;

import specit.util.New;

import java.util.Map;

/**
 *
 *
 */
public class ConverterRegistry {

    private Map<Class<? extends Converter>, Converter> converterPerClass = New.hashMap();
    private Map<Class<?>, Converter> converterPerType = New.hashMap();

    public void registerConverter(Class<?> toType, Converter converter) {
        converterPerType.put(toType, converter);
    }

    public Converter getConverter(Class<? extends Converter> converterClass) throws ConverterException {
        Converter converter = converterPerClass.get(converterClass);
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
            converterPerClass.put(converterClass, converter);
        }
        return converter;
    }

    public Converter getConverterForType(Class<?> toType) throws ConverterException {
        Converter converter = converterPerType.get(toType);
        if (converter == null) {
            throw new ConverterException("No converter defined for type <" + toType + ">");
        }
        return converter;
    }
}
