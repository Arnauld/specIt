package specit;

/**
 *
 *
 */
public class SpecItException extends Exception {
    public SpecItException() {
    }

    public SpecItException(String s) {
        super(s);
    }

    public SpecItException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public SpecItException(Throwable throwable) {
        super(throwable);
    }
}
