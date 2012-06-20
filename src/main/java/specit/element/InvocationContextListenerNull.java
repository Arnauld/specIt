package specit.element;

import specit.invocation.CandidateStep;
import specit.invocation.Lifecycle;

import java.util.List;

/**
 *
 */
public class InvocationContextListenerNull implements InvocationContextListener {

    @Override
    public void lifecycleInvoked(Lifecycle lifecycle) {

    }

    @Override
    public void lifecycleInvocationFailed(Lifecycle lifecycle, String message) {

    }

    @Override
    public void lifecycleInvocationFailed(Lifecycle lifecycle, String message, Exception cause) {

    }

    @Override
    public void lifecycleSkipped(Lifecycle lifecycle) {

    }

    @Override
    public void stepInvocationFailed(String keywordAlias, String resolved, CandidateStep candidateStep, String message, Exception cause) {

    }

    @Override
    public void stepInvocationFailed(String keywordAlias, String resolved, List<CandidateStep> candidateSteps, String message) {

    }

    @Override
    public void stepSkipped(String keywordAlias, String input, CandidateStep candidateStep) {

    }

    @Override
    public void stepInvoked(String keywordAlias, String input, CandidateStep candidateStep) {

    }
}
