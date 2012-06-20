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

    void stepInvocationFailed(String keywordAlias, String resolved, CandidateStep candidateStep, String message, Exception cause);

    void stepInvocationFailed(String keywordAlias, String resolved, List<CandidateStep> candidateSteps, String message);

    void stepSkipped(String keywordAlias, String input, CandidateStep candidateStep);

    void stepInvoked(String keywordAlias, String input, CandidateStep candidateStep);
}
