package specit.element;

/**
 *
 *
 */
public class Scenario extends ExecutablePart {
    public Scenario(RawPart rawPart) {
        super(rawPart);
    }

    @Override
    public void traverse(ElementVisitor visitor) {
        visitor.beginScenario(this);
        super.traverseExecutablePart(visitor);
        visitor.endScenario(this);
    }
}
