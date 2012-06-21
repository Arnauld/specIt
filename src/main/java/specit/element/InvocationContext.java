package specit.element;

import specit.invocation.CandidateStep;
import specit.invocation.Lifecycle;
import specit.util.New;
import specit.util.Proxies;

import java.util.List;
import java.util.Map;

/**
 *
 *
 */
public class InvocationContext {
    private final Map<Object,Object> values = New.hashMap();
    private final InvocationContext parent;
    private final InvocationContextListener listener;
    private final Story currentStory;
    private boolean lifecycleInError;
    private boolean stepInError;

    public InvocationContext(Story currentStory) {
        this(null, currentStory);
    }

    public InvocationContext(InvocationContext parent, Story currentStory) {
        this(parent, currentStory, Proxies.proxyNoOp(InvocationContextListener.class));
    }

    public InvocationContext(InvocationContext parent, Story currentStory, InvocationContextListener listener) {
        this.parent = parent;
        this.currentStory = currentStory;
        this.listener = listener;
    }

    public boolean isNestedContext() {
        return (parent!=null);
    }

    public InvocationContext getParentContext() {
        return parent;
    }

    public Story getCurrentStory() {
        return currentStory;
    }

    public <T> T get(Object key) {
        return (T)values.get(key);
    }

    public InvocationContext set(Object key, Object value) {
        values.put(key, value);
        return this;
    }

    public boolean isLifecycleInError() {
        return lifecycleInError;
    }

    public void lifecycleInvocationFailed(Lifecycle lifecycle, String message) {
        lifecycleInError = true;
        listener.lifecycleInvocationFailed(lifecycle, message);
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

    public void stepInvocationFailed(InvokableStep invokableStep, CandidateStep candidateStep, String message, Exception cause) {
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
}
