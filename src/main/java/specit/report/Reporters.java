package specit.report;

import specit.element.InvocationContextListener;
import specit.element.InvokableStep;
import specit.invocation.CandidateStep;
import specit.invocation.Lifecycle;

import java.util.List;

/**
 *
 *
 */
public final class Reporters {

    private Reporters() {
    }

    public static InvocationContextListener asInvocationContextListener(final Reporter reporter) {
        return new InvocationContextListenerAdapter(reporter);
    }

    private static class InvocationContextListenerAdapter implements InvocationContextListener {

        private final Reporter reporter;

        public InvocationContextListenerAdapter(Reporter reporter) {
            this.reporter = reporter;
        }

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
        public void stepInvocationFailed(InvokableStep invokableStep, CandidateStep candidateStep, String message, Exception cause) {
            reporter.stepInvocationFailed(invokableStep, candidateStep, message, cause);
        }

        @Override
        public void stepInvocationFailed(InvokableStep invokableStep, List<CandidateStep> candidateSteps, String message) {
            reporter.stepInvocationFailed(invokableStep, candidateSteps, message);
        }

        @Override
        public void stepSkipped(InvokableStep invokableStep, CandidateStep candidateStep) {
            reporter.stepSkipped(invokableStep, candidateStep);
        }

        @Override
        public void stepInvoked(InvokableStep invokableStep, CandidateStep candidateStep) {
            reporter.stepInvoked(invokableStep, candidateStep);
        }
    }
}
