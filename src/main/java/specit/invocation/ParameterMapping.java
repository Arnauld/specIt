package specit.invocation;

import specit.ScenarioContext;
import specit.StoryContext;
import specit.annotation.UserContext;
import specit.element.InvocationContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
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
    private final Method method;

    public ParameterMapping(Method method,
                            int parameterIndex)
    {
        this(method, parameterIndex, null, null);
    }

    public ParameterMapping(Method method,
                            int parameterIndex,
                            String variableName,
                            Class<? extends Converter> converterClass)
    {
        this.method = method;
        this.parameterIndex = parameterIndex;
        this.variableName = variableName;
        this.converterClass = converterClass;

        Class<?>[] parameterTypes = method.getParameterTypes();
        Annotation[][] parametersAnnotations = method.getParameterAnnotations();

        this.parameterType = parameterTypes[parameterIndex];
        this.parameterAnnotations = parametersAnnotations[parameterIndex];
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

    public Object extractValue(ConverterRegistry converterRegistry,
                               InvocationContext context,
                               Map<String, String> variableValues) throws ConverterException
    {
        UserContext userContextAnnotation = userContextParameterAnnotation();
        if (userContextAnnotation != null) {
            return context.lookupUserContextOrFail(parameterType, userContextAnnotation);
        }
        else if (parameterType.equals(InvocationContext.class)) {
            return context;
        }
        else if (parameterType.equals(ScenarioContext.class)) {
            return context.getScenarioContext();
        }
        else if (parameterType.equals(StoryContext.class)) {
            return context.getStoryContext();
        }
        else if (hasVariableName()) {
            Converter converter = getConverter(converterRegistry);
            String variableValue = variableValues.get(getVariableName());
            return converter.fromString(variableValue);
        }
        else {
            throw new ParameterMappingRuntimeException(
                    "Unable to extract value for parameter [" + parameterIndex + ", " + parameterType + "]"
                            + " on method " + method.getDeclaringClass().getName() + "#" + method.getName());
        }
    }

    private boolean hasVariableName() {
        return variableName != null;
    }

    private Converter getConverter(ConverterRegistry converterRegistry) throws ConverterException {
        if (hasConverterClass()) {
            return converterRegistry.getConverter(getConverterClass());
        }
        else {
            return converterRegistry.getConverterForType(getParameterType());
        }
    }

    public UserContext userContextParameterAnnotation() {
        for (Annotation annotation : parameterAnnotations) {
            if (annotation.annotationType().equals(UserContext.class)) {
                return (UserContext) annotation;
            }
        }
        return null;
    }
}
