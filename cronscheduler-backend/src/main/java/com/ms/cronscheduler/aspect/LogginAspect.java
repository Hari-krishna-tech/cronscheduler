package com.ms.cronscheduler.aspect;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogginAspect {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Before("execution(* com.ms.cronscheduler.controller.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        log.info("Entering method: {} with argumets: {}",
                joinPoint.getSignature().toShortString(), joinPoint.getArgs());


    }

    @AfterReturning(pointcut = "execution(* com.ms.cronscheduler.controller.*.*(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("Exiting method: {} with result: {}", joinPoint.getSignature().toShortString(), result);
    }




}
