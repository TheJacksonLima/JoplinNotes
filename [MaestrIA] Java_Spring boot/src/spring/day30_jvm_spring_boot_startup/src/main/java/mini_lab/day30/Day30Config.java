package mini_lab.day30;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class Day30Config {

    @Bean
    CommandLineRunner names(ApplicationContext ctx) {

        return args -> System.out.println("Bean count=" + ctx.getBeanDefinitionCount());
    }
}