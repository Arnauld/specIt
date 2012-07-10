package specit.element;

/**
 *
 */
public class Background extends ExecutablePart {

    public Background(RawElement rawElement) {
        super(rawElement);
    }

    @Override
    public void traverse(ElementVisitor visitor) {
        if (visitor.beginBackground(this)) {
            traverseExecutablePart(visitor);
        }
        visitor.endBackground(this);
    }

}
