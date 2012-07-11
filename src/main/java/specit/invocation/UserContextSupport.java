package specit.invocation;

import specit.annotation.UserContext;

/**
 *
 */
public class UserContextSupport {
    private final Object userContext;
    private final UserContextFactorySupport factory;

    public UserContextSupport(Object userContext, UserContextFactorySupport factory) {
        this.userContext = userContext;
        this.factory = factory;
    }


    public UserContext.Scope scope() {
        return factory.scope();
    }

    public Object getUserContext() {
        return userContext;
    }

    public boolean matches(Class<?> userContextType, UserContext userContextAnnotation) {
        return userContextType.isInstance(userContext);
    }
}
