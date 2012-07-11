package specit.invocation.converter;

import static org.fest.assertions.api.Assertions.assertThat;

import specit.SpecItRuntimeException;

import org.junit.Before;
import org.junit.Test;

/**
 *
 *
 */
public class StringConverterTest {

    private StringConverter converter;

    @Before
    public void setUp () {
        converter = new StringConverter();
    }

    @Test(expected = SpecItRuntimeException.class)
    public void fromString_nullValue() {
        converter.fromString(String.class, null);
    }

    @Test(expected = SpecItRuntimeException.class)
    public void fromString_nullType() {
        converter.fromString(null, "bob");
    }

    @Test
    public void fromString_validValues() {
        Object value = converter.fromString(String.class, "2012");
        assertThat(value).isNotNull().isInstanceOf(String.class).isEqualTo("2012");
    }

    @Test
    public void fromString_validValues_subType() {
        Object value = converter.fromString(CharSequence.class, "2012");
        assertThat(value).isNotNull().isInstanceOf(String.class).isEqualTo("2012");
    }

    @Test
    public void suggest() {
        assertThat(converter.suggest("20")).contains("20");
        assertThat(converter.suggest("2011")).contains("2011");
        assertThat(converter.suggest("20aa")).contains("20aa");
    }
}
