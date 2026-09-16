package org.jfl.Day10.java_annotations;

@MyAnnotation
@Audit(action = "CREATE_CUSTOMER")
@Role("ADMIN") //you can omit the value when having "value" on the @interface @Role(value = "ADMIN")
// @TargetAnnotation() - compilation error
public class PaymentService {
    @TargetAnnotation()
    public void process(){};
}
