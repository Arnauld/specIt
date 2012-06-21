package specit.parser;

import org.junit.Test;
import specit.SpecIt;
import specit.element.RepeatParameters;
import specit.element.Table;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 *
 *
 */
public class RepeatParametersParserTest {

    @Test
    public void parse_withCase() {
        String content = "Repeat [Create Entry] with:\n" +
                "    | name    | severity |\n" +
                "    | to do   |        3 |\n" +
                "    | fix me  |        1 |\n" +
                "    | later   |        4 |\n" +
                "    | rework  |        4 |\n" +
                "    | wording |        5 |";

        RepeatParameters parameters = new RepeatParametersParser(new SpecIt()).parse(content);
        assertThat(parameters, notNullValue());
        assertThat(parameters.hasWithTable(), is(true));

        Table withTable = parameters.getWithTable();
        assertThat(withTable, notNullValue());
        assertThat(withTable.getHeaders(), equalTo(new String[]{"name", "severity"}));
        assertThat(withTable.getRowCount(), equalTo(5));
    }

    @Test
    public void parse_loopCase() {
        String content = "  Repeat [Login] 3 times\n";

        RepeatParameters parameters = new RepeatParametersParser(new SpecIt()).parse(content);
        assertThat(parameters, notNullValue());
        assertThat(parameters.hasWithTable(), is(false));
        assertThat(parameters.hasLoopCount(), is(true));
        assertThat(parameters.getLoopCount(), is(3));


    }
}
