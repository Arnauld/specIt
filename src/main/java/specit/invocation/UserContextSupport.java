package specit.invocation;

import specit.annotation.UserContext;

/**
 *
 */
public class UserContextSupport {
    private final Object userContext;
    private final UserContext.Scope scope;
    private final String name;

    public UserContextSupport(UserContext.Scope scope, String name, Object userContext) {
        this.scope = scope;
        this.name = name;
        this.userContext = userContext;
    }

    public UserContext.Scope scope() {
        return scope;
    }

    public Object getUserContext() {
        return userContext;
    }

    public boolean matches(Class<?> userContextType, UserContext userContextAnnotation) {
        return userContextType.isInstance(userContext);
    }
}
