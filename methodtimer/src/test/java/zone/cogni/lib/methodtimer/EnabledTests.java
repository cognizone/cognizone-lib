package zone.cogni.lib.methodtimer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import zone.cogni.lib.methodtimer.infra.ServiceWithTimers;
import zone.cogni.lib.methodtimer.infra.TestConfiguration;
import zone.cogni.lib.methodtimer.infra.TestTimerReport;

import javax.inject.Inject;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "cognizone.methodtimer.enabled=true")
@AutoConfigureMockMvc
@ContextConfiguration(classes = TestConfiguration.class)
public class EnabledTests {

  @Inject
  private ServiceWithTimers serviceWithTimers;
  @Inject
  private TestTimerReport testTimerReport;

  @BeforeEach
  void beforeEach() {
    testTimerReport.getReports().clear();
  }

  @Test
  void simple() {
    serviceWithTimers.emptyMethod();

    assertThat(testTimerReport.getReports()).hasSize(1);
    Map<String, Integer> callsPerName = testTimerReport.getCallsPerNameForReportCall(0);
    assertThat(callsPerName)
            .hasSize(1)
            .containsEntry("ServiceWithTimers.emptyMethod", 1);
  }

  @Test
  void simple_calledTwice() {
    serviceWithTimers.emptyMethod();
    serviceWithTimers.emptyMethod();

    assertThat(testTimerReport.getReports()).hasSize(2);
    Map<String, Integer> callsPerName1 = testTimerReport.getCallsPerNameForReportCall(0);
    assertThat(callsPerName1)
            .hasSize(1)
            .containsEntry("ServiceWithTimers.emptyMethod", 1);
    Map<String, Integer> callsPerName2 = testTimerReport.getCallsPerNameForReportCall(1);
    assertThat(callsPerName2)
            .hasSize(1)
            .containsEntry("ServiceWithTimers.emptyMethod", 1);
  }

  @Test
  void otherName() {
    serviceWithTimers.emptyMethod_timerHasName();

    assertThat(testTimerReport.getReports()).hasSize(1);
    Map<String, Integer> callsPerName = testTimerReport.getCallsPerNameForReportCall(0);
    assertThat(callsPerName)
            .hasSize(1)
            .containsEntry("hasName", 1);
  }

  @Test
  void noParent_showNotShow() {
    serviceWithTimers.emptyMethod_timerNoParent();

    assertThat(testTimerReport.getReports()).isEmpty();
  }

  @Test
  void paramterIsPickedUp() {
    serviceWithTimers.emptyMethod_fieldInTimerName("thisIsAdded");

    assertThat(testTimerReport.getReports()).hasSize(1);
    Map<String, Integer> callsPerName = testTimerReport.getCallsPerNameForReportCall(0);
    assertThat(callsPerName)
            .hasSize(1)
            .containsEntry("ServiceWithTimers.emptyMethod_fieldInTimerName-thisIsAdded", 1);
  }

  @Test
  void subCall() {
    serviceWithTimers.emptyMethod_callRunnables(serviceWithTimers::emptyMethod);

    assertThat(testTimerReport.getReports()).hasSize(1);
    Map<String, Integer> callsPerName = testTimerReport.getCallsPerNameForReportCall(0);
    assertThat(callsPerName)
            .hasSize(2)
            .containsEntry("ServiceWithTimers.emptyMethod_callRunnables", 1)
            .containsEntry("ServiceWithTimers.emptyMethod", 1);
  }

  @Test
  void subCall_multiple_and_noPartentOne() {
    serviceWithTimers.emptyMethod_callRunnables(serviceWithTimers::emptyMethod, serviceWithTimers::emptyMethod, serviceWithTimers::emptyMethod_timerNoParent);

    assertThat(testTimerReport.getReports()).hasSize(1);
    Map<String, Integer> callsPerName = testTimerReport.getCallsPerNameForReportCall(0);
    assertThat(callsPerName)
            .hasSize(3)
            .containsEntry("ServiceWithTimers.emptyMethod_callRunnables", 1)
            .containsEntry("ServiceWithTimers.emptyMethod_timerNoParent", 1)
            .containsEntry("ServiceWithTimers.emptyMethod", 2);
  }
}
