package specit.element;

/**
 *
 */
public abstract class Element {
    private final RawPart rawPart;
    private Element parent;

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

    void setParent(Element parent) {
        this.parent = parent;
    }

    public Element getParent() {
        return parent;
    }

    public boolean endsWithBlankLine() {
        return rawPart.endsWithBlankLine();
    }

    public Fragment findFragment(String fragmentRef, boolean searchInChildren) {
        return null;
    }
}
