package specit.invocation;

import specit.SpecItException;

/**
 *
 *
 */
public class ParameterMappingException extends SpecItException {
    public ParameterMappingException() {
    }

    public ParameterMappingException(String s) {
        super(s);
    }

    public ParameterMappingException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ParameterMappingException(Throwable throwable) {
        super(throwable);
    }
}
