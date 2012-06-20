package specit.report;

import specit.element.Background;
import specit.element.Scenario;
import specit.element.Story;
import specit.invocation.CandidateStep;
import specit.invocation.Lifecycle;

import java.util.List;

/**
 * 
 * 
 */
public class ReporterNull implements Reporter {
    @Override
    public void lifecycleInvoked(Lifecycle lifecycle) {
        
    }

    @Override
    public void lifecycleSkipped(Lifecycle lifecycle) {

    }

    @Override
    public void lifecycleInvocationFailed(Lifecycle lifecycle, String message) {

    }

    @Override
    public void lifecycleInvocationFailed(Lifecycle lifecycle, String message, Exception cause) {

    }

    @Override
    public void startStory(Story story) {
    }

    @Override
    public void startBackground(Background background) {

    }

    @Override
    public void endBackground(Background background) {

    }

    @Override
    public void startScenario(Scenario scenario) {

    }

    @Override
    public void stepInvoked(String keywordAlias, String stepInput, CandidateStep candidateStep) {

    }

    @Override
    public void stepSkipped(String keywordAlias, String stepInput, CandidateStep candidateStep) {

    }

    @Override
    public void stepInvocationFailed(String keywordAlias, String stepInput, CandidateStep candidateStep, String message, Exception cause) {

    }

    @Override
    public void stepInvocationFailed(String keywordAlias, String stepInput, List<CandidateStep> candidateSteps, String message) {

    }

    @Override
    public void endScenario(Scenario scenario) {

    }

    @Override
    public void endStory(Story story) {
    }
}
