package specit.element;

/**
 *
 */
public abstract class Element {
    private final RawElement rawElement;
    private Element parent;

    Element() {
        this(null);
    }

    public Element(RawElement rawElement) {
        this.rawElement = rawElement;
    }

    public RawElement getRawElement() {
        return rawElement;
    }

    public boolean hasRawPart() {
        return rawElement != null;
    }

    public abstract void traverse(ElementVisitor visitor);

    void setParent(Element parent) {
        this.parent = parent;
    }

    public Element getParent() {
        return parent;
    }

    public boolean endsWithBlankLine() {
        return rawElement.endsWithBlankLine();
    }

    public Fragment findFragment(String fragmentRef, boolean searchInChildren) {
        return null;
    }

    public String getKeywordAlias() {
        return getRawElement().getKeywordAlias();
    }

    public Keyword getKeyword() {
        return getRawElement().getKeyword();
    }
}
