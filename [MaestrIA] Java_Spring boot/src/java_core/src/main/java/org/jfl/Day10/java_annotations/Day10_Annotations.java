package org.jfl.Day10.java_annotations;

public class Day10_Annotations {
    public static void main(String[] args)
            throws Exception {

        var method = CustomerService.class
                .getMethod("createCustomer");

        Audit audit =
                method.getAnnotation(Audit.class);

        System.out.println(audit.action());
    }
}
