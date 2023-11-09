package zone.cogni.lib.methodtimer.infra;

import zone.cogni.lib.methodtimer.impl.MethodTime;
import zone.cogni.lib.methodtimer.impl.TimerReport;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class TestTimerReport implements TimerReport {
  private final List<Collection<MethodTime>> reports = new ArrayList<>();

  @Override
  public void report(Collection<MethodTime> methodTimes) {
    reports.add(methodTimes);
  }

  public List<Collection<MethodTime>> getReports() {
    return reports;
  }

  public Map<String, Integer> getCallsPerNameForReportCall(int index) {
    assertThat(reports).hasSizeGreaterThan(index);
    return reports.get(index)
                  .stream()
                  .collect(Collectors.toMap(MethodTime::getName, MethodTime::getSamples));

  }
}
