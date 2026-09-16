package org.jfl.Day10.spring_configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public PropertyFormatter propertyFormatter() {

        return new PropertyFormatter();
    }

    @Bean
    public PropertyService propertyService(
            PropertyFormatter formatter) {

        return new PropertyService(formatter);
    }
}
