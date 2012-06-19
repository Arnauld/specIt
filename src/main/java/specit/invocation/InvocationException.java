package specit.invocation;

import specit.SpecItException;
import specit.SpecItRuntimeException;

/**
 *
 */
public class InvocationException extends SpecItRuntimeException {
    public InvocationException() {
    }

    public InvocationException(String s) {
        super(s);
    }

    public InvocationException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public InvocationException(Throwable throwable) {
        super(throwable);
    }
}
