package mini_lab.day29;

import java.lang.reflect.Proxy;


public class Day29 {

    public static void main(String[] args) {
        Ping p = (Ping)Proxy.newProxyInstance(
                                Day29.class.getClassLoader(),
                                new Class[]{Ping.class},
                                (
                                        proxy,
                                        method,
                                        methodArgs
                                ) -> {
                                    System.out.println( "Method: " + method.getName());
                                    return "intercepted";
                                }
                        );

        System.out.println(p.ping());

        System.out.println("\n");

        PaymentProcessor target = new DefaultPaymentProcessor();
        PaymentProcessor proxy2 = (PaymentProcessor)Proxy.newProxyInstance(
                                PaymentProcessor.class.getClassLoader(),
                                new Class[]{PaymentProcessor.class},
                                (
                                        proxyObject,
                                        method,
                                        arguments
                                ) -> {
                                    System.out.println("Before method");
                                    Object result = method.invoke(target, arguments);
                                    System.out.println("After method");
                                    return result;
                                }
                        );

        String result = proxy2.process(42L);
        System.out.println("\n");

        PaymentProcessor proxy3 = (PaymentProcessor)Proxy.newProxyInstance(
                                                                            PaymentProcessor.class.getClassLoader(),
                                                                            new Class[]{PaymentProcessor.class},
                                                                            (
                                                                                proxyObject,
                                                                                method,
                                                                                arguments
                                                                            ) -> {
                                                                                 long start = System.nanoTime();
                                                                                 try {
                                                                                      return method.invoke(target, arguments);
                                                                                 } finally {
                                                                                       long elapsed = System.nanoTime() - start;
                                                                                        System.out.println(method.getName() + " took " + elapsed + " ns");
                                                                                 }
                                }
                        );

        proxy3.process(42L);

        System.out.println("\n "+proxy3.getClass());
        System.out.println(proxy3 instanceof PaymentProcessor);

    }
}

