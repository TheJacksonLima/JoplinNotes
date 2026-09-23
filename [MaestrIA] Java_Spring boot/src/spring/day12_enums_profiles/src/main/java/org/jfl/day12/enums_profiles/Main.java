package org.jfl.day12.enums_profiles;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@SpringBootApplication
public class Main {
    public static void main(String[] args) throws Exception {
        var ctx = SpringApplication.run(Main.class, args);
        ctx.getBean(PaymentService.class).process();

    }
}
