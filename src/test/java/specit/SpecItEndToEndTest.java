package specit;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import specit.annotation.Given;
import specit.annotation.Then;
import specit.annotation.Variable;
import specit.annotation.When;
import specit.element.Keyword;
import specit.invocation.ParameterMappingException;
import specit.report.ConsoleColoredReporter;
import specit.report.StatsReporter;
import specit.support.ResourceStoryLoader;
import specit.util.IDE;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.util.Locale;

/**
 *
 */
public class SpecItEndToEndTest {

    public static ThreadLocal<ExampleSteps> delegate = new ThreadLocal<ExampleSteps>();
    //
    private SpecIt specIt;
    private StatsReporter statsReporter;
    private ExampleSteps exampleSteps;

    @Before
    public void setUp () throws ParameterMappingException {
        statsReporter = new StatsReporter();
        specIt = new SpecIt()
                .initAliasesWithDefault(Locale.ENGLISH)
                .withReporters(
                        new ConsoleColoredReporter(!IDE.isExecutedWithinIDE()),
                        statsReporter)
                .scanAnnotations(ExampleSteps.class)
                .withStoryLoader(new ResourceStoryLoader("/specit/endtoend"));
        exampleSteps = Mockito.mock(ExampleSteps.class);
        delegate.set(exampleSteps);
    }

    @Test
    public void one_scenario() {
        specIt.withStoryPaths("one_scenario.story");
        specIt.executeStories();

        verify(exampleSteps).defineVariable("x", 2);
        verify(exampleSteps).addValueToVariable("x", 3);
        verify(exampleSteps).assertVariableIsEqualTo("x", 5);
        verifyNoMoreInteractions(exampleSteps);
    }

    @Test
    public void two_scenarios() {
        specIt.withStoryPaths("two_scenarios.story");
        specIt.executeStories();

        verify(exampleSteps).defineVariable("x", 2);
        verify(exampleSteps).addValueToVariable("x", 3);
        verify(exampleSteps).assertVariableIsEqualTo("x", 5);

        verify(exampleSteps).defineVariable("x", 3);
        verify(exampleSteps).addValueToVariable("x", 5);
        verify(exampleSteps).assertVariableIsEqualTo("x", 8);
        verifyNoMoreInteractions(exampleSteps);
    }

    @Test
    public void two_scenarios_first_fail_second_is_executed_anyway() {
        doThrow(new RuntimeException("Erf! Cannot add 3 to x")).when(exampleSteps).addValueToVariable("x", 3);

        specIt.withStoryPaths("two_scenarios.story");
        specIt.executeStories();

        verify(exampleSteps).defineVariable("x", 2);
        verify(exampleSteps).addValueToVariable("x", 3);
        // call skipped du to previous exception
        // verify(exampleSteps).assertVariableIsEqualTo("x", 5);

        verify(exampleSteps).defineVariable("x", 3);
        verify(exampleSteps).addValueToVariable("x", 5);
        verify(exampleSteps).assertVariableIsEqualTo("x", 8);
        verifyNoMoreInteractions(exampleSteps);
    }

    public static class ExampleSteps {
        @Given("a variable $varName with value $varValue")
        public void defineVariable(String variableName, int value) {
            delegate.get().defineVariable(variableName, value);
        }

        @When("I add $value to $varName")
        public void addValueToVariable(@Variable("varName") String variableName, @Variable("value") int value) {
            delegate.get().addValueToVariable(variableName, value);
        }

        @Then("$varName should equal to $value")
        public void assertVariableIsEqualTo(String variableName, int value) {
            delegate.get().assertVariableIsEqualTo(variableName, value);
        }
    }
}
