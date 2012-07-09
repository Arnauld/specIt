package specit.invocation.converter;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.joda.time.chrono.GregorianChronology.getInstanceUTC;

import specit.SpecItRuntimeException;
import specit.util.Time;

import org.joda.time.Chronology;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.chrono.GregorianChronology;
import org.junit.Before;
import org.junit.Test;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 *
 */
public class DateConverterTest {

    private DateConverter converter;

    @Before
    public void setUp () {
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        converter = new DateConverter(df);
    }

    @Test(expected = SpecItRuntimeException.class)
    public void fromString_nullValue() {
        converter.fromString(null);
    }

    @Test
    public void fromString_validValues() {
        Object value = converter.fromString("2012/07/09");

        assertThat(value).isNotNull().isInstanceOf(Date.class);
        LocalDate date = new LocalDate(((Date)value).getTime());
        assertThat(date.getDayOfMonth()).isEqualTo(9);
        assertThat(date.getMonthOfYear()).isEqualTo(7);
        assertThat(date.getYear()).isEqualTo(2012);
    }

    @Test(expected = SpecItRuntimeException.class)
    public void fromString_invalidValues() {
        converter.fromString("2012/a07/09");
    }

    @Test
    public void suggest() {
        LocalDateTime dateTime = new LocalDateTime(2012, 7, 9, 20, 49, 17, 3, getInstanceUTC());
        Time.timeFreeze(dateTime.toDate().getTime());

        assertThat(converter.suggest("20")).contains("2012/07/09");
        assertThat(converter.suggest("2011")).contains("2011/07/09");
        assertThat(converter.suggest("2011/08/")).contains("2011/08/09");
        assertThat(converter.suggest("2011/08/17")).contains("2011/08/17");
        assertThat(converter.suggest("20aa")).isEmpty();
        assertThat(converter.suggest("2011/08/09/0")).isEmpty();
    }
}
