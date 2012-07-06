package specit.invocation;

import specit.annotation.UserContext;
import specit.element.InvocationContext;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 *
 */
public class ParameterMapping {
    private final Class<?> parameterType;
    private final int parameterIndex;
    private final String variableName;
    private final Class<? extends Converter> converterClass;
    private final Annotation[] parameterAnnotations;

    public ParameterMapping(Class<?> parameterType, int parameterIndex, Annotation[] parameterAnnotations) {
        this(parameterType, parameterIndex, parameterAnnotations, null, null);
    }

    public ParameterMapping(Class<?> parameterType, int parameterIndex, Annotation[] parameterAnnotations, String variableName, Class<? extends Converter> converterClass) {
        this.parameterType = parameterType;
        this.parameterIndex = parameterIndex;
        this.variableName = variableName;
        this.converterClass = converterClass;
        this.parameterAnnotations = parameterAnnotations;
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
        UserContext userContextAnnotation = userContextParameterAnnotation();
        if (userContextAnnotation!=null) {
            Object userContext = context.lookupUserContext(parameterType, userContextAnnotation);
            return userContext;
        }
        else if (parameterType.equals(InvocationContext.class)) {
            return context;
        }
        else if (hasVariableName()) {
            Converter converter = getConverter(converterRegistry);
            String variableValue = variableValues.get(getVariableName());
            return converter.fromString(variableValue);
        }
        else {
            throw new ParameterMappingRuntimeException("Unable to extract value for parameter [" + parameterIndex + ", " + parameterType + "]");
        }
    }

    private boolean hasVariableName() {
        return variableName != null ;
    }

    private Converter getConverter(ConverterRegistry converterRegistry) throws ConverterException {
        if (hasConverterClass()) {
            return converterRegistry.getConverter(getConverterClass());
        } else {
            return converterRegistry.getConverterForType(getParameterType());
        }
    }

    public UserContext userContextParameterAnnotation() {
        for(Annotation annotation : parameterAnnotations) {
            if(annotation.annotationType().equals(UserContext.class))
                return (UserContext)annotation;
        }
        return null;
    }
}
