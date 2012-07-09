package specit.element;

import static org.fest.assertions.api.Assertions.assertThat;

import org.fest.assertions.api.MapAssert;
import org.junit.Test;

/**
 *
 */
public class AliasTest {

    @Test(expected = InvalidElementDefinitionException.class)
    public void ctorParameters_Keyword_isMandatory() {
        new Alias(null, "hop");
    }

    @Test(expected = InvalidElementDefinitionException.class)
    public void ctorParameters_KeywordAlias_isMandatory() {
        new Alias(Keyword.Given, null);
    }

    @Test(expected = InvalidElementDefinitionException.class)
    public void ctorParameters_KeywordAndKeywordAlias_areMandatory() {
        new Alias(null, null);
    }

    @Test
    public void equals() {
        assertThat(new Alias(Keyword.Given, "hop"))
                .isEqualTo(new Alias(Keyword.Given, "hop"))
                .isNotEqualTo(new Alias(Keyword.Given, "hOp"))
                .isNotEqualTo(new Alias(Keyword.Given, ""))
                .isNotEqualTo(new Alias(Keyword.Then, "hop"))
                .isNotEqualTo(null)
                .isNotEqualTo(new Alias(Keyword.Given, "hop") {})
        ;
    }
}
