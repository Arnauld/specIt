package specit.invocation;

import static org.fest.assertions.api.Assertions.assertThat;

import specit.invocation.converter.EnumByNameConverter;
import specit.invocation.converter.IntegerConverter;

import org.junit.Before;
import org.junit.Test;
import java.util.concurrent.TimeUnit;

/**
 *
 *
 */
public class ConverterRegistryTest {

    private ConverterRegistry registry;

    @Before
    public void setUp() {
        registry = new ConverterRegistry();
    }

    @Test
    public void registerConverter_and_getConverterForType() throws ConverterException {
        registry.registerConverter(new EnumByNameConverter());

        Converter converter = registry.getConverterForType(TimeUnit.class);
        Object days = converter.fromString(TimeUnit.class, "DAYS");
        assertThat(days).isNotNull().isInstanceOf(TimeUnit.class).isEqualTo(TimeUnit.DAYS);
    }

    @Test
    public void registerConverters_and_getConverterForType() throws ConverterException {
        registry.registerConverter(new IntegerConverter());
        registry.registerConverter(new EnumByNameConverter());

        Converter converter = registry.getConverterForType(TimeUnit.class);
        Object days = converter.fromString(TimeUnit.class, "DAYS");
        assertThat(days).isNotNull().isInstanceOf(TimeUnit.class).isEqualTo(TimeUnit.DAYS);


        converter = registry.getConverterForType(Number.class);
        Object value = converter.fromString(Number.class, "15");
        assertThat(value).isNotNull().isInstanceOf(Integer.class).isEqualTo(15);
    }
}
