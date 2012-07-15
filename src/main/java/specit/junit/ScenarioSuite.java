package specit.junit;

import specit.element.ExecutablePart;
import specit.interpreter.InterpreterContext;
import specit.interpreter.InterpreterListener;
import specit.util.New;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import java.util.List;

/**
 *
 *
 */
public class ScenarioSuite extends SpecItSuite {

    private final SpecItRunner runner;
    private final ExecutablePart scenarioOrBackground;
    private final InterpreterContext context;
    private final List<SpecItSuite> suites = New.arrayList();

    public ScenarioSuite(SpecItRunner runner, ExecutablePart scenarioOrBackground, InterpreterContext context) {
        this.runner = runner;
        this.scenarioOrBackground = scenarioOrBackground;
        this.context = context;
    }

    public void add(SpecItSuite nestedSuite) {
        suites.add(nestedSuite);
    }

    @Override
    protected Description createSuiteDescription() {
        return Description.createSuiteDescription(
                runner.ensureUniqueness(scenarioOrBackground.getTitle().trim()));
    }

    @Override
    protected List<SpecItSuite> createSuites() throws Throwable {
        return suites;
    }

    public void run(RunNotifier notifier, InterpreterListener listener) {
        Description suiteDescription = null;
        try {
            suiteDescription = getSuiteDescription();
            notifier.fireTestStarted(suiteDescription);
            listener.beginScenario(scenarioOrBackground, context);

            for (SpecItSuite suite : suites) {
                ((StepCase) suite).run(notifier, listener);
            }

            listener.endScenario(scenarioOrBackground, context);
            notifier.fireTestFinished(suiteDescription);
        }
        catch (Throwable throwable) {
            notifier.fireTestFailure(new Failure(suiteDescription, throwable));
        }
    }
}
