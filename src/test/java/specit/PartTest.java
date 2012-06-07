package specit;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class PartTest {

    @Test
    public void ctor_step_mustContains_keywordAlias_ok() {
        new Part(17, Keyword.Given, "  \t When a user clicks \n", "When");
    }

    @Test(expected = IllegalArgumentException.class)
    public void ctor_step_mustContains_keywordAlias_ko() {
        new Part(17, Keyword.Given, "  \t When a user clicks \n", "Whoot");
    }

    @Test
    public void contentAfterAlias_withTrailingCharacters() {
        Part part = new Part(17, Keyword.Given, "  \t When a user clicks \n", "When");
        MatcherAssert.assertThat(part.contentAfterAlias(), equalTo(" a user clicks \n"));
    }
}
