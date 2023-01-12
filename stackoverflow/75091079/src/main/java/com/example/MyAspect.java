package com.example;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class MyAspect {

  @AfterReturning("@within(com.example.ShouldBeWoven) && execution(* .new(..))")
  public void myAdvice(JoinPoint joinPoint) {
    System.out.println("******* MyAspect wove: " + joinPoint.getSignature().getDeclaringTypeName());
  }

}
