package org.jfl.Day11.spring_reflection;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Audit {

    String action();

    boolean enabled() default true;
}