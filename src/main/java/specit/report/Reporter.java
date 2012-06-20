package specit.report;

import specit.element.Background;
import specit.element.Keyword;
import specit.element.Scenario;
import specit.element.Story;
import specit.invocation.CandidateStep;
import specit.invocation.InvocationException;
import specit.invocation.Lifecycle;

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

    void stepInvoked(String keywordAlias, String stepInput, CandidateStep candidateStep);
    void stepSkipped(String keywordAlias, String stepInput, CandidateStep candidateStep);
    void stepInvocationFailed(String keywordAlias, String stepInput, CandidateStep candidateStep, String message, Exception cause);
    void stepInvocationFailed(String keywordAlias, String stepInput, List<CandidateStep> candidateSteps, String message);

    void endScenario(Scenario scenario);
    void endStory(Story story);
}
