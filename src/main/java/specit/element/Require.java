package specit.element;

/**
 */
public class Require extends Element {
    public Require(RawPart rawPart) {
        super(rawPart);
    }

    @Override
    public void traverse(ElementVisitor visitor) {
        visitor.beginRequire(this);
        visitor.endRequire(this);
    }
}
