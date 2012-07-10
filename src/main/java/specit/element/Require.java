package specit.element;

/**
 */
public class Require extends Element {
    public Require(RawElement rawElement) {
        super(rawElement);
    }

    @Override
    public void traverse(ElementVisitor visitor) {
        visitor.beginRequire(this);
        visitor.endRequire(this);
    }
}
