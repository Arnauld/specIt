package specit.junit;

import specit.element.InvokableStep;
import specit.interpreter.InterpreterContext;
import specit.interpreter.InterpreterListener;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import java.util.Collections;
import java.util.List;

/**
 *
 *
 */
public class StepCase extends SpecItSuite {

    private final SpecItRunner runner;
    private final InvokableStep invokableStep;
    private final InterpreterContext context;

    public StepCase(SpecItRunner runner, InvokableStep invokableStep, InterpreterContext context) {
        this.runner = runner;
        this.invokableStep = invokableStep;
        this.context = context;
    }

    @Override
    protected Description createSuiteDescription() {
        return Description.createSuiteDescription(
                runner.ensureUniqueness(invokableStep.getAdjustedInput().trim()));
    }

    @Override
    protected List<SpecItSuite> createSuites() throws Throwable {
        return Collections.emptyList();
    }

    public void run(RunNotifier notifier, InterpreterListener listener) throws Throwable {
        notifier.fireTestStarted(getSuiteDescription());
        try {
            listener.invokeStep(invokableStep, context);
            notifier.fireTestFinished(getSuiteDescription());
        }
        catch (Throwable thr) {
            notifier.fireTestFailure(new Failure(getSuiteDescription(), thr));
        }
    }
}