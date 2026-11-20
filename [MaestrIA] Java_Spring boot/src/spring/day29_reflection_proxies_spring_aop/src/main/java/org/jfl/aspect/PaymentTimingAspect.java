package org.jfl.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PaymentTimingAspect {
    private static final Logger log = LoggerFactory.getLogger(PaymentTimingAspect.class);

    @Around("execution(* org.jfl.service.PaymentProcessingService.process(..))")
    //@Around("execution(* org.jfl.service.PaymentProcessingService.foo(..))")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.nanoTime();
        Object[] args = joinPoint.getArgs();

        for (Object arg : args) {
            log.debug("Argument={}", arg);
        }

        log.debug("Target={}", joinPoint.getTarget().getClass().getSimpleName());
        log.debug("Method={}", joinPoint.getSignature().getName());

        try {
            //return joinPoint.proceed();
            return "INTERCEPTED";
        } finally {
            long elapsed = System.nanoTime() - start;
            log.info("Method={} executionTime={}ns", joinPoint .getSignature() .getName(), elapsed);
        }
    }
}