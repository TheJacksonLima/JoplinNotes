package org.jfl.Day11.spring_reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Main {
    public static void main(String[] args) throws Exception {

        Class<PropertyService> clazz = PropertyService.class;
        PropertyService instance = clazz.getDeclaredConstructor().newInstance();

        // TODO:
        // iterate through methods
        // detect @Audit
        // retrieve annotation
        // print action + enabled

        for(Method method: clazz.getDeclaredMethods()){
            if(method.isAnnotationPresent(Audit.class)){
                Audit audit = method.getAnnotation(Audit.class);
                System.out.println("action: "+audit.action());
                System.out.println("enabled: "+audit.enabled());

                if(audit.enabled()){
                    try {
                        method.invoke(instance);
                    } catch (InvocationTargetException e) {
                        System.out.println("Failed to invoke " + method.getName() + ": " + e.getCause());
                    } catch (IllegalAccessException e) {
                        System.out.println("Cannot access " + method.getName() + ": " + e.getMessage());
                    }
                } else {
                    System.out.println("Skipping " + method.getName() + " (audit disabled)");
                }
            }
        }
    }
}
