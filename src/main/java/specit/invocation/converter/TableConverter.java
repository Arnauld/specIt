package specit.invocation.converter;

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
        return null;
    }

    @Override
    public String[] suggest(String input) {
        return new String[0];
    }
}
