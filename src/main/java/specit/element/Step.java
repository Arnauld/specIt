package specit.element;

/**
 *
 */
public class Step extends ExecutablePart {
    public Step(RawPart rawPart) {
        super(rawPart);
    }

    @Override
    public void traverse(ElementVisitor visitor) {
        visitor.beginStep(this);
        visitor.endStep(this);
    }
}
