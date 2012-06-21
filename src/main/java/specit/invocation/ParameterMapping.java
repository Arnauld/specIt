package specit.invocation;

import specit.element.InvocationContext;

import java.util.Map;

/**
 *
 */
public class ParameterMapping {
    private final Class<?> parameterType;
    private final int parameterIndex;
    private final String variableName;
    private final Class<? extends Converter> converterClass;

    public ParameterMapping(Class<?> parameterType, int parameterIndex) {
        this(parameterType, parameterIndex, null, null);
    }

    public ParameterMapping(Class<?> parameterType, int parameterIndex, String variableName, Class<? extends Converter> converterClass) {
        this.parameterType = parameterType;
        this.parameterIndex = parameterIndex;
        this.variableName = variableName;
        this.converterClass = converterClass;
    }

    public Class<?> getParameterType() {
        return parameterType;
    }

    public int getParameterIndex() {
        return parameterIndex;
    }

    public String getVariableName() {
        return variableName;
    }

    public Class<? extends Converter> getConverterClass() {
        return converterClass;
    }

    public boolean hasConverterClass() {
        return converterClass != null;
    }

    public Object extractValue(ConverterRegistry converterRegistry, InvocationContext context, Map<String, String> variableValues) throws ConverterException {
        if (parameterType.equals(InvocationContext.class)) {
            return context;
        }
        Converter converter = getConverter(converterRegistry);
        String variableValue = variableValues.get(getVariableName());
        return converter.fromString(variableValue);
    }

    private Converter getConverter(ConverterRegistry converterRegistry) throws ConverterException {
        if (hasConverterClass()) {
            return converterRegistry.getConverter(getConverterClass());
        } else {
            return converterRegistry.getConverterForType(getParameterType());
        }
    }
}
