package specit.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 *
 *
 */
public class Proxies {

    public static <T> T proxyDispatch(final List<T> impls, Class<T> type) {
        return (T)Proxy.newProxyInstance(Proxies.class.getClassLoader(), new Class<?>[]{type}, new InvocationHandler() {
            @Override
            public Object invoke(Object o, Method method, Object[] arguments) throws Throwable {
                Object last = null;
                for(T impl : impls)
                    last = method.invoke(impl, arguments);
                return last;
            }
        });
    }

    public static <T> T proxyNoOp(Class<T> type) {
        return (T)Proxy.newProxyInstance(Proxies.class.getClassLoader(), new Class<?>[]{type}, new InvocationHandler() {
            @Override
            public Object invoke(Object o, Method method, Object[] arguments) throws Throwable {
                return null;
            }
        });
    }

}
