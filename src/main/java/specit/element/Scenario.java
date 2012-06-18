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
        if(visitor.beginScenario(this))
            traverseExecutablePart(visitor);
        visitor.endScenario(this);
    }
}
