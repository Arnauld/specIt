package specit.element;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;

import specit.parser.RawElementDefault;

import org.junit.Test;

/**
 *
 */
public class StoryBuilderTest {

    @Test
    public void completeCase_requireCreateADefault() {
        Story story = new StoryBuilder()
                .append(rawPart(Keyword.Narrative, "Narrative:\nAs a tester\nI want to test my builder\n\n"))
                .append(rawPart(Keyword.Require, "Require: /story/scenario-shared-env.story\n\n"))
                .getStory();
        assertThat(story.getScenarioList().size(), equalTo(1));
        assertThat(story.getScenarioList().get(0), instanceOf(DefaultExecutablePart.class));
    }

    @Test(expected = InvalidElementDefinitionException.class)
    public void completeCase_backgroundIsNotAllowed_onceScenarioStarts() {
        new StoryBuilder()
                .append(rawPart(Keyword.Narrative, "Narrative:\nAs a tester\nI want to test my builder\n\n"))
                .append(rawPart(Keyword.Scenario, "Scenario: First scenario\n"))
                .append(rawPart(Keyword.Background, "Background:\n"))
        ;
    }

    @Test(expected = InvalidElementDefinitionException.class)
    public void completeCase_backgroundIsNotAllowed_onceStepStarts() {
        new StoryBuilder()
                .append(rawPart(Keyword.Narrative, "Narrative:\nAs a tester\nI want to test my builder\n\n"))
                .append(rawPart(Keyword.Given, "Given steps for all scenario\n"))
                .append(rawPart(Keyword.Background, "Background:\n"))
        ;
    }

    @Test
    public void completeCase() {
        Story story = new StoryBuilder()
                .append(rawPart(Keyword.Narrative, "Narrative:\nAs a tester\nI want to test my builder\n\n"))
                .append(rawPart(Keyword.Background, "Background:\n"))
                .append(rawPart(Keyword.Require, "Require: /story/scenario-shared-env.story\n\n"))
                .append(rawPart(Keyword.Given, "Given steps for all scenario\n"))
                .append(rawPart(Keyword.Given, "Given other steps for all scenario\n\n"))
                .append(rawPart(Keyword.Scenario, "Scenario: First scenario\n"))
                .append(rawPart(Keyword.Require, "Require: /story/scenario-env.story\n\n"))
                .append(rawPart(Keyword.Given, "Given a initial step\n"))
                .append(rawPart(Keyword.When, "When a second step\n\n"))
                .append(rawPart(Keyword.Scenario, "Scenario: A second scenario\n"))
                .append(rawPart(Keyword.Given, "Given a initial step\n"))
                .append(rawPart(Keyword.Given, "Given an other one\n"))
                .append(rawPart(Keyword.Example, "Example:\n" +
                        "|<name>|<value>|\n" +
                        "| bob  |     12|\n" +
                        "| alice|   1257|\n"))
                .getStory();

        // TODO replace visual assert (dump) by programmatic assert!
        story.traverse(new DumpVisitor());
    }

    private int offset = 0;

    private RawElement rawPart(Keyword kw, String text) {
        RawElement element = new RawElementDefault(offset, kw, text, kw.name());
        offset += text.length();
        return element;
    }
}
