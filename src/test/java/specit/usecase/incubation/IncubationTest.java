package specit.usecase.incubation;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import specit.SpecIt;
import specit.element.DumpVisitor;
import specit.element.Keyword;
import specit.element.Story;

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
    public void scenario1() throws IOException {
        String resourceName = "/stories/incubation/complete_usecase_003_fragment.story";
        Story story = specIt.parseAndBuildStory(resourceAsString(resourceName));
        story.traverse(new DumpVisitor());
    }

    private static String resourceAsString(String resourceName) throws IOException {
        InputStream stream = IncubationTest.class.getResourceAsStream(resourceName);
        try {
            return IOUtils.toString(stream, "UTF-8");
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }
}
