package specit.element;

import specit.SpecItRuntimeException;

/**
 *
 */
public class InvalidCallException extends SpecItRuntimeException {
    public InvalidCallException() {
    }

    public InvalidCallException(String s) {
        super(s);
    }

    public InvalidCallException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public InvalidCallException(Throwable throwable) {
        super(throwable);
    }
}
