package specit.invocation;

import specit.SpecItRuntimeException;

/**
 *
 *
 */
public class ParameterMappingRuntimeException extends SpecItRuntimeException {
    public ParameterMappingRuntimeException() {
    }

    public ParameterMappingRuntimeException(String s) {
        super(s);
    }

    public ParameterMappingRuntimeException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ParameterMappingRuntimeException(Throwable throwable) {
        super(throwable);
    }
}
