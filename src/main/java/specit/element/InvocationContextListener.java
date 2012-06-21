package specit.element;

import specit.invocation.CandidateStep;
import specit.invocation.Lifecycle;

import java.util.List;

/**
 */
public interface InvocationContextListener {
    void lifecycleInvoked(Lifecycle lifecycle);

    void lifecycleInvocationFailed(Lifecycle lifecycle, String message);

    void lifecycleInvocationFailed(Lifecycle lifecycle, String message, Exception cause);

    void lifecycleSkipped(Lifecycle lifecycle);

    void stepInvocationFailed(InvokableStep invokableStep, CandidateStep candidateStep, String message, Exception cause);

    void stepInvocationFailed(InvokableStep invokableStep, List<CandidateStep> candidateSteps, String message);

    void stepSkipped(InvokableStep invokableStep, CandidateStep candidateStep);

    void stepInvoked(InvokableStep invokableStep, CandidateStep candidateStep);
}
