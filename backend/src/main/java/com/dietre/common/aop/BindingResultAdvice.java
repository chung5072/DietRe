package com.dietre.common.aop;

import com.dietre.common.exception.BindingException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Aspect
@Component
public class BindingResultAdvice {
    @Pointcut("execution(* com.dietre.api.controller.UserController.registerUserInfo(..))" +
            "|| execution(* com.dietre.api.controller.UserController.updateUserInfo(..))")
    public void pointCut() {

    }

    @Around("pointCut()")
    public Object ValidateHandler(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();

        for (Object arg : args) {
            if (arg instanceof BindingResult) {
                BindingResult bindingResult = (BindingResult)arg;
                if (bindingResult.hasErrors()) {
                    throw new BindingException();
                }
            }
        }


        return joinPoint.proceed();
    }
}