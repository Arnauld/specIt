package specit.parser;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import specit.Conf;
import specit.element.Example;
import specit.element.Keyword;
import specit.element.RawPart;

import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsMapContaining.hasEntry;

/**
 *
 *
 */
public class ExampleParserTest {

    private Conf conf = new Conf();

    @Test
    public void getVariables() {
        String content = "Example:\n" +
                        "|<name>|<value>|\n" +
                        "| bob  |     12|\n" +
                        "| alice|   1257|\n";
        List<Map<String,String>> variablesRows =
                new ExampleVariablesParser(conf).parseVariablesRows(content);
        assertThat(variablesRows.size(), equalTo(2));

        Map<String, String> row0 = variablesRows.get(0);
        assertThat(row0, hasEntry("name", "bob"));
        assertThat(row0, hasEntry("value", "12"));

        Map<String, String> row1 = variablesRows.get(1);
        assertThat(row1, hasEntry("name", "alice"));
        assertThat(row1, hasEntry("value", "1257"));
    }
}
