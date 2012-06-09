package specit.element;

/**
 *
 */
public class Narrative extends Element {
    public Narrative(RawPart rawPart) {
        super(rawPart);
    }

    @Override
    public void traverse(ElementVisitor visitor) {
        visitor.beginNarrative(this);
        visitor.endNarrative(this);
    }
}
