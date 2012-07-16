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
public class StatsReporter implements Reporter {

    private int lifecycleInvoked;
    private int lifecycleSkipped;
    private int lifecycleFailures = 0;
    private int startStory;
    private int endStory;
    private int startBackground;
    private int endBackground;
    private int startScenario;
    private int endScenario;
    private int stepInvoked;
    private int stepSkipped;
    private int stepFailures;
    private int userContextDefined;
    private int userContextDiscarded;

    public int getLifecycleInvoked() {
        return lifecycleInvoked;
    }

    public int getLifecycleSkipped() {
        return lifecycleSkipped;
    }

    public int getLifecycleFailures() {
        return lifecycleFailures;
    }

    public int getStartStory() {
        return startStory;
    }

    public int getEndStory() {
        return endStory;
    }

    public int getStartBackground() {
        return startBackground;
    }

    public int getEndBackground() {
        return endBackground;
    }

    public int getStartScenario() {
        return startScenario;
    }

    public int getEndScenario() {
        return endScenario;
    }

    public int getStepInvoked() {
        return stepInvoked;
    }

    public int getStepSkipped() {
        return stepSkipped;
    }

    public int getStepFailures() {
        return stepFailures;
    }

    public int getUserContextDefined() {
        return userContextDefined;
    }

    public int getUserContextDiscarded() {
        return userContextDiscarded;
    }

    public int failureCount () {
        return lifecycleFailures + stepFailures;
    }

    @Override
    public void lifecycleInvoked(Lifecycle lifecycle) {
        lifecycleInvoked++;
    }

    @Override
    public void lifecycleSkipped(Lifecycle lifecycle) {
        lifecycleSkipped++;
    }

    @Override
    public void lifecycleInvocationFailed(Lifecycle lifecycle, String message) {
        lifecycleFailures++;
    }

    @Override
    public void lifecycleInvocationFailed(Lifecycle lifecycle, String message, Exception cause) {
        lifecycleFailures++;
    }

    @Override
    public void startStory(Story story) {
        startStory++;
    }

    @Override
    public void startBackground(Background background) {
        startBackground++;
    }

    @Override
    public void endBackground(Background background) {
        endBackground++;
    }

    @Override
    public void startScenario(Scenario scenario) {
        startScenario++;
    }

    @Override
    public void stepInvoked(InvokableStep invokableStep, CandidateStep candidateStep) {
        stepInvoked++;
    }

    @Override
    public void stepSkipped(InvokableStep invokableStep, CandidateStep candidateStep) {
        stepSkipped++;
    }

    @Override
    public void stepInvocationFailed(InvokableStep invokableStep,
                                     CandidateStep candidateStep,
                                     String message,
                                     Exception cause)
    {
        stepFailures++;
    }

    @Override
    public void stepInvocationFailed(InvokableStep invokableStep, List<CandidateStep> candidateSteps, String message) {
        stepFailures++;
    }

    @Override
    public void endScenario(Scenario scenario) {
        endScenario++;
    }

    @Override
    public void endStory(Story story) {
        endStory++;
    }

    @Override
    public void userContextDefined(UserContextSupport userContextSupport) {
        userContextDefined++;
    }

    @Override
    public void userContextDiscarded(UserContextSupport userContextSupport) {
        userContextDiscarded++;
    }
}
