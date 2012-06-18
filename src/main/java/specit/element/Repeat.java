package specit.element;

/**
 *
 */
public class Repeat extends Step {
    public Repeat(RawPart rawPart) {
        super(rawPart);
    }

    @Override
    public void traverse(ElementVisitor visitor) {
        visitor.beginRepeat(this);
        visitor.endRepeat(this);
    }

    public RepeatParameters getRepeatParameters () {
        return getRawPart().getRepeatParameters();
    }

    public String toString() {
        return "Repeat{" +
                "parameters='" + getRepeatParameters() + '\'' +
                "}";
    }

    public Fragment findParentInStoryTree() {
        String fragmentRef = getRepeatParameters().getReference();

        Element parent = getParent();
        while(parent!=null) {
            Fragment fragment = parent.findFragment(fragmentRef, false);
            if(fragment!=null)
                return fragment;
            parent = parent.getParent();
        }
        return null;
    }
}
