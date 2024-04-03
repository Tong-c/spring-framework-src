package com.wsh.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

// @Aspect：告诉Spring这是一个切面类
@Aspect
@Component
public class SimpleAspect {

	// 定义切入点表达式
	@Pointcut("execution(* com.wsh.aop.service..*.*(..))")
	private void pointcut() {
	}

	// 环绕通知
	@Around("pointcut()")
	public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Exception {
		String methodName = proceedingJoinPoint.getSignature().getName();
		System.out.println("执行" + methodName + "的环绕通知(@Around)开始...");
		try {
			long startTime = System.currentTimeMillis();
			// 执行目标方法
			Object result = proceedingJoinPoint.proceed();
			long endTime = System.currentTimeMillis();
			System.out.println(methodName + "()方法耗时: " + (endTime - startTime) + "毫秒");
			return result;
		} catch (Throwable throwable) {
			throwable.printStackTrace();
			throw new Exception("方法执行发生异常");
		}
	}

//	// 前置通知
//	@Before("pointcut()")
//	public void before(JoinPoint joinPoint) {
//		String methodName = joinPoint.getSignature().getName();
//		System.out.println("执行" + methodName + "的前置通知(@Before)...");
//	}
//
//	// 后置通知
//	@After("pointcut()")
//	public void after(JoinPoint joinPoint) {
//		String methodName = joinPoint.getSignature().getName();
//		System.out.println("执行" + methodName + "的后置通知(@After)...");
//	}
//
//	// 返回通知
//	@AfterReturning("pointcut()")
//	public void afterReturning(JoinPoint joinPoint) {
//		String methodName = joinPoint.getSignature().getName();
//		System.out.println("执行" + methodName + "的后置返回通知(@AfterReturning)...");
//	}
//
//	// 异常返回通知
//	@AfterThrowing("pointcut()")
//	public void AfterThrowing(JoinPoint joinPoint) {
//		String methodName = joinPoint.getSignature().getName();
//		System.out.println("执行" + methodName + "的异常返回通知(@AfterThrowing)...");
//	}

}
