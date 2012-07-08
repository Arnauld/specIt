package specit.invocation;

import specit.ScenarioContext;
import specit.StoryContext;
import specit.annotation.UserContext;
import specit.annotation.Variable;
import specit.element.InvocationContext;
import specit.util.New;
import specit.util.ParametrizedString;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
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

    public ParameterMappingsBuilder(Method method) {
        this(method, null);
    }

    public ParameterMappingsBuilder(Method method, ParametrizedString pattern) {
        this.method = method;
        this.pattern = pattern;
    }

    public ParameterMapping[] getParameterMappings() throws ParameterMappingException {
        generateParameterMappings();
        return parameterMappings;
    }

    private void generateParameterMappings() throws ParameterMappingException {
        final int nbParameters = method.getParameterTypes().length;
        final int nbVariables = (pattern == null) ? 0 : pattern.getParameterCount();

        // if one parameterType is an instance of InvocationContext then it won't
        // be matched to any variable
        int nbVariablesRequired = countNonSpecialParameters(method);
        if (nbVariables != nbVariablesRequired) {
            throw new ParameterMappingException(
                    "Incompatible number of variables and method parameters on " + formatMethod(method)
                            + " number of variables: " + nbVariables + ","
                            + " number of non special variables: " + nbVariablesRequired);
        }

        List<String> variableNames = (pattern == null) ? Collections.<String>emptyList() : pattern.getParameters();
        Set<String> uniqueVariableNames = New.hashSet(variableNames);
        if (uniqueVariableNames.size() != variableNames.size()) {
            throw new ParameterMappingException(
                    "Duplicate variable name (" + variableNames + ") in pattern on " + formatMethod(method));
        }

        parameterMappings = new ParameterMapping[nbParameters];

        Map<String, Integer> variableNameToParamIndex =
                generateVariableNameToParameterIndex(method.getParameterAnnotations());

        if (!variableNameToParamIndex.isEmpty()) {
            createMappingsUsingVariableNames(uniqueVariableNames, variableNameToParamIndex);
        }
        else {
            createMappingsUsingParameterOrders(variableNames);
        }
    }

    private static String formatMethod(Method method) {
        return method.getDeclaringClass().getName() + '#' + method.getName();
    }

    private void createMappingsUsingParameterOrders(List<String> variableNames) {
        int nbParameters = nbParametersOf(method);
        int nonSpecialIndex = 0;
        for (int parameterIndex = 0; parameterIndex < nbParameters; parameterIndex++) {
            if (isParameterSpecial(method, parameterIndex)) {
                defineSpecialParameterMapping(parameterIndex);
            }
            else {
                defineParameterMapping(parameterIndex, variableNames.get(nonSpecialIndex++));
            }
        }
    }

    private void createMappingsUsingVariableNames(Set<String> uniqueVariableNames,
                                                  Map<String, Integer> variableNameToParamIndex)
            throws ParameterMappingException
    {
        if (variableNameToParamIndex.size() != uniqueVariableNames.size()) {
            throw new ParameterMappingException(
                    "All parameters or none must define @" + Variable.class.getName() + " on " + method);
        }
        for (Map.Entry<String, Integer> entry : variableNameToParamIndex.entrySet()) {
            String variableName = entry.getKey();
            int parameterIndex = entry.getValue();
            if (!uniqueVariableNames.contains(variableName)) {
                throw new ParameterMappingException("Variable name mismatch between @" + Variable.class.getName() + " ("
                        + variableNameToParamIndex.keySet() + ") and step pattern (" + uniqueVariableNames + ")");
            }
            defineParameterMapping(parameterIndex, variableName);
        }
        int nbParameters = method.getParameterTypes().length;
        // complete with specials
        for (int parameterIndex = 0; parameterIndex < nbParameters; parameterIndex++) {
            if (isParameterSpecial(method, parameterIndex)) {
                defineSpecialParameterMapping(parameterIndex);
            }
        }
    }

    private void defineSpecialParameterMapping(int parameterIndex) {
        parameterMappings[parameterIndex] =
                new ParameterMapping(
                        method,
                        parameterIndex);
    }

    private void defineParameterMapping(int parameterIndex, String variableName) {
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Class<? extends Converter> converterClass = lookupConverter(parameterAnnotations[parameterIndex]);
        parameterMappings[parameterIndex] =
                new ParameterMapping(
                        method,
                        parameterIndex,
                        variableName,
                        converterClass);
    }

    private static int nbParametersOf(Method method) {
        return method.getParameterTypes().length;
    }

    private static int countNonSpecialParameters(Method method) {
        int nonSpecials = 0;
        for (int i = 0, n = nbParametersOf(method); i < n; i++) {
            if (!isParameterSpecial(method, i)) {
                nonSpecials++;
            }
        }
        return nonSpecials;
    }

    private static boolean isParameterSpecial(Method method, int parameterIndex) {
        return isPredefinedContext(method.getParameterTypes()[parameterIndex])
                || hasContextAnnotation(method.getParameterAnnotations()[parameterIndex]);
    }

    private static boolean hasContextAnnotation(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().equals(UserContext.class)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isPredefinedContext(Class<?> parameterType) {
        return StoryContext.class.isAssignableFrom(parameterType)
                || ScenarioContext.class.isAssignableFrom(parameterType)
                || InvocationContext.class.isAssignableFrom(parameterType);
    }

    private Map<String, Integer> generateVariableNameToParameterIndex(Annotation[][] parameterAnnotations)
            throws ParameterMappingException
    {
        Map<String, Integer> variableNameToParamIndex = New.hashMap();
        for (int index = 0; index < parameterAnnotations.length; index++) {
            // index as parameterIndex
            Variable variable = lookupVariable(parameterAnnotations[index]);
            if (variable != null) {
                Integer prevIndex = variableNameToParamIndex.put(variable.value(), index);
                if (prevIndex != null) {
                    throw new ParameterMappingException(
                            "Duplicate variable name (" + variable.value() + ") on " + method);
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
        if (converter == null) {
            return null;
        }
        else {
            return converter.value();
        }
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
