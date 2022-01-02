package com.example;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class MyAspect {

	@Around("""
			@within(com.example.MyAnnotation)
			&& (execution(* *.*(..)) || execution(* .new(..)))
			""")
	public Object intercept(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		System.out.println("********** InvocationAspect intercepted.");
		var returnValue = proceedingJoinPoint.proceed();
		System.out.println("********** InvocationAspect finished.");

		return returnValue;
	}
}
