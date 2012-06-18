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

    public ParameterMapping[] getParameterMappings() {
        generateParameterMappings();
        return parameterMappings;
    }

    private void generateParameterMappings() {
        Class<?>[] parameterTypes = method.getParameterTypes();

        final int nbParameters = parameterTypes.length;
        final int nbVariables = pattern.getParameterCount();

        int nbVariablesRequired = nbParameters;
        // if one parameterType is an instance of InvocationContext then it won't
        // be matched to any variable
        for (Class<?> parameterType : parameterTypes) {
            if (isSpecialArgument(parameterType)) {
                nbVariablesRequired--;
            }
        }

        // TODO nbVariables can be >= nbParameters ?
        if (nbVariables != nbVariablesRequired) {
            throw new IllegalArgumentException("Incompatible number of variables and method parameters on " + method);
        }

        List<String> variableNames = pattern.getParameters();
        Set<String> uniqueVariableNames = New.hashSet(variableNames);
        if (uniqueVariableNames.size() != variableNames.size()) {
            throw new IllegalArgumentException("Duplicate variable name (" + variableNames + ") in pattern on " + method);
        }

        parameterMappings = new ParameterMapping[nbParameters];

        Map<String, Integer> variableNameToParamIndex =
                generateVariableNameToParameterIndex(method.getParameterAnnotations(), nbParameters);

        if (!variableNameToParamIndex.isEmpty()) {
            if (variableNameToParamIndex.size() != nbVariablesRequired)
                throw new IllegalArgumentException("All parameters or none must define @" + Variable.class.getName() + " on " + method);
            for (String variableName : variableNameToParamIndex.keySet()) {
                int parameterIndex = variableNameToParamIndex.get(variableName);
                if (!uniqueVariableNames.contains(variableName)) {
                    throw new IllegalArgumentException("Variable name mismatch between @" + Variable.class.getName() + " (" + variableNameToParamIndex.keySet() + ") and step pattern (" + uniqueVariableNames + ")");
                }
                defineParameterMapping(parameterIndex, variableName);
            }
            // complete with specials
            for (int parameterIndex = 0; parameterIndex < nbParameters; parameterIndex++) {
                if (isSpecialArgument(parameterTypes[parameterIndex]))
                    defineSpecialParameterMapping(parameterIndex);
            }
        } else {
            int nonSpecialIndex = 0;
            for (int parameterIndex = 0; parameterIndex < nbParameters; parameterIndex++) {
                if (isSpecialArgument(parameterTypes[parameterIndex]))
                    defineSpecialParameterMapping(parameterIndex);
                else
                    defineParameterMapping(parameterIndex, variableNames.get(nonSpecialIndex++));
            }
        }
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

    private Map<String, Integer> generateVariableNameToParameterIndex(Annotation[][] parameterAnnotations, int nbParameters) {
        Map<String, Integer> variableNameToParamIndex = New.hashMap();
        for (int index = 0; index < nbParameters; index++) {
            // index as parameterIndex
            Variable variable = lookupVariable(parameterAnnotations[index]);
            if (variable != null) {
                Integer prevIndex = variableNameToParamIndex.put(variable.value(), index);
                if (prevIndex != null) {
                    throw new IllegalArgumentException("Duplicate variable name (" + variable.value() + ") on " + method);
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
