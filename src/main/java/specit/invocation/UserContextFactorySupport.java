package specit.invocation;

import specit.annotation.UserContextFactory;
import specit.annotation.UserContextScope;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 */
public class UserContextFactorySupport {
    private final Class<?> klazz;
    private final Method method;
    private final UserContextFactory factoryAnnotation;

    public UserContextFactorySupport(Class<?> klazz, Method method, UserContextFactory factoryAnnotation) {
        this.klazz = klazz;
        this.method = method;
        this.factoryAnnotation = factoryAnnotation;
    }

    public UserContextScope scope() {
        return factoryAnnotation.scope();
    }

    public Class<?> getOwningType() {
        return klazz;
    }

    public Method getMethod() {
        return method;
    }
}
