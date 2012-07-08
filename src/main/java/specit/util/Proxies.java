package specit.util;

import specit.SpecItRuntimeException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;

/**
 *
 *
 */
public final class Proxies {

    private Proxies() {
    }

    public static <T> T proxyDispatch(final List<T> impls, Class<T> type) {
        return (T) Proxy.newProxyInstance(
                Proxies.class.getClassLoader(),
                new Class<?>[]{type},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object o, Method method, Object[] arguments) {
                        Object last = null;
                        for (T impl : impls) {
                            try {
                                last = method.invoke(impl, arguments);
                            }
                            catch (IllegalAccessException e) {
                                throw new SpecItRuntimeException(
                                        "Dispatch failed {" + method + "(" + Arrays.toString(arguments) + ")}", e);
                            }
                            catch (InvocationTargetException e) {
                                throw new SpecItRuntimeException(
                                        "Dispatch failed {" + method + "(" + Arrays.toString(arguments) + ")}", e);
                            }
                        }
                        return last;
                    }
                });
    }

    public static <T> T proxyNoOp(Class<T> type) {
        return (T) Proxy.newProxyInstance(
                Proxies.class.getClassLoader(),
                new Class<?>[]{type},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object o, Method method, Object[] arguments) {
                        return null;
                    }
                });
    }

}
