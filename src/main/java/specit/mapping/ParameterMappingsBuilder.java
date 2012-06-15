package specit.mapping;

import specit.annotation.Variable;
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

        int nbParameters = parameterTypes.length;
        int nbVariables = pattern.getParameterCount();

        // TODO nbVariables can be >= nbParameters ?
        if (nbVariables != nbParameters) {
            throw new IllegalArgumentException("Incompatible number of variables and method parameters on " + method);
        }

        List<String> variableNames = pattern.getParameters();
        Set<String> uniqueVariableNames = New.hashSet(variableNames);
        if (uniqueVariableNames.size() != variableNames.size()) {
            throw new IllegalArgumentException("Duplicate variable name (" + variableNames + ") in pattern on " + method);
        }

        initializeParameterMappings(nbParameters);
        Map<String, Integer> variableNameToParamIndex =
                generateVariableNameToParameterIndex(method.getParameterAnnotations(), nbParameters);

        if (!variableNameToParamIndex.isEmpty()) {
            if (variableNameToParamIndex.size() != nbVariables)
                throw new IllegalArgumentException("All parameters or none must define @" + Variable.class.getName() + " on " + method);
            for (String variableName : variableNameToParamIndex.keySet()) {
                int parameterIndex = variableNameToParamIndex.get(variableName);
                if (!uniqueVariableNames.contains(variableName)) {
                    throw new IllegalArgumentException("Variable name mismatch between @" + Variable.class.getName() + " (" + variableNameToParamIndex.keySet() + ") and step pattern (" + uniqueVariableNames + ")");
                }
                defineParameterMapping(parameterIndex, variableName);
            }
        } else {
            for (int parameterIndex = 0; parameterIndex < nbParameters; parameterIndex++) {
                defineParameterMapping(parameterIndex, variableNames.get(parameterIndex));
            }
        }
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

    private void initializeParameterMappings(int nbParameters) {
        parameterMappings = new ParameterMapping[nbParameters];
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
