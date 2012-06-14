package specit.element;

/**
 *
 *
 */
public class Forall extends Element {

    public Forall(RawPart rawPart) {
        super(rawPart);
    }

    @Override
    public void traverse(ElementVisitor visitor) {
        visitor.beginForall(this);
        visitor.endForall(this);
    }

    public Table getForallTable() {
        return getRawPart().getForallTable();
    }
}
