package specit.element;

import specit.SpecItRuntimeException;

/**
 *
 */
public class InvalidElementDefinitionException extends SpecItRuntimeException {
    public InvalidElementDefinitionException() {
    }

    public InvalidElementDefinitionException(String s) {
        super(s);
    }

    public InvalidElementDefinitionException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public InvalidElementDefinitionException(Throwable throwable) {
        super(throwable);
    }
}
