package specit.parser;

import org.junit.Before;
import org.junit.Test;
import specit.SpecIt;
import specit.element.Keyword;
import specit.element.RawPart;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

public class ParserTest {
    private static final String NL = "\n";

    private Parser parser;
    private SpecIt specIt;
    //
    private ListenerCollector listener;

    @Before
    public void setUp() {
        listener = new ListenerCollector();
        specIt = new SpecIt();
        parser = new Parser(specIt);
    }

    @Test
    public void scan_singleStep() {
        specIt.withAlias(Keyword.Given, "With");
        parser.scan("With a simple step", listener);

        List<RawPart> steps = listener.getSteps();
        assertThat(steps, hasSize(1));
        assertThat(steps.get(0), equalTo(rawPart(0, Keyword.Given, "With a simple step", "With")));
    }

    @Test
    public void scan_withTrailingNewlines() {
        specIt.withAlias(Keyword.Given, "With");
        parser.scan("\n\nWith a simple step", listener);

        List<RawPart> steps = listener.getSteps();
        assertThat(steps, hasSize(2));
        assertThat(steps.get(0), equalTo(rawPart(0, Keyword.Unknown, "\n\n", null)));
        assertThat(steps.get(1), equalTo(rawPart(2, Keyword.Given, "With a simple step", "With")));
    }

    @Test
    public void scan_withTrailingWhitechars_spaces() {
        specIt.withAlias(Keyword.Given, "With");
        parser.scan("\n\n  With a simple step", listener);

        List<RawPart> steps = listener.getSteps();
        assertThat(steps, hasSize(2));
        assertThat(steps.get(0), equalTo(rawPart(0, Keyword.Unknown, "\n\n", null)));
        assertThat(steps.get(1), equalTo(rawPart(2, Keyword.Given, "  With a simple step", "With")));
    }

    @Test
    public void scan_multipleAliasesForKeyword() {
        String story =
                "Given a defined step" + NL +
                        "With an other simple step";

        specIt.withAlias(Keyword.Given, "With");
        specIt.withAlias(Keyword.Given, "Given");
        parser.scan(story, listener);

        List<RawPart> steps = listener.getSteps();
        assertThat(steps, hasSize(2));
        assertThat(steps.get(0), equalTo(rawPart(0, Keyword.Given, "Given a defined step\n", "Given")));
        assertThat(steps.get(1), equalTo(rawPart(21, Keyword.Given, "With an other simple step", "With")));
    }

    @Test
    public void scan_multipleKeywords_andIsResolvedUsingPrevious() {
        String story =
                "Given a defined step" + NL +
                        "With an other step" + NL +
                        "And yet an other one";

        specIt.withAlias(Keyword.Given, "Given");
        specIt.withAlias(Keyword.Given, "With");
        specIt.withAlias(Keyword.And, "And");
        parser.scan(story, listener);

        List<RawPart> steps = listener.getSteps();
        assertThat(steps, hasSize(3));
        assertThat(steps.get(0), equalTo(rawPart(0, Keyword.Given, "Given a defined step\n", "Given")));
        assertThat(steps.get(1), equalTo(rawPart(21, Keyword.Given, "With an other step\n", "With")));
        assertThat(steps.get(2), equalTo(rawPart(40, Keyword.And, "And yet an other one", "And")));
    }

    private static RawPart rawPart(int offset, Keyword kw, String rawContent, String alias) {
        return new RawPartDefault(offset, kw, rawContent, alias);
    }
}
