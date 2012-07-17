package specit;

import static java.util.Arrays.asList;
import static org.fest.assertions.api.Assertions.assertThat;

import specit.element.Alias;
import specit.element.Keyword;

import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;

/**
 *
 *
 */
public class SpecItTest {

    private SpecIt specIt;

    @Before
    public void setUp () {
        specIt = new SpecIt();
    }

    @Test
    public void defaultKeywords() {
        assertThat(specIt.aliases()).containsAll(asList(
                new Alias(Keyword.Narrative, "Narrative:"),
                new Alias(Keyword.Scenario, "Scenario:"),
                new Alias(Keyword.Background, "Background:"),
                new Alias(Keyword.Given, "Given"),
                new Alias(Keyword.When, "When"),
                new Alias(Keyword.Then, "Then"),
                new Alias(Keyword.Require, "Require"),
                new Alias(Keyword.And, "And"),
                new Alias(Keyword.And, "But"),
                new Alias(Keyword.Example, "Example:"),
                new Alias(Keyword.Fragment, "Fragment:"),
                new Alias(Keyword.Repeat, "Repeat")
                ));
    }
}
