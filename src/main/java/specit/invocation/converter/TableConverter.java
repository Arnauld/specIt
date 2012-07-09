package specit.invocation.converter;

import specit.SpecItRuntimeException;
import specit.invocation.Converter;
import specit.parser.TableParser;

/**
 *
 *
 */
public class TableConverter implements Converter {

    private final TableParser tableParser;

    public TableConverter(TableParser tableParser) {
        this.tableParser = tableParser;
    }

    @Override
    public Object fromString(String value) {
        if (value == null) {
            throw new SpecItRuntimeException("Value <null> is not a valid table value");
        }
        return tableParser.parse(value);
    }

    @Override
    public String[] suggest(String input) {
        return new String[0];
    }
}
