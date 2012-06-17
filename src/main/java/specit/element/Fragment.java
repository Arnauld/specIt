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
        visitor.beginFragment(this);
        super.traverseExecutablePart(visitor);
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
            if(parent!=null && parent instanceof FragmentHolder)
                ((FragmentHolder)parent).fragmentEnds();
        }
    }

    @Override
    public void addRequire(Require require) {
        super.addRequire(require);    //To change body of overridden methods use File | Settings | File Templates.
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
}
