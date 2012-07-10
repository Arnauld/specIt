package specit.parser;

import org.junit.Test;
import specit.element.InvalidElementDefinitionException;
import specit.element.Keyword;
import specit.element.RawElement;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

public class RawPartDefaultTest {

    @Test
    public void integrity_step_mustContains_keywordAlias_ok() {
        new RawElementDefault(17, Keyword.Given, "  \t When a user clicks \n", "When");
    }

    @Test(expected = InvalidElementDefinitionException.class)
    public void integrity_step_mustContains_keywordAlias_ko() {
        new RawElementDefault(17, Keyword.Given, "  \t When a user clicks \n", "Whoot");
    }

    @Test
    public void integrity_noKeywordAlias_notUnknownKeyword_mustThrowAnException() {
        for (Keyword kw : Keyword.values()) {
            if (kw != Keyword.Unknown)
                assertExceptionIsThrownWithNullAlias("" + kw + " should not be allowed with null alias", kw);
        }
    }

    private void assertExceptionIsThrownWithNullAlias(String message, Keyword kw) {
        try {
            new RawElementDefault(17, kw, "  \t When a user clicks \n", null);
            fail(message);
        } catch (InvalidElementDefinitionException e) {
            // ok :)
        }
    }

    @Test
    public void integrity_noKeywordAlias_unknownKeyword_isOk() {
        new RawElementDefault(17, Keyword.Unknown, "  \t When a user clicks \n", null);
    }

    @Test
    public void contentAfterAlias_() {
        RawElement rawElement = new RawElementDefault(17, Keyword.Given, "When a user clicks \n", "When");
        assertThat(rawElement.contentAfterAlias(), equalTo(" a user clicks \n"));
    }

    @Test
    public void contentAfterAlias_withTrailingCharacters() {
        RawElement rawElement = new RawElementDefault(17, Keyword.Given, "  \t When a user clicks \n", "When");
        assertThat(rawElement.contentAfterAlias(), equalTo(" a user clicks \n"));
    }
}
