package specit.element;

import specit.util.New;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 *
 *
 */
public abstract class ExecutablePart extends Element implements FragmentHolder {

    private List<ExecutablePart> executableParts = New.arrayList();
    private List<Example> examples = New.arrayList();
    private List<Require> requires = New.arrayList();
    private List<Fragment> fragments = New.arrayList();
    //
    private transient Stack<ExecutablePart> currentExecutablePart = New.stack();

    ExecutablePart() {
        currentExecutablePart.push(this);
    }

    public ExecutablePart(RawPart rawPart) {
        super(rawPart);
        currentExecutablePart.push(this);
    }

    @Override
    public Fragment findFragment(String fragmentRef, boolean searchInChildren) {
        for(Fragment fragment : fragments) {
            if(fragmentRef.equalsIgnoreCase(fragment.getFragmentReference()))
                return fragment;
        }

        if(searchInChildren) {
            for (ExecutablePart part : executableParts) {
                Fragment fragment = part.findFragment(fragmentRef, searchInChildren);
                if(fragment!=null)
                    return fragment;
            }
        }
        return null;
    }

    public void traverseExecutablePart(ElementVisitor visitor) {
        for(Fragment fragment : fragments) {
            fragment.traverse(visitor);
        }
        for (Require require : requires) {
            require.traverse(visitor);
        }
        for (Example example : examples) {
            example.traverse(visitor);
        }
        for (ExecutablePart part : executableParts) {
            part.traverse(visitor);
        }
    }

    /**
     * Add a step to the current executable part held by this one:
     * either this one, or a nested Fragment.
     *
     * @param step
     */
    public void addStep(Step step) {
        ExecutablePart executablePart = currentExecutablePart.peek();
        if(executablePart!=this) {
            executablePart.addStep(step);
        }
        else {
            executableParts.add(step);
            step.setParent(this);
        }
    }

    public void addExemple(Example example) {
        ExecutablePart executablePart = currentExecutablePart.peek();
        if(executablePart!=this) {
            executablePart.addExemple(example);
        }
        else {
            examples.add(example);
            example.setParent(this);
        }

    }

    public void addRequire(Require require) {
        ExecutablePart executablePart = currentExecutablePart.peek();
        if(executablePart!=this) {
            executablePart.addRequire(require);
        }
        else {
            requires.add(require);
            require.setParent(this);
        }
    }

    public boolean hasContent() {
        return hasExample() ||  hasExecutablePart() || hasRequire() || hasFragment();
    }

    private boolean hasExecutablePart() {
        return !executableParts.isEmpty();
    }

    private boolean hasExample() {
        return !examples.isEmpty();
    }

    public boolean hasRequire() {
        return !requires.isEmpty();
    }

    public List<ExecutablePart> getExecutableParts() {
        return Collections.unmodifiableList(executableParts);
    }

    public List<Example> getExamples() {
        return Collections.unmodifiableList(examples);
    }

    public List<Require> getRequires() {
        return Collections.unmodifiableList(requires);
    }

    public void addFragment(Fragment fragment) {
        fragments.add(fragment);
        fragment.setParent(this);
        currentExecutablePart.push(fragment);
    }

    private boolean hasFragment() {
        return !fragments.isEmpty();
    }

    @Override
    public void fragmentEnds() {
        if(!(currentExecutablePart.pop() instanceof Fragment))
            throw new InvalidCallException("Current ExecutablePart was not a Fragment!");
    }
}
