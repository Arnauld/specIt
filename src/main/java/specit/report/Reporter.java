package specit.report;

import specit.element.Background;
import specit.element.InvokableStep;
import specit.element.Scenario;
import specit.element.Story;
import specit.invocation.CandidateStep;
import specit.invocation.Lifecycle;
import specit.invocation.UserContextSupport;

import java.util.List;

/**
 *
 */
public interface Reporter {

    void lifecycleInvoked(Lifecycle lifecycle);

    void lifecycleSkipped(Lifecycle lifecycle);

    void lifecycleInvocationFailed(Lifecycle lifecycle, String message);

    void lifecycleInvocationFailed(Lifecycle lifecycle, String message, Exception cause);

    void startStory(Story story);

    void startBackground(Background background);

    void endBackground(Background background);

    void startScenario(Scenario scenario);

    void stepInvoked(InvokableStep invokableStep, CandidateStep candidateStep);

    void stepSkipped(InvokableStep invokableStep, CandidateStep candidateStep);

    void stepInvocationFailed(InvokableStep invokableStep, CandidateStep candidateStep, String message, Exception cause);

    void stepInvocationFailed(InvokableStep invokableStep, List<CandidateStep> candidateSteps, String message);

    void endScenario(Scenario scenario);

    void endStory(Story story);

    void userContextDefined(UserContextSupport userContextSupport);

    void userContextDiscarded(UserContextSupport userContextSupport);
}
