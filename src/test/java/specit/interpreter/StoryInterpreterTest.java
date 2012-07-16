package specit.interpreter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;

import specit.SpecIt;
import specit.element.DumpVisitor;
import specit.element.Keyword;
import specit.element.RawElement;
import specit.element.Story;
import specit.element.StoryBuilder;
import specit.interpreter.InterpreterListenerRecorder.BeginScenario;
import specit.interpreter.InterpreterListenerRecorder.BeginStory;
import specit.interpreter.InterpreterListenerRecorder.EndScenario;
import specit.interpreter.InterpreterListenerRecorder.EndStory;
import specit.interpreter.InterpreterListenerRecorder.Event;
import specit.interpreter.InterpreterListenerRecorder.InvokeRequire;
import specit.interpreter.InterpreterListenerRecorder.InvokeStep;
import specit.parser.RawElementDefault;

import org.junit.Before;
import org.junit.Test;
import java.util.Iterator;

/**
 *
 *
 */
public class StoryInterpreterTest {

    private SpecIt specIt;

    @Before
    public void setUp() {
        specIt = new SpecIt();
    }

    @Test
    public void simpleCase() {
        // Given
        Story story = new StoryBuilder()
                .append(rawElement(Keyword.Narrative, "Narrative:\nAs a tester\nI want to test my builder\n\n"))
                        // scenario 1
                .append(rawElement(Keyword.Scenario, "Scenario: First scenario\n"))
                .append(rawElement(Keyword.Require, "Require: /story/scenario-env.story\n\n", "Require:"))
                .append(rawElement(Keyword.Given, "Given a initial step for the first scenario\n"))
                .append(rawElement(Keyword.When, "When a second step for the first scenario\n\n"))
                .getStory();

        InterpreterListenerRecorder recorder = new InterpreterListenerRecorder();

        // When
        new StoryInterpreter(specIt).interpretStory(story, recorder);

        // Then
        Iterator<Event> events = recorder.getEvents().iterator();
        assertThat(events.next(), instanceOf(BeginStory.class));
        assertThat(events.next(), instanceOf(BeginScenario.class));
        assertInvokeRequire(events.next(), "/story/scenario-env.story");
        assertInvokeStep(events.next(), Keyword.Given, "a initial step for the first scenario");
        assertInvokeStep(events.next(), Keyword.When, "a second step for the first scenario");
        assertThat(events.next(), instanceOf(EndScenario.class));
        assertThat(events.next(), instanceOf(EndStory.class));
    }

    @Test
    public void simpleCase_oneBackground() {
        // Given
        Story story = new StoryBuilder()
                .append(rawElement(Keyword.Narrative, "Narrative:\nAs a tester\nI want to test my builder\n\n"))
                        // background
                .append(rawElement(Keyword.Background, "Background:\n"))
                .append(rawElement(Keyword.Require, "Require: /story/scenario-shared.story\n\n", "Require:"))
                .append(rawElement(Keyword.Given, "Given steps for all scenario\n"))
                .append(rawElement(Keyword.Given, "Given other steps for all scenario\n\n"))
                        // scenario 1
                .append(rawElement(Keyword.Scenario, "Scenario: First scenario\n"))
                .append(rawElement(Keyword.Require, "Require: /story/scenario-env.story\n\n", "Require:"))
                .append(rawElement(Keyword.Given, "Given a initial step for the first scenario\n"))
                .append(rawElement(Keyword.When, "When a second step for the first scenario\n\n"))
                .getStory();

        InterpreterListenerRecorder recorder = new InterpreterListenerRecorder();

        // When
        new StoryInterpreter(specIt).interpretStory(story, recorder);

        // Then
        Iterator<Event> events = recorder.getEvents().iterator();
        assertThat(events.next(), instanceOf(BeginStory.class));
        assertThat(events.next(), instanceOf(BeginScenario.class));
        assertInvokeRequire(events.next(), "/story/scenario-shared.story");
        assertInvokeStep(events.next(), Keyword.Given, "steps for all scenario");
        assertInvokeStep(events.next(), Keyword.Given, "other steps for all scenario");
        assertInvokeRequire(events.next(), "/story/scenario-env.story");
        assertInvokeStep(events.next(), Keyword.Given, "a initial step for the first scenario");
        assertInvokeStep(events.next(), Keyword.When, "a second step for the first scenario");
        assertThat(events.next(), instanceOf(EndScenario.class));
        assertThat(events.next(), instanceOf(EndStory.class));
    }

