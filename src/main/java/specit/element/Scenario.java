package specit.element;

/**
 *
 *
 */
public class Scenario extends ExecutablePart {
    public Scenario(RawElement rawElement) {
        super(rawElement);
    }

    @Override
    public void traverse(ElementVisitor visitor) {
        if (visitor.beginScenario(this)) {
            traverseExecutablePart(visitor);
        }
        visitor.endScenario(this);
    }
}
