package specit.invocation;

import specit.annotation.UserContext;

import java.lang.reflect.Method;

/**
 */
public class UserContextFactorySupport {
    private final Class<?> klazz;
    private final Method method;
    private final UserContext.Factory factoryAnnotation;

    public UserContextFactorySupport(Class<?> klazz, Method method, UserContext.Factory factoryAnnotation) {
        this.klazz = klazz;
        this.method = method;
        this.factoryAnnotation = factoryAnnotation;
    }

    public UserContext.Scope scope() {
        return factoryAnnotation.scope();
    }

    public Class<?> getOwningType() {
        return klazz;
    }

    public Method getMethod() {
        return method;
    }
}
