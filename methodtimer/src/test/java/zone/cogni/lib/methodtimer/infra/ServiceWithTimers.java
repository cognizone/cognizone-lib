package zone.cogni.lib.methodtimer.infra;

import zone.cogni.lib.methodtimer.TimedMethod;
import zone.cogni.lib.methodtimer.TimerName;

import java.util.Arrays;

public class ServiceWithTimers {

  @TimedMethod
  public void emptyMethod() {
  }

  @TimedMethod
  public void emptyMethod_fieldInTimerName(@TimerName String myName) {
  }

  @TimedMethod("hasName")
  public void emptyMethod_timerHasName() {
  }

  @TimedMethod(canBeParent = false)
  public void emptyMethod_timerNoParent() {
  }

  @TimedMethod
  public void emptyMethod_callRunnables(Runnable... runMe) {
    Arrays.stream(runMe).forEach(Runnable::run);
  }


}
