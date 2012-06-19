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
    private final InstanceProvider instanceProvider;

    public Invoker(ConverterRegistry converterRegistry, InstanceProvider instanceProvider) {
        this.converterRegistry = converterRegistry;
        this.instanceProvider = instanceProvider;
    }

    public void invoke(InvocationContext context, Lifecycle lifecycle) {
        if(!context.canInvokeLifecycle(lifecycle))
            return;

        try {
            Object instance = instanceProvider.getInstance(lifecycle.getOwningType());
            Method method = lifecycle.getMethod();
            Class<?>[] parameterTypes = method.getParameterTypes();
            if(parameterTypes.length==1) {
                if(InvocationContext.class.isAssignableFrom(parameterTypes[0])) {
                    method.invoke(instance, context);
                }
                else {
                    context.lifecycleInvocationFailed(lifecycle, "Invalid parameter type on lifecycle <" + method + ">");
                }
            }
            else if(parameterTypes.length>1) {
                context.lifecycleInvocationFailed(lifecycle, "Invalid number of parameter on lifecycle <" + method + ">");
            }
            else {
                method.invoke(instance);
            }
        } catch (IllegalAccessException e) {
            context.lifecycleInvocationFailed(lifecycle, "Failed to invoke lifecycle", e);
        } catch (InvocationTargetException e) {
            context.lifecycleInvocationFailed(lifecycle, "Failed to invoke lifecycle", e);
        } catch (InstanceProviderException e) {
            context.lifecycleInvocationFailed(lifecycle, "Failed to invoke lifecycle", e);
        }
    }

    public void invoke(InvocationContext context, String input, CandidateStep candidateStep) {
        if(!context.canInvokeStep(input, candidateStep))
            return;

        try {
            Object[] arguments = prepareMethodArguments(context, input, candidateStep);
            Object instance = instanceProvider.getInstance(candidateStep.getOwningType());
            Method method = candidateStep.getMethod();
            method.invoke(instance, arguments);
        } catch (IllegalAccessException e) {
            context.stepInvocationFailed(input, candidateStep, "Failed to invoke step on input [" + input + "]", e);
        } catch (InvocationTargetException e) {
            context.stepInvocationFailed(input, candidateStep, "Failed to invoke step on input [" + input + "]", e);
        } catch (ConverterException e) {
            context.stepInvocationFailed(input, candidateStep, "Failed to invoke step on input [" + input + "]", e);
        } catch (InstanceProviderException e) {
            context.stepInvocationFailed(input, candidateStep, "Failed to invoke step on input [" + input + "]", e);
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
