package specit.element;

import java.util.List;
import java.util.Map;

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

    public Table getExampleTable() {
        return getRawPart().getExampleTable();
    }
}
