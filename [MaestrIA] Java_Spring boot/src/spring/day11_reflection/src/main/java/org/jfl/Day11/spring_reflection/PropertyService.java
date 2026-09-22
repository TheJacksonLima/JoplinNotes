package org.jfl.Day11.spring_reflection;

public class PropertyService {

    @Audit(action = "CREATE_PROPERTY")
    public void createProperty() {

        System.out.println(
                "Property created"
        );
    }

    @Audit(
            action = "DELETE_PROPERTY",
            enabled = false
    )
    public void deleteProperty() {

        System.out.println(
                "Property deleted"
        );
    }

    public void listProperties() {

        System.out.println(
                "Listing properties"
        );
    }
}