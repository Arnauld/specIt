package specit.usecase.calculator.annotationcontext;

import static org.fest.assertions.api.Assertions.assertThat;

import specit.SpecIt;
import specit.element.DumpVisitor;
import specit.element.Keyword;
import specit.element.Story;
import specit.invocation.ParameterMappingException;
import specit.report.ConsoleColoredReporter;
import specit.report.StatsReporter;
import specit.util.IDE;

import org.fest.assertions.api.IntegerAssert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 *
 */
public class CalculatorTest {

    private SpecIt specIt;
    private StatsReporter statsReporter;

    @Before
    public void setUp() {
        statsReporter = new StatsReporter();

        specIt = new SpecIt();
        specIt.withAlias(Keyword.Scenario, "Scenario:")
                .withAlias(Keyword.Given, "Given")
                .withAlias(Keyword.When, "When")
                .withAlias(Keyword.Then, "Then")
                .withAlias(Keyword.Fragment, "Fragment:")
                .withAlias(Keyword.Repeat, "Repeat")
                .withReporters(
                        new ConsoleColoredReporter(!IDE.isExecutedWithinIDE()),
                        statsReporter);
    }

    @Test
    public void scenario_given_when_then() throws ParameterMappingException {
        String story = "Scenario: 2+2\n" + //
                "\n" + //
                "Given a variable x with value 2\n" + //
                "When I add 2 to x\n" + //
                "Then x should equal to 4";
        specIt.scanAnnotations(CalculatorSteps.class);
        specIt.executeStoryContent(story);
        assertNoFailure();
    }

    private void assertNoFailure() {
        assertThat(statsReporter.failureCount()).isEqualTo(0);
    }

    @Test
    public void scenario__fragment_and_repeatWithTable() throws ParameterMappingException {
        String story = "Scenario: Additions using repeat and table\n" + //
                "\n" + //
                "  Fragment: Add Value\n" + //
                "  When I add <value> to <variable>\n" + //
                "\n" + //
                "  Given a variable x with value 2\n" + //
                "  Repeat [Add Value] with:\n" + //
                "    | value | variable |\n" + //
                "    |   3   |        x |\n" + //
                "    |   5   |        x |\n" + //
                "    |   7   |        x |\n" + //
                "  Then x should equal to 17";
        specIt.scanAnnotations(CalculatorSteps.class);
        specIt.executeStoryContent(story);
        assertNoFailure();
    }

    @Test
    public void no_scenario_keyword__given_when_then() throws ParameterMappingException {
        String story = //
                "Given a variable x with value 2\n" + //
                "When I add 2 to x\n" + //
                "Then x should equal to 4";
        specIt.scanAnnotations(CalculatorSteps.class);
        specIt.executeStoryContent(story);
        assertNoFailure();
    }

    @Test
    public void no_scenario_keyword__fragment_and_repeatWithTable() throws ParameterMappingException {
        String storyContent =
                "  Fragment: Add Value\n" + //
                "  When I add <value> to <variable>\n" + //
                "\n" + //
                "  Given a variable x with value 2\n" + //
                "  Repeat [Add Value] with:\n" + //
                "    | value | variable |\n" + //
                "    |   3   |        x |\n" + //
                "    |   5   |        x |\n" + //
                "    |   7   |        x |\n" + //
                "  Then x should equal to 17";
        specIt.scanAnnotations(CalculatorSteps.class);
        Story story = specIt.parseAndBuildStory(storyContent);
        //story.traverse(new DumpVisitor());
        specIt.interpretStory(story);
        assertNoFailure();
    }

    @Test
    public void no_scenario_keyword__multipleFragments_and_repeatWithTable_and_repeatNTimes() throws ParameterMappingException {
        String story =
                "Scenario: Additions using mutiple fragments, repeat and table and repeat n times\n" + //
                "\n" + //
                "  Fragment: Add Value\n" + //
                "  When I add <value> to <variable>\n" + //
                "\n" + //
                "  Fragment: Add 2 to x\n" + //
                "  When I add 2 to x\n" + //
                "\n" + //
                "  Given a variable x with value 2\n" + //
                "  Repeat [Add Value] with:\n" + //
                "    | value | variable |\n" + //
                "    |   3   |        x |\n" + //
                "    |   5   |        x |\n" + //
                "    |   7   |        x |\n" + //
                "  Repeat [Add 2 to x] 3 times\n" + //
                "  Then x should equal to 23";
        specIt.scanAnnotations(CalculatorSteps.class);
        specIt.executeStoryContent(story);

        assertNoFailure();
    }

}
