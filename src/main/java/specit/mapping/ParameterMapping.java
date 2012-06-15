package specit.mapping;

/**
 *
 */
public class ParameterMapping {
    private final Class<?> parameterType;
    private final int parameterIndex;
    private final String variableName;
    private final Class<? extends Converter> converterClass;

    public ParameterMapping(Class<?> parameterType, int parameterIndex, String variableName) {
        this(parameterType, parameterIndex, variableName, null);
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
}
