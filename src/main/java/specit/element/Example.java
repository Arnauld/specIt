package specit.element;

/**
 *
 *
 */
public class Example extends Element {

    public Example(RawElement rawElement) {
        super(rawElement);
    }

    @Override
    public void traverse(ElementVisitor visitor) {
        visitor.beginExample(this);
        visitor.endExample(this);
    }

    public Table getExampleTable() {
        return getRawElement().getExampleTable();
    }
}
