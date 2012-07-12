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

    @Test(expected = ConverterException.class)
    public void getConverterForType_none() throws ConverterException {
        registry.getConverterForType(TimeUnit.class);
    }

    @Test(expected = ConverterException.class)
    public void getConverterForType_missing() throws ConverterException {
        registry.registerConverter(new IntegerConverter());

        registry.getConverterForType(TimeUnit.class);
    }

    @Test
    public void getConverter_instanciateOnDemand() throws ConverterException {
        Converter c1 = registry.getConverter(InternalConverter.class);
        assertThat(c1).isNotNull().isInstanceOf(InternalConverter.class);

        Converter c2 = registry.getConverter(InternalConverter.class);
        assertThat(c2).isSameAs(c1);
    }

    @Test(expected = ConverterException.class)
    public void getConverter_failGracefullyOnInstanciationFailure() throws ConverterException {
        registry.getConverter(InternalConverterWithPrivateCtor.class);
    }

    public static class InternalConverter implements Converter {
        @Override
        public boolean canConvertTo(Class<?> requiredType) {
            return true;
        }

        @Override
        public Object fromString(Class<?> requiredType, String value) {
            return null;
        }

        @Override
        public String[] suggest(String input) {
            return new String[0];
        }
    }

    public static class InternalConverterWithPrivateCtor implements Converter {
        private InternalConverterWithPrivateCtor() {
        }

        @Override
        public boolean canConvertTo(Class<?> requiredType) {
            return true;
        }

        @Override
        public Object fromString(Class<?> requiredType, String value) {
            return null;
        }

        @Override
        public String[] suggest(String input) {
            return new String[0];
        }
    }
}
