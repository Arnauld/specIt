package specit.element;

import org.junit.Test;
import specit.parser.RawPartDefault;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

public class RawPartTest {

    @Test
    public void integrity_step_mustContains_keywordAlias_ok() {
        new RawPartDefault(17, Keyword.Given, "  \t When a user clicks \n", "When");
    }

    @Test(expected = IllegalArgumentException.class)
    public void integrity_step_mustContains_keywordAlias_ko() {
        new RawPartDefault(17, Keyword.Given, "  \t When a user clicks \n", "Whoot");
    }

    @Test
    public void integrity_noKeywordAlias_notUnknownKeyword_mustThrowAnException() {
        for(Keyword kw : Keyword.values()) {
            if(kw != Keyword.Unknown)
                assertExceptionIsThrownWithNullAlias("" + kw + " should not be allowed with null alias", kw);
        }
    }

    private void assertExceptionIsThrownWithNullAlias(String message, Keyword kw) {
        try {
            new RawPartDefault(17, kw, "  \t When a user clicks \n", null);
            fail(message);
        } catch (IllegalArgumentException e) {
            // ok :)
        }
    }

    @Test
    public void integrity_noKeywordAlias_unknownKeyword_isOk() {
        new RawPartDefault(17, Keyword.Unknown, "  \t When a user clicks \n", null);
    }

    @Test
    public void contentAfterAlias_() {
        RawPart rawPart = new RawPartDefault(17, Keyword.Given, "When a user clicks \n", "When");
        assertThat(rawPart.contentAfterAlias(), equalTo(" a user clicks \n"));
    }

    @Test
    public void contentAfterAlias_withTrailingCharacters() {
        RawPart rawPart = new RawPartDefault(17, Keyword.Given, "  \t When a user clicks \n", "When");
        assertThat(rawPart.contentAfterAlias(), equalTo(" a user clicks \n"));
    }
}
