package specit.invocation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 *
 *
 */
public class Lifecycle {
    private final Class<?> owningType;
    private final Method method;
    private final Annotation lifecycleAnnotation;
    private final ParameterMapping[] parameterMappings;

    public Lifecycle(Class<?> owningType, Method method, Annotation lifecycleAnnotation, ParameterMapping[] parameterMappings) {
        this.owningType = owningType;
        this.method = method;
        this.lifecycleAnnotation = lifecycleAnnotation;
        this.parameterMappings = parameterMappings;
    }

    public Class<?> getOwningType() {
        return owningType;
    }

    public Method getMethod() {
        return method;
    }

    public Annotation getLifecycleAnnotation() {
        return lifecycleAnnotation;
    }

    public Class<? extends Annotation> getLifecycleType() {
        return lifecycleAnnotation.annotationType();
    }

    public ParameterMapping[] getParameterMappings() {
        return parameterMappings;
    }

    @Override
    public String toString() {
        return "Lifecycle{" +
                "owningType=" + owningType +
                ", method=" + method +
                ", lifecycle=" + lifecycleAnnotation +
                '}';
    }
}
