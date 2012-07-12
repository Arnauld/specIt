package specit.invocation.converter;

import specit.SpecItRuntimeException;
import specit.element.Table;
import specit.invocation.Converter;
import specit.parser.TableParser;

/**
 * Convert a <code>String</code> into {@link Table} based on configured {@link Table} definition settings.
 *
 */
public class TableConverter implements Converter {

    private final TableParser tableParser;

    public TableConverter(TableParser tableParser) {
        this.tableParser = tableParser;
    }

    @Override
    public boolean canConvertTo(Class<?> requiredType) {
        return (requiredType != null)
                && requiredType.isAssignableFrom(Table.class);
    }

    @Override
    public Object fromString(Class<?> requiredType, String value) {
        if (requiredType == null) {
            throw new SpecItRuntimeException("RequiredType <null> is not assignable from Table");
        }
        if(!requiredType.isAssignableFrom(Table.class)) {
            throw new SpecItRuntimeException("Incompatible required type, expected assignable from Table");
        }
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
