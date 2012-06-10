package specit.usecase;

import org.junit.Before;
import org.junit.Test;
import specit.Conf;
import specit.element.Keyword;
import specit.element.RawPart;
import specit.parser.ListenerCollector;
import specit.parser.Parser;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

public class ContextSpecificationTest {
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

        // Given When Then
        // Arrange Act Assert
        // When Should
    }

    @Test
    public void specIt_canBeUsed_forContextSpecification () {
        String story =
                "When a product is added that is not already in the cart\n" +
                " - The cart item factory should be used to create a cart item for the product being added.\n" +
                "When an item is added\n" +
                " - The item count should be incremented\n" +
                " - The item should be added to the underlying list";

        conf.withAlias(Keyword.When, "When").withAlias(Keyword.Then, "-");
        parser.scan(story, listener);

        List<RawPart> steps = listener.getSteps();
        assertThat(steps, hasSize(5));
        assertThat(steps.get(0), equalTo(new RawPart(0, Keyword.When, "When a product is added that is not already in the cart\n", "When")));
        assertThat(steps.get(1), equalTo(new RawPart(56, Keyword.Then, " - The cart item factory should be used to create a cart item for the product being added.\n", "-")));
        assertThat(steps.get(2), equalTo(new RawPart(147, Keyword.When, "When an item is added\n", "When")));
        assertThat(steps.get(3), equalTo(new RawPart(169, Keyword.Then, " - The item count should be incremented\n", "-")));
        assertThat(steps.get(4), equalTo(new RawPart(209, Keyword.Then, " - The item should be added to the underlying list", "-")));
    }
}
