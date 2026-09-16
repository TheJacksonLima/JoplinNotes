package org.jfl.Day10.java_annotations;

class CustomerService {

    @Audit(action = "CREATE_CUSTOMER")
    public void createCustomer() {
        System.out.println("Creating customer");
    }
}