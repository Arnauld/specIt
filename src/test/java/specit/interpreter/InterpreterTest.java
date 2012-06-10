package specit.interpreter;

import org.junit.Test;
import specit.Conf;
import specit.element.*;

/**
 *
 *
 */
public class InterpreterTest {
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

        //
        Conf conf = new Conf();
        new Interpreter(conf).interpretStory(story, new InterpreterListener() {
            @Override
            public void beginStory(Story story) {
                System.out.println("beginStory()");
            }

            @Override
            public void endStory(Story story) {
                System.out.println("endStory()");
            }

            @Override
            public void beginScenario(ExecutablePart scenario, ExecutionContext context) {
                System.out.println("beginScenario(" + context.getVariables() + ")");
            }

            @Override
            public void endScenario(ExecutablePart scenario, ExecutionContext context) {
                System.out.println("endScenario()");
            }
        });
    }

    private int offset = 0;

    private RawPart rawPart(Keyword kw, String text) {
        RawPart part = new RawPart(offset, kw, text, kw.name());
        offset += text.length();
        return part;
    }
}
