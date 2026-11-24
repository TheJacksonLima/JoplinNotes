package org.jfl.utils;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class Day30Config {

    @Bean
    CommandLineRunner names(org.springframework.context.ApplicationContext ctx) {
        return args ->
        {
            System.out.println("Bean count=" + ctx.getBeanDefinitionCount());
            String[] names = ctx.getBeanDefinitionNames();
            for (String name : names) {
                System.out.println(name);
            }
        };
    }
}