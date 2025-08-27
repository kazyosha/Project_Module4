package com.codegym.demo.aop;

import ch.qos.logback.classic.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAOP {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(LoggingAOP.class);

    @Pointcut("execution(* com.codegym.demo.controllers..*(..))")
    public void applicationPackagePointcut() {
        // Pointcut for all methods in the application package
    }

    @Before("applicationPackagePointcut()")
    public void logBeforeMethod(JoinPoint joinPoint) {
        logger.info("Entering method: {} with arguments: {}", joinPoint.getSignature(), joinPoint.getArgs());
    }

    @AfterThrowing(pointcut = "applicationPackagePointcut()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        logger.error("Exception in method: {} with cause: {}", joinPoint.getSignature(), ex.getCause() != null ? ex.getCause() : "NULL");
    }
}

