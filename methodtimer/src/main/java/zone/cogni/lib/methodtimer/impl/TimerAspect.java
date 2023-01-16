package zone.cogni.lib.methodtimer.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.support.AopUtils;
import zone.cogni.lib.methodtimer.TimedMethod;
import zone.cogni.lib.methodtimer.TimerName;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.SortedSet;

@Aspect
@RequiredArgsConstructor
public class TimerAspect {
  private static final int nameBuilderInitSize = 40;
  private final TimerHolder timerHolder;
  private final TimerReport timerReport;

  @SuppressWarnings("ProhibitedExceptionDeclared")
  @Around("@annotation(timedMethod)")
  public Object trace(ProceedingJoinPoint joinPoint, TimedMethod timedMethod) throws Throwable {
    String name = getName(joinPoint, timedMethod);
    boolean initDone = timerHolder.initForThread();
    long startTime = System.currentTimeMillis();
    try {
      return joinPoint.proceed();
    }
    finally {
      timerHolder.registerTime(name, System.currentTimeMillis() - startTime);
      if (initDone) logAndCleanTimer();
    }
  }

  private void logAndCleanTimer() {
    SortedSet<MethodTime> times = timerHolder.getTimes();
    timerHolder.clearTimes();
    timerReport.report(times);
  }

  private String getName(ProceedingJoinPoint joinPoint, TimedMethod timedMethod) {
    boolean hasName = StringUtils.isNotBlank(timedMethod.value());

    StringBuilder nameBuilder = new StringBuilder(nameBuilderInitSize);
    Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
    if (hasName) {
      nameBuilder.append(timedMethod.value());
    }
    else {
      Object pjpObject = joinPoint.getThis();
      Class<?> clazz = null == pjpObject ? method.getDeclaringClass() : AopUtils.getTargetClass(pjpObject);
      String simpleName = clazz.getSimpleName();
      if (StringUtils.isBlank(simpleName)) simpleName = StringUtils.substringAfterLast(clazz.getName(), ".");
      nameBuilder.append(simpleName).append('.').append(method.getName());
    }

    Object[] args = joinPoint.getArgs();
    Annotation[][] parameterAnnotations = method.getParameterAnnotations();
    for (int i = 0; i < parameterAnnotations.length; i++) {
      Annotation[] parameterAnnotation = parameterAnnotations[i];
      boolean hasNoTimerName = Arrays.stream(parameterAnnotation).map(Annotation::annotationType).noneMatch(annotationClazz -> annotationClazz.equals(TimerName.class));
      if (hasNoTimerName) continue;

      nameBuilder.append('-').append(args[i]);
    }

    return nameBuilder.toString();
  }

}
