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
        if (visitor.beginDefaultExecutablePart(this)) {
            traverseExecutablePart(visitor);
        }
        visitor.endDefaultExecutablePart(this);
    }
}
