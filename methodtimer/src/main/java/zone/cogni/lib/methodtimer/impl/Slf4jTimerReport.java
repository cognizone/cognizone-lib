package zone.cogni.lib.methodtimer.impl;

import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
public class Slf4jTimerReport implements TimerReport {
  private static final String prefixSpace = "         ";
  private static final String newline = "\n";
  private static final String reportHeader = "Total ms, #Samples, Avg ms, Min ms, Max ms, Name";

  @Override
  public void report(Collection<MethodTime> methodTimes) {
    log.info("Timer report: {}", toString(methodTimes));
  }

  private String toString(Collection<MethodTime> methodTimes) {
    String timeLines = methodTimes.stream()
                                  .map(this::toString)
                                  .collect(Collectors.joining(newline));
    return newline + prefixSpace + reportHeader + newline + timeLines;
  }

  private String toString(MethodTime methodTime) {
    return prefixSpace +
           methodTime.getTotalTime() + ", " +
           methodTime.getSamples() + ", " +
           methodTime.getAverage() + ", " +
           methodTime.getMinTime() + ", " +
           methodTime.getMaxTime() + ", " +
           methodTime.getName();
  }
}
