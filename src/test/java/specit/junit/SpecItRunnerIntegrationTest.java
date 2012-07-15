package specit.junit;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import specit.SpecIt;
import specit.annotation.Given;
import specit.annotation.Then;
import specit.annotation.Variable;
import specit.annotation.When;
import specit.element.Keyword;
import specit.invocation.ParameterMappingException;
import specit.report.ConsoleColoredReporter;
import specit.support.ResourceStoryLoader;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import java.util.Iterator;

/**
 *
 *
 */
public class SpecItRunnerIntegrationTest {


    public static ThreadLocal<ExampleSteps> delegate = new ThreadLocal<ExampleSteps>();
    public static ThreadLocal<String> storyToExecute = new ThreadLocal<String>();

    @Test
    public void exampleSteps_case1_analyseDescriptionTree() throws Throwable {
        storyToExecute.set("example-it-01.story");
        ExampleSteps steps = mock(ExampleSteps.class);
        delegate.set(steps);

        SpecItRunner runner = new SpecItRunner(Example.class);

        Description rootDescription = runner.getDescription();
        assertThat(rootDescription.getDisplayName()).isEqualTo("specit.junit.SpecItRunnerIntegrationTest$Example");

        assertThat(rootDescription.getChildren()).hasSize(1);
        Description storyDescription = rootDescription.getChildren().get(0);
        assertThat(storyDescription.getDisplayName()).isEqualTo("example-it-01");

        assertThat(storyDescription.getChildren()).hasSize(1);
        Description scenarioDescription = storyDescription.getChildren().get(0);
        assertThat(scenarioDescription.getDisplayName()).isEqualTo("2+3");

        assertThat(scenarioDescription.getChildren()).hasSize(3);
        Iterator<Description> stepsDescription = scenarioDescription.getChildren().iterator();
        assertThat(stepsDescription.next().getDisplayName()).isEqualTo("a variable x with value 2");
        assertThat(stepsDescription.next().getDisplayName()).isEqualTo("I add 3 to x");
        assertThat(stepsDescription.next().getDisplayName()).isEqualTo("x should equal to 5");

        verifyNoMoreInteractions(steps);
    }

    @Test
    public void exampleSteps_case2_analyseDescriptionTree() throws Throwable {
        storyToExecute.set("example-it-02.story");
        ExampleSteps steps = mock(ExampleSteps.class);
        delegate.set(steps);

        SpecItRunner runner = new SpecItRunner(Example.class);

        Description rootDescription = runner.getDescription();
        assertThat(rootDescription.getDisplayName()).isEqualTo("specit.junit.SpecItRunnerIntegrationTest$Example");

        assertThat(rootDescription.getChildren()).hasSize(1);
        Description storyDescription = rootDescription.getChildren().get(0);
        assertThat(storyDescription.getDisplayName()).isEqualTo("example-it-02");

        assertThat(storyDescription.getChildren()).hasSize(1);
        Description scenarioDescription = storyDescription.getChildren().get(0);
        assertThat(scenarioDescription.getDisplayName()).isEqualTo("Additions using repeat and table");

        assertThat(scenarioDescription.getChildren()).hasSize(5);
        Iterator<Description> stepsDescription = scenarioDescription.getChildren().iterator();
        assertThat(stepsDescription.next().getDisplayName()).isEqualTo("a variable x with value 2");
        assertThat(stepsDescription.next().getDisplayName()).isEqualTo("I add 3 to x");
        assertThat(stepsDescription.next().getDisplayName()).isEqualTo("I add 5 to x");
        assertThat(stepsDescription.next().getDisplayName()).isEqualTo("I add 7 to x");
        assertThat(stepsDescription.next().getDisplayName()).isEqualTo("x should equal to 17");

        verifyNoMoreInteractions(steps);
    }


    @Test
    public void exampleSteps_case1_ensureStepsAreCalled() {
        storyToExecute.set("example-it-01.story");
        ExampleSteps steps = mock(ExampleSteps.class);
        delegate.set(steps);

        JUnitCore core = new JUnitCore();
        core.run(Example.class);

        verify(steps).defineVariable("x", 2);
        verify(steps).addValueToVariable("x", 3);
        verify(steps).assertVariableIsEqualTo("x", 5);
        verifyNoMoreInteractions(steps);
    }

    @Test
    public void exampleSteps_case2_ensureStepsAreCalled() {
        storyToExecute.set("example-it-02.story");
        ExampleSteps steps = mock(ExampleSteps.class);
        delegate.set(steps);

        JUnitCore core = new JUnitCore();
        core.run(Example.class);

        verify(steps).defineVariable("x", 2);
        verify(steps).addValueToVariable("x", 3);
        verify(steps).addValueToVariable("x", 5);
        verify(steps).addValueToVariable("x", 7);
        verify(steps).assertVariableIsEqualTo("x", 17);
        verifyNoMoreInteractions(steps);
    }

    @RunWith(SpecItRunner.class)
    public static class Example {
        @SpecItRunner.Parameters
        public static SpecIt specIt() throws ParameterMappingException {
            return new SpecIt()
                    .withAlias(Keyword.Scenario, "Scenario:")
                    .withAlias(Keyword.Given, "Given")
                    .withAlias(Keyword.When, "When")
                    .withAlias(Keyword.Then, "Then")
                    .withAlias(Keyword.Fragment, "Fragment:")
                    .withAlias(Keyword.Repeat, "Repeat")
                    .withReporter(new ConsoleColoredReporter())
                    .scanAnnotations(ExampleSteps.class)
                    .withStoryLoader(new ResourceStoryLoader("/specit/junit"))
                    .withStoryPaths(storyToExecute.get());
        }
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
