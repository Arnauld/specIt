package specit.usecase.incubation;

import specit.SpecIt;
import specit.element.DumpVisitor;
import specit.element.ExecutablePart;
import specit.element.InvokableStep;
import specit.element.Keyword;
import specit.element.Story;
import specit.interpreter.InterpreterContext;
import specit.interpreter.InterpreterListener;
import specit.interpreter.StoryInterpreter;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 *
 */
public class IncubationDumpTest {

    private SpecIt specIt;

    @Before
    public void setUp() {
        specIt = new SpecIt();
        specIt.withAlias(Keyword.Scenario, "Scenario:")
                .withAlias(Keyword.Given, "Given")
                .withAlias(Keyword.When, "When")
                .withAlias(Keyword.Then, "Then")
                .withAliases(Keyword.And, "And", "But")
                .withAlias(Keyword.Fragment, "Fragment:")
                .withAlias(Keyword.Repeat, "Repeat")
        ;
    }

    @Test
    public void scenario1() throws IOException {
        String resourceName = "/stories/incubation/complete_usecase_003_fragment_light.story";
        Story story = specIt.parseAndBuildStory("test-name", resourceAsString(resourceName));
        story.traverse(new DumpVisitor());
        new StoryInterpreter(specIt).interpretStory(story, new InterpreterListener() {
            @Override
            public void beginStory(Story story) {
                System.out.println("IncubationDumpTest.beginStory(" + story.getRawElement() + ")");
            }

            @Override
            public void endStory(Story story) {
                System.out.println("IncubationDumpTest.endStory");
            }

            @Override
            public void beginScenario(ExecutablePart scenario, InterpreterContext context) {
                System.out.println("IncubationDumpTest.beginScenario(" + scenario.getRawElement() + ")");
            }

            @Override
            public void endScenario(ExecutablePart scenario, InterpreterContext context) {
                System.out.println("IncubationDumpTest.endScenario");
            }

            @Override
            public void invokeStep(InvokableStep invokableStep, InterpreterContext context) {
                System.out.println("IncubationDumpTest.stepInvoked(" + invokableStep.getKeyword() + ":"
                        + invokableStep.getAdjustedInput() + ")");
            }

            @Override
            public void invokeRequire(String resolved, InterpreterContext context) {
                System.out.println("IncubationDumpTest.invokeRequire(" + resolved + ")");
            }

        });
    }

    private static String resourceAsString(String resourceName) throws IOException {
        InputStream stream = IncubationDumpTest.class.getResourceAsStream(resourceName);
        try {
            return IOUtils.toString(stream, "UTF-8");
        }
        finally {
            IOUtils.closeQuietly(stream);
        }
    }
}
