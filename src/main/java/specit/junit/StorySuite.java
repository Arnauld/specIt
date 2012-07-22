package specit.junit;

import specit.element.ExecutablePart;
import specit.element.InvokableStep;
import specit.element.Story;
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
public class StorySuite extends SpecItSuite {

    private final SpecItRunner runner;
    private final String storyPath;
    private final Story story;

    public StorySuite(SpecItRunner runner, String storyPath) {
        this.runner = runner;
        this.storyPath = storyPath;
        this.story = runner.getSpecIt().loadStory(storyPath);
        story.traverse(new specit.element.DumpVisitor());
    }

    @Override
    public void run(RunNotifier notifier) {
        InterpreterListener listener = runner.getSpecIt().invokerInterpreterListener(story);

        Description suiteDescription = null;
        try {
            suiteDescription = getSuiteDescription();
            notifier.fireTestStarted(suiteDescription);
            listener.beginStory(story);

            for (SpecItSuite suite : getSuites()) {
                ScenarioSuite scenarioSuite = (ScenarioSuite) suite;
                scenarioSuite.run(notifier, listener);
            }

            listener.endStory(story);
            notifier.fireTestFinished(suiteDescription);
        }
        catch (Throwable throwable) {
            notifier.fireTestFailure(new Failure(suiteDescription, throwable));
        }
    }

    @Override
    protected Description createSuiteDescription() {
        String displayName = storyPath;
        if (displayName.toLowerCase().endsWith(".story")) {
            displayName = displayName.substring(0, displayName.length() - 6);
        }
        return Description.createSuiteDescription(
                runner.ensureUniqueness(displayName));
    }

    @Override
    protected List<SpecItSuite> createSuites() throws Throwable {
        final List<SpecItSuite> storySuites = New.arrayList();

        runner.getSpecIt().interpretStory(story, new InterpreterListener() {
            private ScenarioSuite currentScenarioSuite;

            @Override
            public void beginScenario(ExecutablePart scenario, InterpreterContext context) {
                currentScenarioSuite = new ScenarioSuite(runner, scenario, context);
            }

            @Override
            public void endScenario(ExecutablePart scenario, InterpreterContext context) {
                storySuites.add(currentScenarioSuite);
                currentScenarioSuite = null;
            }

            @Override
            public void invokeStep(InvokableStep invokableStep, InterpreterContext context) {
                StepCase stepCase = new StepCase(runner, invokableStep, context);
                currentScenarioSuite.add(stepCase);
            }

            @Override
            public void invokeRequire(String resolved, InterpreterContext context) {
                RequireSuite requireSuite = new RequireSuite(runner, resolved);
                currentScenarioSuite.add(requireSuite);
            }
        });
        return storySuites;
    }
}