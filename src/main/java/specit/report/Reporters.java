package specit.report;

import specit.element.InvocationContextListener;
import specit.invocation.CandidateStep;
import specit.invocation.Lifecycle;

import java.util.List;

/**
 *
 *
 */
public class Reporters {

    public static InvocationContextListener asInvocationContextListener(final Reporter reporter) {
        return new InvocationContextListener() {
            @Override
            public void lifecycleInvoked(Lifecycle lifecycle) {
                reporter.lifecycleInvoked(lifecycle);
            }

            @Override
            public void lifecycleInvocationFailed(Lifecycle lifecycle, String message) {
                reporter.lifecycleInvocationFailed(lifecycle, message);
            }

            @Override
            public void lifecycleInvocationFailed(Lifecycle lifecycle, String message, Exception cause) {
                reporter.lifecycleInvocationFailed(lifecycle, message, cause);
            }

            @Override
            public void lifecycleSkipped(Lifecycle lifecycle) {
                reporter.lifecycleSkipped(lifecycle);
            }

            @Override
            public void stepInvocationFailed(String keywordAlias, String resolved, CandidateStep candidateStep, String message, Exception cause) {
                reporter.stepInvocationFailed(keywordAlias, resolved, candidateStep, message, cause);
            }

            @Override
            public void stepInvocationFailed(String keywordAlias, String resolved, List<CandidateStep> candidateSteps, String message) {
                reporter.stepInvocationFailed(keywordAlias, resolved, candidateSteps, message);
            }

            @Override
            public void stepSkipped(String keywordAlias, String input, CandidateStep candidateStep) {
                reporter.stepSkipped(keywordAlias, input, candidateStep);
            }

            @Override
            public void stepInvoked(String keywordAlias, String input, CandidateStep candidateStep) {
                reporter.stepInvoked(keywordAlias, input, candidateStep);
            }
        };
    }
}
