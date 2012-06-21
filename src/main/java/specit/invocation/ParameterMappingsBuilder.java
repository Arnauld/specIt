package specit.invocation;

import specit.annotation.Variable;
import specit.element.InvocationContext;
import specit.util.New;
import specit.util.ParametrizedString;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 *
 */
public class ParameterMappingsBuilder {

    // inputs
    private final Method method;
    private final ParametrizedString pattern;

    // results
    private ParameterMapping[] parameterMappings;

    public ParameterMappingsBuilder(Method method, ParametrizedString pattern) {
        this.method = method;
        this.pattern = pattern;
    }

    public ParameterMapping[] getParameterMappings() throws ParameterMappingException {
        generateParameterMappings();
        return parameterMappings;
    }

    private void generateParameterMappings() throws ParameterMappingException {
        Class<?>[] parameterTypes = method.getParameterTypes();

        final int nbParameters = parameterTypes.length;
        final int nbVariables = pattern.getParameterCount();

        // if one parameterType is an instance of InvocationContext then it won't
        // be matched to any variable
        int nbVariablesRequired = countNonSpecialParameters(parameterTypes);
        if (nbVariables != nbVariablesRequired) {
            throw new ParameterMappingException("Incompatible number of variables and method parameters on " + method);
        }

        List<String> variableNames = pattern.getParameters();
        Set<String> uniqueVariableNames = New.hashSet(variableNames);
        if (uniqueVariableNames.size() != variableNames.size()) {
            throw new ParameterMappingException("Duplicate variable name (" + variableNames + ") in pattern on " + method);
        }

        parameterMappings = new ParameterMapping[nbParameters];

        Map<String, Integer> variableNameToParamIndex =
                generateVariableNameToParameterIndex(method.getParameterAnnotations());

        if (!variableNameToParamIndex.isEmpty()) {
            createMappingsUsingVariableNames(uniqueVariableNames, variableNameToParamIndex);
        } else {
            createMappingsUsingParameterOrders(variableNames);
        }
    }

    private void createMappingsUsingParameterOrders(List<String> variableNames) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        int nbParameters = parameterTypes.length;
        int nonSpecialIndex = 0;
        for (int parameterIndex = 0; parameterIndex < nbParameters; parameterIndex++) {
            if (isSpecialArgument(parameterTypes[parameterIndex]))
                defineSpecialParameterMapping(parameterIndex);
            else
                defineParameterMapping(parameterIndex, variableNames.get(nonSpecialIndex++));
        }
    }

    private void createMappingsUsingVariableNames(Set<String> uniqueVariableNames, Map<String, Integer> variableNameToParamIndex) throws ParameterMappingException {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (variableNameToParamIndex.size() != uniqueVariableNames.size())
            throw new ParameterMappingException("All parameters or none must define @" + Variable.class.getName() + " on " + method);
        for (Map.Entry<String, Integer> entry : variableNameToParamIndex.entrySet()) {
            String variableName = entry.getKey();
            int parameterIndex = entry.getValue();
            if (!uniqueVariableNames.contains(variableName)) {
                throw new ParameterMappingException("Variable name mismatch between @" + Variable.class.getName() + " (" + variableNameToParamIndex.keySet() + ") and step pattern (" + uniqueVariableNames + ")");
            }
            defineParameterMapping(parameterIndex, variableName);
        }
        int nbParameters = parameterTypes.length;
        // complete with specials
        for (int parameterIndex = 0; parameterIndex < nbParameters; parameterIndex++) {
            if (isSpecialArgument(parameterTypes[parameterIndex]))
                defineSpecialParameterMapping(parameterIndex);
        }
    }

    private int countNonSpecialParameters(Class<?>[] parameterTypes) {
        int nonSpecials = 0;
        for (Class<?> parameterType : parameterTypes) {
            if (!isSpecialArgument(parameterType)) {
                nonSpecials++;
            }
        }
        return nonSpecials;
    }

    private void defineSpecialParameterMapping(int parameterIndex) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        parameterMappings[parameterIndex] =
                new ParameterMapping(
                        parameterTypes[parameterIndex],
                        parameterIndex);
    }

    private void defineParameterMapping(int parameterIndex, String variableName) {
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Class<?>[] parameterTypes = method.getParameterTypes();
        Class<? extends Converter> converterClass = lookupConverter(parameterAnnotations[parameterIndex]);
        parameterMappings[parameterIndex] =
                new ParameterMapping(
                        parameterTypes[parameterIndex],
                        parameterIndex,
                        variableName,
                        converterClass);
    }

    private boolean isSpecialArgument(Class<?> parameterType) {
        return InvocationContext.class.isAssignableFrom(parameterType);
    }

    private Map<String, Integer> generateVariableNameToParameterIndex(Annotation[][] parameterAnnotations) throws ParameterMappingException {
        Map<String, Integer> variableNameToParamIndex = New.hashMap();
        for (int index = 0; index < parameterAnnotations.length; index++) {
            // index as parameterIndex
            Variable variable = lookupVariable(parameterAnnotations[index]);
            if (variable != null) {
                Integer prevIndex = variableNameToParamIndex.put(variable.value(), index);
                if (prevIndex != null) {
                    throw new ParameterMappingException("Duplicate variable name (" + variable.value() + ") on " + method);
                }
            }
        }
        return variableNameToParamIndex;
    }

    private static Variable lookupVariable(Annotation[] annotations) {
        return lookupAnnotation(specit.annotation.Variable.class, annotations);
    }

    private static Class<? extends Converter> lookupConverter(Annotation[] annotations) {
        specit.annotation.Converter converter = lookupAnnotation(specit.annotation.Converter.class, annotations);
        if (converter == null)
            return null;
        else
            return converter.value();
    }

    @SuppressWarnings("unchecked")
    private static <T extends Annotation> T lookupAnnotation(Class<T> type, Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (type.isInstance(annotation)) {
                return (T) annotation;
            }
        }
        return null;
    }
}
