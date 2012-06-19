package specit.invocation;

/**
 *
 *
 */
public interface InstanceProvider {

    Object getInstance(Class<?> owningType) throws InstanceProviderException;
}
