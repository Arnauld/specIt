package specit.junit;

import specit.util.New;

import org.junit.runner.Description;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class RootSuite extends SpecItSuite {

    private final SpecItRunner runner;
    private final Class<?> testClass;

    public RootSuite(SpecItRunner runner, Class<?> testClass) {
        this.runner = runner;
        this.testClass = testClass;
    }

    @Override
    protected Description createSuiteDescription() {
        return Description.createSuiteDescription(testClass);
    }

    @Override
    protected List<SpecItSuite> createSuites() throws Throwable {
        List<SpecItSuite> suites = New.arrayList();
        suites.addAll(buildBeforeStoriesSuites());
        suites.addAll(buildStoriesSuites());
        suites.addAll(buildAfterStoriesSuites());
        return suites;
    }

    private List<SpecItSuite> buildBeforeStoriesSuites() throws Throwable {
        // TODO
        return Collections.emptyList();
    }

    private List<SpecItSuite> buildAfterStoriesSuites() throws Throwable {
        // TODO
        return Collections.emptyList();
    }

    private List<StorySuite> buildStoriesSuites() throws Throwable {
        List<StorySuite> suites = New.arrayList();
        for (String storyPath : runner.getSpecIt().storyPaths()) {
            StorySuite storySuite = buildStorySuite(storyPath);
            suites.add(storySuite);
        }
        return suites;
    }

    private StorySuite buildStorySuite(String storyPath) {
        return new StorySuite(runner, storyPath);
    }
}
