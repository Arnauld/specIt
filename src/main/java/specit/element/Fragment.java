package specit.element;

/**
 *
 *
 */
public class Fragment extends ExecutablePart {
    public Fragment(RawPart rawPart) {
        super(rawPart);
    }

    @Override
    public void traverse(ElementVisitor visitor) {
        if(visitor.beginFragment(this))
            traverseExecutablePart(visitor);
        visitor.endFragment(this);
    }

    @Override
    public void addStep(Step step) {
        super.addStep(step);
        checkForFragmentTermination(step);
    }

    private void checkForFragmentTermination(Step step) {
        if(step.endsWithBlankLine()) {
            Element parent = getParent();
            if(parent instanceof FragmentHolder)
                ((FragmentHolder)parent).fragmentEnds();
        }
    }

    @Override
    public void addExemple(Example example) {
        throw new IllegalArgumentException("Cannot add Example to Fragment");
    }

    @Override
    public void addFragment(Fragment fragment) {
        throw new IllegalArgumentException("Cannot add Fragment to Fragment");
    }

    @Override
    public void fragmentEnds() {
        throw new IllegalArgumentException("Cannot add Fragment to Fragment");
    }

    public String getFragmentReference() {
        String remaining = getRawPart().contentAfterAlias();
        return remaining.trim();
    }
}
