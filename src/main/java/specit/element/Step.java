package specit.element;

/**
 *
 */
public class Step extends ExecutablePart {
    public Step(RawElement rawElement) {
        super(rawElement);
    }

    @Override
    public void traverse(ElementVisitor visitor) {
        visitor.beginStep(this);
        visitor.endStep(this);
    }

    public String toString() {
        return "Step {" + getRawElement().rawContent().replace("\n", "\\n") + "}";
    }
}
