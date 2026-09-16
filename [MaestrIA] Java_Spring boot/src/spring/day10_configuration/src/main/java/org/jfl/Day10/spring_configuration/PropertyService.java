package org.jfl.Day10.spring_configuration;

public class PropertyService {

    private final PropertyFormatter formatter;

    public PropertyService(
            PropertyFormatter formatter) {

        this.formatter = formatter;
    }

    public void printProperty() {

        String result =
                formatter.format(
                        "Apartment in Batel",
                        3500.0
                );

        System.out.println(result);
    }
}
