package specit.invocation;

import specit.SpecItException;

/**
 *
 *
 */
public class ConverterException extends SpecItException {
    public ConverterException() {
    }

    public ConverterException(String s) {
        super(s);
    }

    public ConverterException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ConverterException(Throwable throwable) {
        super(throwable);
    }
}
