package specit.interpreter;

import org.junit.Test;
import specit.Conf;
import specit.element.*;
import specit.interpreter.InterpreterListenerRecorder.*;
import specit.parser.ExampleVariablesParser;
import specit.util.New;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 *
 *
 */
public class StoryInterpreterTest {
    @Test
    public void simpleCase() {
        // Given
        Story story = new StoryBuilder()
                .append(rawPart(Keyword.Narrative, "Narrative:\nAs a tester\nI want to test my builder\n\n"))
                // scenario 1
                .append(rawPart(Keyword.Scenario, "Scenario: First scenario\n"))
                .append(rawPart(Keyword.Require, "Require: /story/scenario-env.story\n\n", "Require:"))
                .append(rawPart(Keyword.Given, "Given a initial step for the first scenario\n"))
                .append(rawPart(Keyword.When, "When a second step for the first scenario\n\n"))
                .getStory();

        InterpreterListenerRecorder recorder = new InterpreterListenerRecorder();
        Conf conf = new Conf();

        // When
        new StoryInterpreter(conf).interpretStory(story, recorder);

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
                .append(rawPart(Keyword.Narrative, "Narrative:\nAs a tester\nI want to test my builder\n\n"))
                // background
                .append(rawPart(Keyword.Background, "Background:\n"))
                .append(rawPart(Keyword.Require, "Require: /story/scenario-shared.story\n\n", "Require:"))
                .append(rawPart(Keyword.Given, "Given steps for all scenario\n"))
                .append(rawPart(Keyword.Given, "Given other steps for all scenario\n\n"))
                // scenario 1
                .append(rawPart(Keyword.Scenario, "Scenario: First scenario\n"))
                .append(rawPart(Keyword.Require, "Require: /story/scenario-env.story\n\n", "Require:"))
                .append(rawPart(Keyword.Given, "Given a initial step for the first scenario\n"))
                .append(rawPart(Keyword.When, "When a second step for the first scenario\n\n"))
                .getStory();

        InterpreterListenerRecorder recorder = new InterpreterListenerRecorder();
        Conf conf = new Conf();

        // When
        new StoryInterpreter(conf).interpretStory(story, recorder);

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
                .append(rawPart(Keyword.Narrative, "Narrative:\nAs a tester\nI want to test my builder\n\n"))
                        // background
                .append(rawPart(Keyword.Background, "Background:\n"))
                .append(rawPart(Keyword.Require, "Require: /story/scenario-shared-<env>.story\n\n", "Require:"))
                .append(rawPart(Keyword.Given, "Given steps for all scenario, owner: <owner>\n"))
                .append(rawPart(Keyword.Given, "Given other steps for all scenario\n\n"))
                .append(rawPart(Keyword.Example, "Example:\n" +
                        "|<owner>|<env>|\n" +
                        "| john  |  dev|\n" +
                        "| etan  | prod|\n"))
                        // scenario 1
                .append(rawPart(Keyword.Scenario, "Scenario: First scenario\n"))
                .append(rawPart(Keyword.Require, "Require: /story/scenario-<env>.story\n\n", "Require:")) //use <env> define from Background
                .append(rawPart(Keyword.Given, "Given a initial step for the first scenario\n"))
                .append(rawPart(Keyword.When, "When a second step for the first scenario\n\n"))
                        // scenario 2
                .append(rawPart(Keyword.Scenario, "Scenario: A second scenario\n"))
                .append(rawPart(Keyword.Given, "Given a initial <name>:<value> step for the second scenario\n"))
                .append(rawPart(Keyword.When, "When an other one <name> & <owner> for the second scenario\n")) // use <owner> define from Background
                .append(rawPart(Keyword.Example, "Example:\n" +
                        "|<name>|<value>|\n" +
                        "| bob  |     12|\n" +
                        "| alice|   1257|\n"))
                .getStory();

        InterpreterListenerRecorder recorder = new InterpreterListenerRecorder();
        Conf conf = new Conf();

        // When
        new StoryInterpreter(conf).interpretStory(story, recorder);

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

    private int offset = 0;

    private RawPart rawPart(Keyword kw, String text) {
        return rawPart(kw, text, kw.name());
    }

    private RawPart rawPart(Keyword kw, String text, String keywordAlias) {
        try {
            List<Map<String,String>> variablesRows = New.arrayList();
            if (kw == Keyword.Example)
                variablesRows = new ExampleVariablesParser(new Conf()).parseVariablesRows(text);
            return new RawPart(
                    offset,
                    kw,
                    text,
                    keywordAlias,
                    New.<Comment>arrayList(),
                    variablesRows);
        } finally {
            offset += text.length();
        }
    }

    private static void assertInvokeStep(Event event, Keyword keyword, String resolved) {
        assertThat(event, instanceOf(InvokeStep.class));
        assertThat(((InvokeStep)event).getKeyword(), equalTo(keyword));
        assertThat(((InvokeStep)event).getResolved(), equalTo(resolved));
    }

    private static void assertInvokeRequire(Event event, String resolved) {
        assertThat(event, instanceOf(InvokeRequire.class));
        assertThat(((InvokeRequire)event).getResolved(), equalTo(resolved));
    }

}
