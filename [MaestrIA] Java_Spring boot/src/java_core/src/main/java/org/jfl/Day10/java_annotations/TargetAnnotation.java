package org.jfl.Day10.java_annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface TargetAnnotation {
}
