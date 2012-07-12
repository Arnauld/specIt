package specit.invocation.converter;

import static org.fest.assertions.api.Assertions.assertThat;

import specit.element.Keyword;

import org.junit.Before;
import org.junit.Test;
import java.util.concurrent.TimeUnit;

/**
 *
 *
 */
public class EnumByNameConverterTest {

    private EnumByNameConverter converter;

    @Before
    public void setUp() {
        converter = new EnumByNameConverter();
    }

    @Test
    public void fromString_validValues_timeUnit() {
        Object value = converter.fromString(TimeUnit.class, "DAYS");
        assertThat(value).isNotNull().isInstanceOf(TimeUnit.class).isEqualTo(TimeUnit.DAYS);
    }

    @Test
    public void fromString_validValues_keyword() {
        Object value = converter.fromString(Keyword.class, "Scenario");
        assertThat(value).isNotNull().isInstanceOf(Keyword.class).isEqualTo(Keyword.Scenario);
    }
}
