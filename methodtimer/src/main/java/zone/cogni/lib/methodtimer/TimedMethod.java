package zone.cogni.lib.methodtimer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to put on a method that needs to be timed.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TimedMethod {
  /**
   * To specify the name of this timer. If not set, the method name will be used.
   */
  String value() default "";

  /**
   * Put to false so this method will only be timed if a timer is already running in the current thread.
   */
  boolean canStartTimer() default true;
}
