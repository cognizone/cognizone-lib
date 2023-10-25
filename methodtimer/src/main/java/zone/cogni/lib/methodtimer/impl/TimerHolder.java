package zone.cogni.lib.methodtimer.impl;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class TimerHolder {
  private static final ThreadLocal<Map<String, MethodTime>> timeMap = new ThreadLocal<>();

  //Compare time first then name, if twice same time we don't want the count to get lost so also compare on (unique) name
  private static final Comparator<MethodTime> methodTimeComparator = Comparator.comparing(MethodTime::getTotalTime).thenComparing(MethodTime::getName);

  void registerTime(String name, long time) {
    Map<String, MethodTime> threadTimeMap = timeMap.get();
    if (threadTimeMap.containsKey(name)) {
      threadTimeMap.get(name).addTime(time);
    }
    else {
      threadTimeMap.put(name, new MethodTime(name, time));
    }
  }

  boolean threadHasTimer() {
    return timeMap.get() != null;
  }
  boolean initForThread() {
    if(threadHasTimer()) return false;

    timeMap.set(new HashMap<>());
    return true;
  }

  public SortedSet<MethodTime> getTimes() {
    SortedSet<MethodTime> times = new TreeSet<>(methodTimeComparator);
    times.addAll(timeMap.get().values());
    return times;
  }

  public void clearTimes() {
    timeMap.remove();
  }

}
