package zone.cogni.lib.methodtimer.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.function.FailableSupplier;
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
import java.util.function.Supplier;

@Aspect
@RequiredArgsConstructor
public class TimerAspect implements TimedMethodRunner {
  private static final int nameBuilderInitSize = 40;
  private final TimerHolder timerHolder;
  private final TimerReportReference timerReportReference;

  @SuppressWarnings("ProhibitedExceptionDeclared")
  @Around("@annotation(timedMethod)")
  public Object trace(ProceedingJoinPoint joinPoint, TimedMethod timedMethod) throws Throwable {
    return runTimedMethod(joinPoint::proceed, () -> getName(joinPoint, timedMethod), timedMethod.canStartTimer());
  }

  @Override
  public <T, EX extends Throwable> T runTimedMethod(FailableSupplier<T, EX> supplier, Supplier<String> nameSupplier, boolean canStartTimer) throws EX {
    if(!canStartTimer && !timerHolder.threadHasTimer()) {
      //Thread cannot start a timer and no timer started, so just run the stuff and return
      return supplier.get();
    }

    String name = nameSupplier.get();
    boolean initDone = timerHolder.initForThread(); //True if init is done here => aka this method starts the timer => so we need to log and cleanup at the end
    long startTime = System.currentTimeMillis();
    try {
      return supplier.get();
    }
    finally {
      timerHolder.registerTime(name, System.currentTimeMillis() - startTime);
      if (initDone) logAndCleanTimer();
    }
  }

  private void logAndCleanTimer() {
    SortedSet<MethodTime> times = timerHolder.getTimes();
    timerHolder.clearTimes();
    timerReportReference.getTimerReport().report(times);
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
