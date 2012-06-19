package specit.element;

import specit.invocation.CandidateStep;
import specit.invocation.InvocationException;
import specit.invocation.Lifecycle;
import specit.util.New;

import java.util.List;
import java.util.Map;

/**
 *
 *
 */
public class InvocationContext {
    private final Map<Object,Object> values = New.hashMap();
    private final InvocationContext parent;
    private final Story currentStory;
    private boolean lifecycleInError;
    private boolean stepInError;

    public InvocationContext(Story currentStory) {
        this(null, currentStory);
    }

    public InvocationContext(InvocationContext parent, Story currentStory) {
        this.parent = parent;
        this.currentStory = currentStory;
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
        throw new InvocationException(message);
    }

    public void lifecycleInvocationFailed(Lifecycle lifecycle, String message, Exception cause) {
        lifecycleInError = true;
        throw new InvocationException(message, cause);
    }

    public boolean canInvokeLifecycle(Lifecycle lifecycle) {
        return !lifecycleInError && !stepInError;
    }

    public void stepInvocationFailed(String input, CandidateStep candidateStep, String message, Exception cause) {
        stepInError = true;
    }

    public void stepInvocationFailed(String resolved, List<CandidateStep> candidateSteps, String message) {
        stepInError = true;
    }

    public boolean canInvokeStep(String input, CandidateStep candidateStep) {
        return !lifecycleInError && !stepInError;
    }

}