    @Test
    public void simpleCase_oneForall() {
        // Given
        Story story = new StoryBuilder()
                .append(rawElement(Keyword.Narrative, "Narrative:\nAs a tester\nI want to test my builder\n\n"))
                        // background
                .append(rawElement(Keyword.Background, "Background:\n"))
                .append(rawElement(Keyword.Require, "Require: /story/scenario-shared.story\n\n", "Require:"))
                .append(rawElement(Keyword.Given, "Given steps for all scenario\n"))
                .append(rawElement(Keyword.Given, "Given other steps for all scenario\n\n"))
                        // scenario 1
                .append(rawElement(Keyword.Scenario, "Scenario: First scenario\n"))
                .append(rawElement(Keyword.Require, "Require: /story/scenario-env.story\n\n", "Require:"))
                .append(rawElement(Keyword.Given, "Given a initial step for the first scenario\n"))
                .append(rawElement(Keyword.When, "When a second step for the first scenario\n\n"))
                .getStory();

        InterpreterListenerRecorder recorder = new InterpreterListenerRecorder();

        // When
        new StoryInterpreter(specIt).interpretStory(story, recorder);

        // Then
        Iterator<Event> events = recorder.getEvents().iterator();
        assertThat(events.next(), instanceOf(BeginStory.class));
        assertThat(events.next(), instanceOf(BeginScenario.class));
        assertInvokeRequire(events.next(), "/story/scenario-shared.story");
        assertInvokeStep(events.next(), Keyword.Given, "steps for all scenario");
        assertInvokeStep(events.next(), Keyword.Given, "other steps for all scenario");
        assertInvokeRequire(events.next(), "/story/scenario-env.story");
        assertInvokeStep(events.next(), Keyword.Given, "a initial step for the first scenario");
        assertInvokeStep(events.next(), Keyword.When, "a second step for the first scenario");
        assertThat(events.next(), instanceOf(EndScenario.class));
        assertThat(events.next(), instanceOf(EndStory.class));
    }

