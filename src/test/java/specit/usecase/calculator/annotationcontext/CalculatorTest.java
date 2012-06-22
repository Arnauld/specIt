package specit.usecase.calculator.annotationcontext;

import org.junit.Before;
import org.junit.Test;
import specit.SpecIt;
import specit.element.Keyword;
import specit.invocation.ParameterMappingException;

/**
 *
 *
 */
public class CalculatorTest {

    private static String SCENARIO_1 = "Scenario: 2+2\n" + //
            "\n" + //
            "Given a variable x with value 2\n" + //
            "When I add 2 to x\n" + //
            "Then x should equal to 4";
    private SpecIt specIt;

    @Before
    public void setUp() {
        specIt = new SpecIt();
        specIt.withAlias(Keyword.Scenario, "Scenario:")
                .withAlias(Keyword.Given, "Given")
                .withAlias(Keyword.When, "When")
                .withAlias(Keyword.Then, "Then");
    }

    @Test
    public void scenario1() throws ParameterMappingException {
        specIt.scanAnnotations(CalculatorSteps.class);
        specIt.executeStory(SCENARIO_1);
    }

}
