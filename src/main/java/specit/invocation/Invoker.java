package specit.invocation;

import specit.element.InvocationContext;
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

    public void invoke(InvocationContext context, Lifecycle lifecycle) {
        Object instance = stepInstanceProvider.getInstance(lifecycle.getOwningType());
        Method method = lifecycle.getMethod();
        try {
            method.invoke(instance);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to invoke lifecycle", e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Failed to invoke lifecycle", e);
        }
    }

    public void invoke(InvocationContext context, String input, CandidateStep candidateStep) {
        Object[] arguments = prepareMethodArguments(context, input, candidateStep);
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

    private Object[] prepareMethodArguments(InvocationContext context, String input, CandidateStep candidateStep) {
        ParametrizedString pattern = candidateStep.getPattern();
        ParameterMapping[] parameterMappings = candidateStep.getParameterMappings();

        Map<String, String> variableValues = pattern.extractParameterValues(input);

        Object[] arguments = new Object[parameterMappings.length];
        for (ParameterMapping mapping : parameterMappings) {
            int parameterIndex = mapping.getParameterIndex();
            arguments[parameterIndex] = mapping.extractValue(converterRegistry, context, variableValues);
        }
        return arguments;
    }
}
