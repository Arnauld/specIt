package specit.invocation;

import specit.util.New;

import java.util.Map;

/**
 *
 *
 */
public class InstanceProviderBasic implements InstanceProvider {

    private Map<Class<?>, Object> instancePerType = New.hashMap();

    public Object getInstance(Class<?> owningType) throws InstanceProviderException {
        Object instance = instancePerType.get(owningType);
        if (instance == null) {
            try {
                instance = owningType.newInstance();
            } catch (InstantiationException e) {
                throw new InstanceProviderException("Failed to instanciate instance for step <" + owningType + ">", e);
            } catch (IllegalAccessException e) {
                throw new InstanceProviderException("Failed to instanciate instance for step <" + owningType + ">", e);
            }
            instancePerType.put(owningType, instance);
        }
        return instance;
    }
}
