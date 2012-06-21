package specit.invocation;

import specit.element.InvocationContext;
import specit.element.InvokableStep;
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
    private final InstanceProvider instanceProvider;

    public Invoker(ConverterRegistry converterRegistry, InstanceProvider instanceProvider) {
        this.converterRegistry = converterRegistry;
        this.instanceProvider = instanceProvider;
    }

    public void invoke(InvocationContext context, Lifecycle lifecycle) {
        if (!context.canInvokeLifecycle(lifecycle)) {
            context.lifecycleSkipped(lifecycle);
            return;
        }

        try {
            Object instance = instanceProvider.getInstance(lifecycle.getOwningType());
            Method method = lifecycle.getMethod();
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length == 1) {
                if (InvocationContext.class.isAssignableFrom(parameterTypes[0])) {
                    method.invoke(instance, context);
                    context.lifecycleInvoked(lifecycle);
                } else {
                    context.lifecycleInvocationFailed(lifecycle, "Invalid parameter type on lifecycle <" + method + ">");
                }
            } else if (parameterTypes.length > 1) {
                context.lifecycleInvocationFailed(lifecycle, "Invalid number of parameter on lifecycle <" + method + ">");
            } else {
                method.invoke(instance);
                context.lifecycleInvoked(lifecycle);
            }
        } catch (IllegalAccessException e) {
            context.lifecycleInvocationFailed(lifecycle, "Failed to invoke lifecycle", e);
        } catch (InvocationTargetException e) {
            context.lifecycleInvocationFailed(lifecycle, "Failed to invoke lifecycle", e);
        } catch (InstanceProviderException e) {
            context.lifecycleInvocationFailed(lifecycle, "Failed to invoke lifecycle", e);
        }
    }

    public void invoke(InvocationContext context, InvokableStep invokableStep, CandidateStep candidateStep) {
        if (!context.canInvokeStep(invokableStep, candidateStep)) {
            context.stepSkipped(invokableStep, candidateStep);
            return;
        }

        String input = invokableStep.getAdjustedInput();

        try {
            Object[] arguments = prepareMethodArguments(context, input, candidateStep);
            Object instance = instanceProvider.getInstance(candidateStep.getOwningType());
            Method method = candidateStep.getMethod();
            method.invoke(instance, arguments);
            context.stepInvoked(invokableStep, candidateStep);
        } catch (IllegalAccessException e) {
            context.stepInvocationFailed(invokableStep, candidateStep, "Failed to invoke step on input [" + input + "]", e);
        } catch (InvocationTargetException e) {
            context.stepInvocationFailed(invokableStep, candidateStep, "Failed to invoke step on input [" + input + "]", e);
        } catch (ConverterException e) {
            context.stepInvocationFailed(invokableStep, candidateStep, "Failed to invoke step on input [" + input + "]", e);
        } catch (InstanceProviderException e) {
            context.stepInvocationFailed(invokableStep, candidateStep, "Failed to invoke step on input [" + input + "]", e);
        }
    }

    private Object[] prepareMethodArguments(InvocationContext context, String input, CandidateStep candidateStep) throws ConverterException {
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
