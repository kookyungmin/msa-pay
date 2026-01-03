package net.happykoo.common.logging;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {

  private final LoggingProducer loggingProducer;

  @Before("execution(* net.happykoo.*.adapter.in.web.*.*(..))")
  public void beforeMethodExecution(JoinPoint joinPoint) {
    String methodName = joinPoint.getSignature().getName();
    loggingProducer.sendMessage("logging", "before executing method : " + methodName);
  }
}
