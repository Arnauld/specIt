package specit.invocation;

import specit.SpecItException;

/**
 *
 *
 */
public class InstanceProviderException extends SpecItException {
    public InstanceProviderException() {
    }

    public InstanceProviderException(String s) {
        super(s);
    }

    public InstanceProviderException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public InstanceProviderException(Throwable throwable) {
        super(throwable);
    }
}