    @Test
    public void complexCase_backgroundWithExample_scenarioWithExample() {
        Story story = new StoryBuilder()
                .append(rawElement(Keyword.Narrative, "Narrative:\nAs a tester\nI want to test my builder\n\n"))
                        // background
                .append(rawElement(Keyword.Background, "Background:\n"))
                .append(rawElement(Keyword.Require, "Require: /story/scenario-shared-<env>.story\n\n", "Require:"))
                .append(rawElement(Keyword.Given, "Given steps for all scenario, owner: <owner>\n"))
                .append(rawElement(Keyword.Given, "Given other steps for all scenario\n\n"))
                .append(rawElement(Keyword.Example, "Example:\n" +
                        "|<owner>|<env>|\n" +
                        "| john  |  dev|\n" +
                        "| etan  | prod|\n"))
                        // scenario 1
                .append(rawElement(Keyword.Scenario, "Scenario: First scenario\n"))
                .append(rawElement(Keyword.Require, "Require: /story/scenario-<env>.story\n\n", "Require:")) //use <env> define from Background
                .append(rawElement(Keyword.Given, "Given a initial step for the first scenario\n"))
                .append(rawElement(Keyword.When, "When a second step for the first scenario\n\n"))
                        // scenario 2
                .append(rawElement(Keyword.Scenario, "Scenario: A second scenario\n"))
                .append(rawElement(Keyword.Given, "Given a initial <name>:<value> step for the second scenario\n"))
                .append(rawElement(Keyword.When, "When an other one <name> & <owner> for the second scenario\n")) // use <owner> define from Background
                .append(rawElement(Keyword.Example, "Example:\n" +
                        "|<name>|<value>|\n" +
                        "| bob  |     12|\n" +
                        "| alice|   1257|\n"))
                .getStory();

        InterpreterListenerRecorder recorder = new InterpreterListenerRecorder();
        SpecIt specIt = new SpecIt();

        // When
        new StoryInterpreter(specIt).interpretStory(story, recorder);

        // Then
        //
        Iterator<Event> events = recorder.getEvents().iterator();
        assertThat(events.next(), instanceOf(BeginStory.class));

        // --- Scenario 1 -w- john
        assertThat(events.next(), instanceOf(BeginScenario.class));
        assertInvokeRequire(events.next(), "/story/scenario-shared-dev.story");
        assertInvokeStep(events.next(), Keyword.Given, "steps for all scenario, owner: john");
        assertInvokeStep(events.next(), Keyword.Given, "other steps for all scenario");
        assertInvokeRequire(events.next(), "/story/scenario-dev.story");
        assertInvokeStep(events.next(), Keyword.Given, "a initial step for the first scenario");
        assertInvokeStep(events.next(), Keyword.When, "a second step for the first scenario");
        assertThat(events.next(), instanceOf(EndScenario.class));

        // --- Scenario 1 -w- etan
        assertThat(events.next(), instanceOf(BeginScenario.class));
        assertInvokeRequire(events.next(), "/story/scenario-shared-prod.story");
        assertInvokeStep(events.next(), Keyword.Given, "steps for all scenario, owner: etan");
        assertInvokeStep(events.next(), Keyword.Given, "other steps for all scenario");
        assertInvokeRequire(events.next(), "/story/scenario-prod.story");
        assertInvokeStep(events.next(), Keyword.Given, "a initial step for the first scenario");
        assertInvokeStep(events.next(), Keyword.When, "a second step for the first scenario");
        assertThat(events.next(), instanceOf(EndScenario.class));

        // --- Scenario 2 -w- john & bob
        assertThat(events.next(), instanceOf(BeginScenario.class));
        assertInvokeRequire(events.next(), "/story/scenario-shared-dev.story");
        assertInvokeStep(events.next(), Keyword.Given, "steps for all scenario, owner: john");
        assertInvokeStep(events.next(), Keyword.Given, "other steps for all scenario");
        assertInvokeStep(events.next(), Keyword.Given, "a initial bob:12 step for the second scenario");
        assertInvokeStep(events.next(), Keyword.When, "an other one bob & john for the second scenario");
        assertThat(events.next(), instanceOf(EndScenario.class));

        // --- Scenario 2 -w- john & alice
        assertThat(events.next(), instanceOf(BeginScenario.class));
        assertInvokeRequire(events.next(), "/story/scenario-shared-dev.story");
        assertInvokeStep(events.next(), Keyword.Given, "steps for all scenario, owner: john");
        assertInvokeStep(events.next(), Keyword.Given, "other steps for all scenario");
        assertInvokeStep(events.next(), Keyword.Given, "a initial alice:1257 step for the second scenario");
        assertInvokeStep(events.next(), Keyword.When, "an other one alice & john for the second scenario");
        assertThat(events.next(), instanceOf(EndScenario.class));

        // --- Scenario 2 -w- etan & bob
        assertThat(events.next(), instanceOf(BeginScenario.class));
        assertInvokeRequire(events.next(), "/story/scenario-shared-prod.story");
        assertInvokeStep(events.next(), Keyword.Given, "steps for all scenario, owner: etan");
        assertInvokeStep(events.next(), Keyword.Given, "other steps for all scenario");
        assertInvokeStep(events.next(), Keyword.Given, "a initial bob:12 step for the second scenario");
        assertInvokeStep(events.next(), Keyword.When, "an other one bob & etan for the second scenario");
        assertThat(events.next(), instanceOf(EndScenario.class));

        // --- Scenario 2 -w- etan & alice
        assertThat(events.next(), instanceOf(BeginScenario.class));
        assertInvokeRequire(events.next(), "/story/scenario-shared-prod.story");
        assertInvokeStep(events.next(), Keyword.Given, "steps for all scenario, owner: etan");
        assertInvokeStep(events.next(), Keyword.Given, "other steps for all scenario");
        assertInvokeStep(events.next(), Keyword.Given, "a initial alice:1257 step for the second scenario");
        assertInvokeStep(events.next(), Keyword.When, "an other one alice & etan for the second scenario");
        assertThat(events.next(), instanceOf(EndScenario.class));


        assertThat(events.next(), instanceOf(EndStory.class));
    }

