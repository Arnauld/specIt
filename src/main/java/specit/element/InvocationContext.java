package specit.element;

import specit.annotation.UserContext;
import specit.invocation.CandidateStep;
import specit.invocation.InvocationException;
import specit.invocation.Lifecycle;
import specit.invocation.UserContextSupport;
import specit.util.New;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 *
 */
public class InvocationContext {
    private final Map<Object, Object> values = New.hashMap();
    private final InvocationContext parent;
    private final InvocationContextListener listener;
    private final List<UserContextSupport> userContexts = New.arrayList();
    private final Story currentStory;
    private Scenario currentScenario;
    private boolean lifecycleInError;
    private boolean stepInError;

    public InvocationContext(InvocationContext parent, Story currentStory, InvocationContextListener listener) {
        this.parent = parent;
        this.currentStory = currentStory;
        this.listener = listener;
    }

    public boolean isNestedContext() {
        return (parent != null);
    }

    public Story getCurrentStory() {
        return currentStory;
    }

    public Scenario getCurrentScenario() {
        return currentScenario;
    }

    public <T> T get(Object key) {
        return (T) values.get(key);
    }

    public InvocationContext set(Object key, Object value) {
        values.put(key, value);
        return this;
    }

    public boolean isLifecycleInError() {
        return lifecycleInError;
    }

    public void lifecycleInvocationFailed(Lifecycle lifecycle, String message, Exception cause) {
        lifecycleInError = true;
        listener.lifecycleInvocationFailed(lifecycle, message, cause);
    }

    public boolean canInvokeLifecycle(Lifecycle lifecycle) {
        return !isLifecycleInError() && !isStepInError();
    }

    public void lifecycleSkipped(Lifecycle lifecycle) {
        listener.lifecycleSkipped(lifecycle);
    }

    public boolean isStepInError() {
        return stepInError;
    }

    public void stepInvocationFailed(InvokableStep invokableStep,
                                     CandidateStep candidateStep,
                                     String message,
                                     Exception cause)
    {
        stepInError = true;
        listener.stepInvocationFailed(invokableStep, candidateStep, message, cause);
    }

    public void stepInvocationFailed(InvokableStep invokableStep, List<CandidateStep> candidateSteps, String message) {
        stepInError = true;
        listener.stepInvocationFailed(invokableStep, candidateSteps, message);
    }

    public boolean canInvokeStep(InvokableStep invokableStep, CandidateStep candidateStep) {
        return !lifecycleInError && !stepInError;
    }

    public void stepSkipped(InvokableStep invokableStep, CandidateStep candidateStep) {
        listener.stepSkipped(invokableStep, candidateStep);
    }

    public void lifecycleInvoked(Lifecycle lifecycle) {
        listener.lifecycleInvoked(lifecycle);
    }

    public void stepInvoked(InvokableStep invokableStep, CandidateStep candidateStep) {
        listener.stepInvoked(invokableStep, candidateStep);
    }

    public void defineUserContext(UserContextSupport userContextSupport) {
        listener.userContextDefined(userContextSupport);
        userContexts.add(userContextSupport);
    }

    public Object lookupUserContextOrFail(Class<?> userContextType, UserContext userContextAnnotation) {
        for (UserContextSupport userContext : userContexts) {
            if (userContext.matches(userContextType, userContextAnnotation)) {
                return userContext.getUserContext();
            }
        }
        throw new InvocationException("No User Context matching {"
                + userContextType
                + ", @" + userContextAnnotation
                + "}");
    }

    public void discardUserContexts(UserContext.Scope scope) {
        Iterator<UserContextSupport> userContextIt = userContexts.iterator();
        while (userContextIt.hasNext()) {
            UserContextSupport userContext = userContextIt.next();
            if (userContext.scope() == scope) {
                listener.userContextDiscarded(userContext);
                userContextIt.remove();
            }
        }
    }

    public void beginScenarioOrBackground(ExecutablePart scenarioOrBackground) {
        if (scenarioOrBackground instanceof Scenario) {
            this.currentScenario = (Scenario) scenarioOrBackground;
        }
    }

    public void endScenarioOrBackground(ExecutablePart scenarioOrBackground) {
        if (scenarioOrBackground instanceof Scenario) {
            this.currentScenario = null;
        }
    }

}
