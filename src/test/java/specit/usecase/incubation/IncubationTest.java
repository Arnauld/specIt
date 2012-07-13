package specit.usecase.incubation;

import specit.SpecIt;
import specit.element.Keyword;
import specit.invocation.ParameterMappingException;
import specit.report.ConsoleColoredReporter;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 *
 */
public class IncubationTest {

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
    public void scenario1() throws IOException, ParameterMappingException {
        String resourceName = "/stories/incubation/complete_usecase_003_fragment_light.story";
        specIt.withReporter(new ConsoleColoredReporter());
        specIt.scanAnnotations(IncubationSteps.class);
        specIt.executeStoryContent(resourceAsString(resourceName));
    }

    private static String resourceAsString(String resourceName) throws IOException {
        InputStream stream = IncubationTest.class.getResourceAsStream(resourceName);
        try {
            return IOUtils.toString(stream, "UTF-8");
        }
        finally {
            IOUtils.closeQuietly(stream);
        }
    }
}