    @Test
    public void scenario_fragmentAndRepeat() {
        // Given
        Story story = new StoryBuilder()
                .append(rawElement(Keyword.Narrative, "Narrative:\nAs a tester\nI want to test my builder\n\n"))
                        // scenario 1
                .append(rawElement(Keyword.Scenario, "Scenario: First scenario\n", "Scenario:"))
                .append(rawElement(Keyword.Fragment, "Fragment: Add Value\n", "Fragment:"))
                .append(rawElement(Keyword.When, "When I add 2 to x\n\n"))
                // last element ends with an empty line to indicate the end of the fragment
                .append(rawElement(Keyword.Repeat, "Repeat [Add Value] 3 times"))
                .getStory();

        story.traverse(new DumpVisitor());

        InterpreterListenerRecorder recorder = new InterpreterListenerRecorder();

        // When
        new StoryInterpreter(specIt).interpretStory(story, recorder);

        // Then
        Iterator<Event> events = recorder.getEvents().iterator();
        assertThat(events.next(), instanceOf(BeginStory.class));
        assertThat(events.next(), instanceOf(BeginScenario.class));
        assertInvokeStep(events.next(), Keyword.When, "I add 2 to x");
        assertInvokeStep(events.next(), Keyword.When, "I add 2 to x");
        assertInvokeStep(events.next(), Keyword.When, "I add 2 to x");
        assertThat(events.next(), instanceOf(EndScenario.class));
        assertThat(events.next(), instanceOf(EndStory.class));
    }

    @Test
    public void no_scenario_fragmentAndRepeat() {
        // Given
        Story story = new StoryBuilder()
                .append(rawElement(Keyword.Fragment, "Fragment: Add Value\n", "Fragment:"))
                .append(rawElement(Keyword.When, "When I add 2 to x\n\n"))
                        // last element ends with an empty line to indicate the end of the fragment
                .append(rawElement(Keyword.Repeat, "Repeat [Add Value] 3 times"))
                .getStory();

        story.traverse(new DumpVisitor());

        InterpreterListenerRecorder recorder = new InterpreterListenerRecorder();

        // When
        new StoryInterpreter(specIt).interpretStory(story, recorder);

        // Then
        Iterator<Event> events = recorder.getEvents().iterator();
        assertThat(events.next(), instanceOf(BeginStory.class));
        assertThat(events.next(), instanceOf(BeginScenario.class));
        assertInvokeStep(events.next(), Keyword.When, "I add 2 to x");
        assertInvokeStep(events.next(), Keyword.When, "I add 2 to x");
        assertInvokeStep(events.next(), Keyword.When, "I add 2 to x");
        assertThat(events.next(), instanceOf(EndScenario.class));
        assertThat(events.next(), instanceOf(EndStory.class));
    }

    private int offset = 0;

    private RawElement rawElement(Keyword kw, String text) {
        return rawElement(kw, text, kw.name());
    }

    private RawElement rawElement(Keyword kw, String text, String keywordAlias) {
        try {
            return new RawElementDefault(
                    offset,
                    kw,
                    text,
                    keywordAlias,
                    specIt);
        }
        finally {
            offset += text.length();
        }
    }

    private static void assertInvokeStep(Event event, Keyword keyword, String resolved) {
        assertThat(event, instanceOf(InvokeStep.class));
        assertThat(((InvokeStep) event).getKeyword(), equalTo(keyword));
        assertThat(((InvokeStep) event).getResolved(), equalTo(resolved));
    }

    private static void assertInvokeRequire(Event event, String resolved) {
        assertThat(event, instanceOf(InvokeRequire.class));
        assertThat(((InvokeRequire) event).getResolved(), equalTo(resolved));
    }

}
