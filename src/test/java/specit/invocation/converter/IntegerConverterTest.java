package specit.invocation.converter;

import static org.fest.assertions.api.Assertions.assertThat;

import specit.SpecItRuntimeException;

import org.junit.Before;
import org.junit.Test;

/**
 *
 *
 */
public class IntegerConverterTest {

    private IntegerConverter converter;

    @Before
    public void setUp () {
        converter = new IntegerConverter();
    }

    @Test(expected = SpecItRuntimeException.class)
    public void fromString_nullValue() {
        converter.fromString(int.class, null);
    }

    @Test(expected = SpecItRuntimeException.class)
    public void fromString_nullType() {
        converter.fromString(null, "12");
    }

    @Test(expected = SpecItRuntimeException.class)
    public void fromString_invalidType() {
        converter.fromString(String.class, "12");
    }

    @Test
    public void fromString_validValues() {
        Object value = converter.fromString(Integer.class, "2012");
        assertThat(value).isNotNull().isInstanceOf(Integer.class).isEqualTo(2012);
    }

    @Test
    public void fromString_validValues_subType() {
        Object value = converter.fromString(Number.class, "2012");
        assertThat(value).isNotNull().isInstanceOf(Integer.class).isEqualTo(2012);
    }

    @Test(expected = SpecItRuntimeException.class)
    public void fromString_invalidValues() {
        converter.fromString(Integer.class, "2012/a07/09");
    }

    @Test
    public void suggest() {
        assertThat(converter.suggest("20")).contains("20");
        assertThat(converter.suggest("2011")).contains("2011");
        assertThat(converter.suggest("20aa")).isEmpty();
        assertThat(converter.suggest("2011/08/")).isEmpty();
    }
}
