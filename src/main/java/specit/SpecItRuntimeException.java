package specit;

/**
 *
 */
public class SpecItRuntimeException extends RuntimeException {
    public SpecItRuntimeException() {
    }

    public SpecItRuntimeException(String s) {
        super(s);
    }

    public SpecItRuntimeException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public SpecItRuntimeException(Throwable throwable) {
        super(throwable);
    }
}
