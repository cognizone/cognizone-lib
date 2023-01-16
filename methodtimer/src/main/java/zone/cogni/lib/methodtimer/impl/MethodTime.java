package zone.cogni.lib.methodtimer.impl;

import lombok.Getter;

@Getter
public class MethodTime {
  private final String name;
  private int samples = 1;
  private long totalTime;
  private long maxTime = -1L;
  private long minTime = -1L;

  public MethodTime(String name, long time) {
    this.name = name;
    this.totalTime = time;
    this.maxTime = time;
    this.minTime = time;
  }

  public void addTime(long time) {
    samples++;
    totalTime += time;
    maxTime = Math.max(maxTime, time);
    minTime = Math.min(minTime, time);
  }

  public long getAverage() {
    return totalTime / samples;
  }

}
