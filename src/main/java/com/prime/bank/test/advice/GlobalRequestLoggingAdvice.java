package com.prime.bank.test.advice;

import com.prime.bank.test.dao.LoggerDAO;
import com.prime.bank.test.model.Logger;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Date;

/**
 * Created by polash
 * on 9/11/18.
 */

@Component
@EnableAspectJAutoProxy
@Aspect
public class GlobalRequestLoggingAdvice {

    @Inject
    private LoggerDAO loggerDAO;

    @Before(value = "execution(* com.prime.bank.test.controller.*.*(..))")
    public void beforeWebMethodExecution(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringType().getName();

        StringBuilder description = new StringBuilder();
        description.append("Before Calling").append(StringUtils.SPACE).append(className).append(StringUtils.SPACE).append(methodName);
        loggerDAO.createLog(Logger.builder()
                .description(description.toString())
                .logTime(new Date())
                .build());

    }

    @After("execution(* com.prime.bank.test.controller.*.*(..))")
    public void afterWebMethodExecution(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringType().getName();

        StringBuilder description = new StringBuilder();
        description.append("after Calling").append(StringUtils.SPACE).append(className).append(StringUtils.SPACE).append(methodName);
        loggerDAO.createLog(Logger.builder()
                .description(description.toString())
                .logTime(new Date())
                .build());
    }


}
