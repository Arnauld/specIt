package specit.element;

/**
 *
 */
public class DefaultExecutablePart extends ExecutablePart {
    public DefaultExecutablePart() {
        super();
    }

    @Override
    public void traverse(ElementVisitor visitor) {
        visitor.beginDefaultExecutablePart(this);
        traverseExecutablePart(visitor);
        visitor.endDefaultExecutablePart(this);
    }
}
