package com.ps.inventoryservice.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LoggingAndPerformanceAspect {

    //    @Around("execution(* com.ps.money_manager_backend.*.service..*.*(..))")
    @Around("execution(* com.ps.inventoryservice.service.*.*(..))")
    public Object logAndMeasureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        long startTime = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().toShortString();

        log.info("➡️ Entering {}", methodName);
        log.info("📥 Arguments: {}", Arrays.toString(joinPoint.getArgs()));

        try {
            Object result = joinPoint.proceed();

            log.info("✅ Exiting {}", methodName);
            return result;

        } catch (Exception ex) {
            log.error("❌ Exception in {}", methodName, ex);
            throw ex;

        } finally {
            long executionTime = System.currentTimeMillis() - startTime;
            log.info("⏱ Execution time: {} ms", executionTime);
        }
    }
}
