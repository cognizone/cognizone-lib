package zone.cogni.lib.methodtimer.impl;

import java.util.Collection;

public interface TimerReport {
  void report(Collection<MethodTime> methodTimes);
}
