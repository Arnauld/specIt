package specit.parser;

import org.junit.Test;
import specit.Conf;
import specit.element.Table;

import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsMapContaining.hasEntry;

/**
 *
 *
 */
public class TableParserTest {

    private Conf conf = new Conf();

    @Test
    public void getVariables() {
        String content = "Example:\n" +
                        "|<name>|<value>|\n" +
                        "| bob  |     12|\n" +
                        "| alice|   1257|\n";
        Table variablesRows =
                new TableParser(conf).parse(content);
        assertThat(variablesRows.getRowCount(), equalTo(2));

        Map<String, String> row0 = variablesRows.getRow(0).asMap();
        assertThat(row0, hasEntry("name", "bob"));
        assertThat(row0, hasEntry("value", "12"));

        Map<String, String> row1 = variablesRows.getRow(1).asMap();
        assertThat(row1, hasEntry("name", "alice"));
        assertThat(row1, hasEntry("value", "1257"));
    }
}
