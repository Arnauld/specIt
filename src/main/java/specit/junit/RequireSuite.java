package specit.junit;

import org.junit.runner.Description;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class RequireSuite extends SpecItSuite {
    private final SpecItRunner runner;
    private final String resolved;

    public RequireSuite(SpecItRunner runner, String resolved) {
        this.runner = runner;
        this.resolved = resolved;
    }

    @Override
    protected Description createSuiteDescription() {
        return Description.createSuiteDescription(
                runner.ensureUniqueness("Require " + resolved));
    }

    @Override
    protected List<SpecItSuite> createSuites() throws Throwable {
        return Arrays.<SpecItSuite>asList(new StorySuite(runner, resolved));
    }
}
