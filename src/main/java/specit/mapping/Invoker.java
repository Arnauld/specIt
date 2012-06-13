package specit.mapping;

import specit.util.ParametrizedString;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 *
 *
 */
public class Invoker {

    private final ConverterRegistry converterRegistry;
    private final StepInstanceProvider stepInstanceProvider;

    public Invoker(ConverterRegistry converterRegistry, StepInstanceProvider stepInstanceProvider) {
        this.converterRegistry = converterRegistry;
        this.stepInstanceProvider = stepInstanceProvider;
    }

    public void invoke(String input, CandidateStep candidateStep) {
        Object[] arguments = prepareMethodArguments(input, candidateStep);
        Object instance = stepInstanceProvider.getInstance(candidateStep.getOwningType());
        Method method = candidateStep.getMethod();
        try {
            method.invoke(instance, arguments);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to invoke step on input [" + input + "]", e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Failed to invoke step on input [" + input + "]", e);
        }
    }

    private Object[] prepareMethodArguments(String input, CandidateStep candidateStep) {
        ParametrizedString pattern = candidateStep.getPattern();
        ParameterMapping[] parameterMappings = candidateStep.getParameterMappings();

        Map<String,String> variableValues = pattern.extractParameterValues(input);

        Object[] arguments = new Object[parameterMappings.length];
        for(ParameterMapping mapping : parameterMappings) {
            Converter converter = getConverter(mapping);
            int parameterIndex = mapping.getParameterIndex();
            String variableValue = variableValues.get(mapping.getVariableName());
            arguments[parameterIndex] = converter.fromString(variableValue);
        }
        return arguments;
    }

    private Converter getConverter(ParameterMapping mapping) {
        if(mapping.hasConverterClass()) {
            return converterRegistry.getConverter(mapping.getConverterClass());
        }
        else {
            return converterRegistry.getConverterForType(mapping.getParameterType());
        }
    }
}
