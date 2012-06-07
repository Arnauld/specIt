package specit.parser;

import org.junit.Before;
import org.junit.Test;
import specit.Keyword;
import specit.Part;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

public class ParserTest {
    private static final String NL = "\n";

    private List<Part> steps;
    private Listener listener;
    private Parser parser;

    @Before
    public void setUp() {
        steps = new ArrayList<Part>();
        listener = new Listener() {
            @Override
            public void on(Part step) {
                steps.add(step);
            }
        };
        parser = new Parser();
    }

    @Test
    public void scan_singleStep () {
        parser.withAlias(Keyword.Given, "With");
        parser.scan("With a simple step", listener);

        assertThat(steps, hasSize(1));
        assertThat(steps.get(0), equalTo(new Part(0, Keyword.Given, "With a simple step", "With")));
    }

    @Test
    public void scan_withTrailingNewlines () {
        parser.withAlias(Keyword.Given, "With");
        parser.scan("\n\nWith a simple step", listener);

        assertThat(steps, hasSize(2));
        assertThat(steps.get(0), equalTo(new Part(0, Keyword.Unknown, "\n\n", null)));
        assertThat(steps.get(1), equalTo(new Part(2, Keyword.Given, "With a simple step", "With")));
    }

    @Test
    public void scan_withTrailingWhitechars_spaces () {
        parser.withAlias(Keyword.Given, "With");
        parser.scan("\n\n  With a simple step", listener);

        assertThat(steps, hasSize(2));
        assertThat(steps.get(0), equalTo(new Part(0, Keyword.Unknown, "\n\n", null)));
        assertThat(steps.get(1), equalTo(new Part(2, Keyword.Given, "  With a simple step", "With")));
    }

    @Test
    public void scan_multipleAliasesForKeyword () {
        String story =
                "Given a defined step" + NL +
                "With an other simple step";

        parser.withAlias(Keyword.Given, "With");
        parser.withAlias(Keyword.Given, "Given");
        parser.scan(story, listener);

        assertThat(steps, hasSize(2));
        assertThat(steps.get(0), equalTo(new Part(0, Keyword.Given, "Given a defined step\n", "Given")));
        assertThat(steps.get(1), equalTo(new Part(21, Keyword.Given, "With an other simple step", "With")));
    }

    @Test
    public void scan_multipleKeywords_andIsResolvedUsingPrevious () {
        String story =
                "Given a defined step" + NL +
                "With an other step" + NL +
                "And yet an other one";

        parser.withAlias(Keyword.Given, "Given");
        parser.withAlias(Keyword.Given, "With");
        parser.withAlias(Keyword.And, "And");
        parser.scan(story, listener);

        assertThat(steps, hasSize(3));
        assertThat(steps.get(0), equalTo(new Part(0, Keyword.Given, "Given a defined step\n", "Given")));
        assertThat(steps.get(1), equalTo(new Part(21, Keyword.Given, "With an other step\n", "With")));
        assertThat(steps.get(2), equalTo(new Part(40, Keyword.And, "And yet an other one", "And")));
    }

}
