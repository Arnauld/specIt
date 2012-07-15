package specit.junit;

import specit.SpecItRuntimeException;

import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import java.util.List;

/**
 *
 *
 */
public abstract class SpecItSuite {

    private List<SpecItSuite> suites;

    private Description description;

    protected abstract Description createSuiteDescription();

    protected abstract List<SpecItSuite> createSuites() throws Throwable;

    public List<SpecItSuite> getSuites() {
        try {
            if (suites == null) {
                suites = createSuites();
            }
            return suites;
        }
        catch (Throwable throwable) {
            throw new SpecItRuntimeException("Error during TestSuite creation", throwable);
        }
    }

    public void run(RunNotifier notifier) {
        for (SpecItSuite suite : getSuites()) {
            suite.run(notifier);
        }
    }

    public Description getSuiteDescription() throws Throwable {
        if (description == null) {
            description = createSuiteDescription();
            for (SpecItSuite suite : getSuites()) {
                description.getChildren().add(suite.getSuiteDescription());
            }
        }
        return description;
    }

    public int testCount() {
        int count = 0;
        for (SpecItSuite suite : getSuites()) {
            count += suite.testCount();
        }
        return count;
    }
}
