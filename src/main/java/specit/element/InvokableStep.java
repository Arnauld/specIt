package specit.element;

/**
 *
 *
 */
public class InvokableStep {
    private final Step underlying;
    private final String adjustedInput;
    private final String adjustedInputWithoutComment;

    public InvokableStep(Step underlying, String adjustedInput, String adjustedInputWithoutComment) {
        this.underlying = underlying;
        this.adjustedInput = adjustedInput;
        this.adjustedInputWithoutComment = adjustedInputWithoutComment;
    }

    public Step getUnderlying() {
        return underlying;
    }

    public String getAdjustedInputWithoutComment() {
        return adjustedInputWithoutComment;
    }

    public String getAdjustedInput() {
        return adjustedInput;
    }

    public Keyword getKeyword() {
        return getUnderlying().getKeyword();
    }
}
