package specit.element;

/**
 *
 */
public class Background extends ExecutablePart {

    public Background(RawPart rawPart) {
        super(rawPart);
    }

    @Override
    public void traverse(ElementVisitor visitor) {
        visitor.beginBackground(this);
        traverseExecutablePart(visitor);
        visitor.endBackground(this);
    }

}
