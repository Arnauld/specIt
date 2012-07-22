package specit.usecase.calculator.annotationcontext;

import specit.SpecIt;
import specit.element.Keyword;
import specit.invocation.ParameterMappingException;
import specit.junit.SpecItRunner;
import specit.report.ConsoleColoredReporter;
import specit.support.ResourceStoryLoader;
import specit.util.IDE;

import org.junit.runner.RunWith;

/**
 *
 *
 */
@RunWith(SpecItRunner.class)
public class CalculatorJunitRunnerTest {

    @SpecItRunner.Parameters
    public static SpecIt conf() throws ParameterMappingException {
        return new SpecIt()
                .withAlias(Keyword.Scenario, "Scenario:")
                .withAlias(Keyword.Given, "Given")
                .withAlias(Keyword.When, "When")
                .withAlias(Keyword.Then, "Then")
                .withAlias(Keyword.Fragment, "Fragment:")
                .withAlias(Keyword.Repeat, "Repeat")
                .withReporter(new ConsoleColoredReporter(!IDE.isExecutedWithinIDE()))
                .scanAnnotations(CalculatorSteps.class)
                .withStoryLoader(new ResourceStoryLoader("/specit/usecase/calculator"))
                .withStoryPaths(
                        "calculator_simple.story",
                        "calculator_repeat.story",
                        "calculator_multiplescenario.story");
    }
}
