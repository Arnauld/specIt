package specit.element;

import specit.util.New;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 *
 *
 */
public class Example extends Element {

    public Example(RawPart rawPart) {
        super(rawPart);
    }

    @Override
    public void traverse(ElementVisitor visitor) {
        visitor.beginExample(this);
        visitor.endExample(this);
    }

    public List<Map<String, String>> getVariablesRows() {
        return getRawPart().getVariablesRows();
    }
}