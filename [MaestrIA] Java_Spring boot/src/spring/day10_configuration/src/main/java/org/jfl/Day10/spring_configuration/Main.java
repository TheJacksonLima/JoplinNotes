package org.jfl.Day10.spring_configuration;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {

        ApplicationContext context =
                new AnnotationConfigApplicationContext(
                        AppConfig.class
                );

        PropertyService service =
                context.getBean(
                        PropertyService.class
                );

        service.printProperty();
    }
}
