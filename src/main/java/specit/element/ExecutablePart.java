package specit.element;

import specit.util.New;

import java.util.List;

/**
 *
 *
 */
public abstract class ExecutablePart extends Element {

    private List<ExecutablePart> executableParts = New.arrayList();
    private List<Example> examples = New.arrayList();
    private List<Require> requires = New.arrayList();

    ExecutablePart() {
    }

    public ExecutablePart(RawPart rawPart) {
        super(rawPart);
    }

    public void traverseExecutablePart(ElementVisitor visitor) {
        for(Require require: requires) {
            require.traverse(visitor);
        }
        for(Example example: examples) {
            example.traverse(visitor);
        }
        for(ExecutablePart part: executableParts) {
            part.traverse(visitor);
        }
    }

    public void addStep(Step step) {
        executableParts.add(step);
    }

    public void addExemple(Example example) {
        examples.add(example);
    }

    public void addRequire(Require require) {
        requires.add(require);
    }

    public boolean hasContent() {
        return hasExample() || hasExecutablePart() || hasRequire();
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
}
