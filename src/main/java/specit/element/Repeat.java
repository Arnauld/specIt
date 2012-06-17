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
}
