package specit.invocation.converter;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.data.MapEntry.entry;

import specit.SpecIt;
import specit.SpecItRuntimeException;
import specit.element.Table;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 *
 */
public class TableConverterTest {

    private TableConverter converter;

    @Before
    public void setUp () {
        SpecIt specIt = new SpecIt();
        converter = new TableConverter (specIt.tableParser());
    }

    @Test(expected = SpecItRuntimeException.class)
    public void fromString_nullValue() {
        converter.fromString(Table.class, null);
    }

    @Test
    public void parse_validInput() {
        String content = "|<name>|<value>|\n" +
                         "| bob  |     12|\n" +
                         "| alice|   1257|\n";

        Object parsed = converter.fromString(Table.class, content);
        assertThat(parsed).isNotNull().isInstanceOf(Table.class);

        Table table = (Table)parsed;
        assertThat(table.getRowCount()).isEqualTo(2);
        assertThat(table.getRow(0).asMap()).contains(entry("name", "bob"), entry("value", "12"));
        assertThat(table.getRow(1).asMap()).contains(entry("name", "alice"), entry("value", "1257"));
    }

    @Test
    public void parse_invalidInput_noDelimiter() {
        String content = "yuk";

        Object parsed = converter.fromString(Table.class, content);
        assertThat(parsed).isNotNull().isInstanceOf(Table.class);

        Table table = (Table)parsed;
        assertThat(table.getRowCount()).isEqualTo(0);
        assertThat(table.isEmpty()).isTrue();
    }

    @Test
    @Ignore
    public void parse_invalidInput_unbalancedDelimiter() {
        String content = "|<name>|<value>|\n" +
                "| bob  |     12|\n" +
                "| alice|   125";

        Object parsed = converter.fromString(Table.class, content);
        assertThat(parsed).isNotNull().isInstanceOf(Table.class);

        Table table = (Table)parsed;
        assertThat(table.getRowCount()).isEqualTo(2);
        assertThat(table.getRow(0).asMap()).contains(entry("name", "bob"), entry("value", "12"));
        assertThat(table.getRow(1).asMap()).contains(entry("name", "alice"), entry("value", "1257"));
    }

}
