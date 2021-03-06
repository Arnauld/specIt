package specit.element;

import static java.util.Arrays.asList;

import specit.annotation.UserContext;
import specit.annotation.lifecycle.AfterScenario;
import specit.annotation.lifecycle.AfterStory;
import specit.annotation.lifecycle.BeforeScenario;
import specit.annotation.lifecycle.BeforeStory;
import specit.invocation.CandidateStep;
import specit.invocation.InvocationException;
import specit.invocation.Lifecycle;
import specit.invocation.UserContextSupport;
import specit.util.New;

import java.util.Arrays;
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
    // ~
    private boolean scenarioLifecycleInError;
    private boolean storyLifecycleInError;
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
        return scenarioLifecycleInError || storyLifecycleInError;
    }

    public void lifecycleInvocationFailed(Lifecycle lifecycle, String message, Exception cause) {
        if(isScenarioLifecycle(lifecycle)) {
            scenarioLifecycleInError = true;
        }
        else if(isStoryLifecycle(lifecycle)) {
            storyLifecycleInError = true;
        }
        listener.lifecycleInvocationFailed(lifecycle, message, cause);
    }

    private boolean isStoryLifecycle(Lifecycle lifecycle) {
        return asList(BeforeStory.class, AfterStory.class).contains(lifecycle.getLifecycleType());
    }

    private boolean isScenarioLifecycle(Lifecycle lifecycle) {
        return asList(BeforeScenario.class, AfterScenario.class).contains(lifecycle.getLifecycleType());
    }

    public boolean canInvokeLifecycle(Lifecycle lifecycle) {
        // even if a lifecycle is in error
        // all other should still be invoked see junit's tearDown
        return true;
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
        return !isLifecycleInError() && !isStepInError();
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

    public void defineUserContext(UserContext.Scope scope, String name, Object data) {
        UserContextSupport userContextSupport = new UserContextSupport(scope, name, data);
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

            // reset stepError flag: one starts a new sequence of steps
            this.stepInError = false;
            this.scenarioLifecycleInError = false;
        }
    }

    public void endScenarioOrBackground(ExecutablePart scenarioOrBackground) {
        if (scenarioOrBackground instanceof Scenario) {
            this.currentScenario = null;
        }
    }

}
