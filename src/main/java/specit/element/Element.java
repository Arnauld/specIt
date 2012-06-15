package specit.element;

/**
 *
 */
public abstract class Element {
    private final RawPart rawPart;

    Element() {
        this(null);
    }

    public Element(RawPart rawPart) {
        this.rawPart = rawPart;
    }

    public RawPart getRawPart() {
        return rawPart;
    }

    public boolean hasRawPart() {
        return rawPart != null;
    }

    public abstract void traverse(ElementVisitor visitor);

}
