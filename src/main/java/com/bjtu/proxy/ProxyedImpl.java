package com.bjtu.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


interface  Proxyed {
    public int getInt();
}
/**
 * Created by lids on 15/12/5.
 */
public class ProxyedImpl  implements Proxyed {

    public ProxyedImpl() {

    }

    int i = 0;
    public int getInt() {
        System.out.println("wroking");
        return i++;
    }

    public static void main(String[] args) {


        Class<?>[] interfaces = new Class[] {Proxyed.class};

        ProxyInvocationHandler proxyInvocationHandler = new ProxyInvocationHandler(new ProxyedImpl());

        Proxyed result = (Proxyed) Proxy.newProxyInstance(ProxyedImpl.class.getClassLoader(), interfaces, proxyInvocationHandler);
        for (int a = 0;a < 10;a++) {
            System.out.println("allok " + result.getInt());
        }
    }
}

class ProxyInvocationHandler implements InvocationHandler {
    Object o;
    public ProxyInvocationHandler (Object c) {
        this.o = c;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//        if (proxy instanceof ProxyedImpl) {
        System.out.println("start");
            Object a =  method.invoke(o, args);
        System.out.println("end");
        return a;
//        } else {
//            return 0;
//        }
    }

}

