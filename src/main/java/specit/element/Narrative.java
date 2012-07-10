package specit.element;

/**
 *
 */
public class Narrative extends Element {
    public Narrative(RawElement rawElement) {
        super(rawElement);
    }

    @Override
    public void traverse(ElementVisitor visitor) {
        visitor.beginNarrative(this);
        visitor.endNarrative(this);
    }
}
