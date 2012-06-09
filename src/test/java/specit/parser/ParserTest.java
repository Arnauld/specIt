package specit.parser;

import org.junit.Before;
import org.junit.Test;
import specit.element.Conf;
import specit.element.Keyword;
import specit.element.RawPart;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

public class ParserTest {
    private static final String NL = "\n";

    private Parser parser;
    private Conf conf;
    //
    private ListenerCollector listener;

    @Before
    public void setUp() {
        listener = new ListenerCollector();
        conf = new Conf();
        parser = new Parser(conf);
    }

    @Test
    public void scan_singleStep () {
        conf.withAlias(Keyword.Given, "With");
        parser.scan("With a simple step", listener);

        List<RawPart> steps = listener.getSteps();
        assertThat(steps, hasSize(1));
        assertThat(steps.get(0), equalTo(new RawPart(0, Keyword.Given, "With a simple step", "With")));
    }

    @Test
    public void scan_withTrailingNewlines () {
        conf.withAlias(Keyword.Given, "With");
        parser.scan("\n\nWith a simple step", listener);

        List<RawPart> steps = listener.getSteps();
        assertThat(steps, hasSize(2));
        assertThat(steps.get(0), equalTo(new RawPart(0, Keyword.Unknown, "\n\n", null)));
        assertThat(steps.get(1), equalTo(new RawPart(2, Keyword.Given, "With a simple step", "With")));
    }

    @Test
    public void scan_withTrailingWhitechars_spaces () {
        conf.withAlias(Keyword.Given, "With");
        parser.scan("\n\n  With a simple step", listener);

        List<RawPart> steps = listener.getSteps();
        assertThat(steps, hasSize(2));
        assertThat(steps.get(0), equalTo(new RawPart(0, Keyword.Unknown, "\n\n", null)));
        assertThat(steps.get(1), equalTo(new RawPart(2, Keyword.Given, "  With a simple step", "With")));
    }

    @Test
    public void scan_multipleAliasesForKeyword () {
        String story =
                "Given a defined step" + NL +
                "With an other simple step";

        conf.withAlias(Keyword.Given, "With");
        conf.withAlias(Keyword.Given, "Given");
        parser.scan(story, listener);

        List<RawPart> steps = listener.getSteps();
        assertThat(steps, hasSize(2));
        assertThat(steps.get(0), equalTo(new RawPart(0, Keyword.Given, "Given a defined step\n", "Given")));
        assertThat(steps.get(1), equalTo(new RawPart(21, Keyword.Given, "With an other simple step", "With")));
    }

    @Test
    public void scan_multipleKeywords_andIsResolvedUsingPrevious () {
        String story =
                "Given a defined step" + NL +
                "With an other step" + NL +
                "And yet an other one";

        conf.withAlias(Keyword.Given, "Given");
        conf.withAlias(Keyword.Given, "With");
        conf.withAlias(Keyword.And, "And");
        parser.scan(story, listener);

        List<RawPart> steps = listener.getSteps();
        assertThat(steps, hasSize(3));
        assertThat(steps.get(0), equalTo(new RawPart(0, Keyword.Given, "Given a defined step\n", "Given")));
        assertThat(steps.get(1), equalTo(new RawPart(21, Keyword.Given, "With an other step\n", "With")));
        assertThat(steps.get(2), equalTo(new RawPart(40, Keyword.And, "And yet an other one", "And")));
    }

}
