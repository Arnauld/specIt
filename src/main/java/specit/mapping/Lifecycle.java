package specit.mapping;

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

    public Lifecycle(Class<?> owningType, Method method, Annotation lifecycleAnnotation) {
        this.owningType = owningType;
        this.method = method;
        this.lifecycleAnnotation = lifecycleAnnotation;
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

    @Override
    public String toString() {
        return "Lifecycle{" +
                "owningType=" + owningType +
                ", method=" + method +
                ", lifecycle=" + lifecycleAnnotation +
                '}';
    }
}
