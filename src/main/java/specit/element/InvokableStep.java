package specit.element;

/**
 *
 *
 */
public class InvokableStep {
    private final Step underlying;
    private final String adjustedInput;

    public InvokableStep(Step underlying, String adjustedInput) {
        this.underlying = underlying;
        this.adjustedInput = adjustedInput;
    }

    public Step getUnderlying() {
        return underlying;
    }

    public String getAdjustedInput() {
        return adjustedInput;
    }

    public Keyword getKeyword() {
        return getUnderlying().getKeyword();
    }
}
