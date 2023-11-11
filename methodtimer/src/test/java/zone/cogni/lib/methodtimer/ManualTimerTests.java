package zone.cogni.lib.methodtimer;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import zone.cogni.lib.methodtimer.infra.ServiceWithTimers;
import zone.cogni.lib.methodtimer.infra.TestConfiguration;
import zone.cogni.lib.methodtimer.infra.TestTimerReport;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "cognizone.methodtimer.enabled=true")
@AutoConfigureMockMvc
@ContextConfiguration(classes = TestConfiguration.class)
public class ManualTimerTests {

  @Inject
  private ServiceWithTimers serviceWithTimers;
  @Inject
  private TestTimerReport testTimerReport;

  @BeforeEach
  void beforeEach() {
    testTimerReport.getReports().clear();
  }

  @Test
  void runInternal_runnable() {
    serviceWithTimers.runInternal_runnable();

    assertThat(testTimerReport.getReports()).hasSize(1);
    Map<String, Integer> callsPerName = testTimerReport.getCallsPerNameForReportCall(0);
    assertThat(callsPerName)
            .hasSize(2)
            .containsEntry("ServiceWithTimers.runInternal_runnable", 1)
            .containsEntry("interal_runnable", 1);
  }

  @Test
  void runInternal_runnableFailableFail() {
    try {
      serviceWithTimers.runInternal_runnableFailableFail();
      Assertions.fail("RunnableFailable should fail");
    }
    catch (IOException e) {
      assertThat(e).hasMessage("exception_interal_runnableFail");
    }

    assertThat(testTimerReport.getReports()).hasSize(1);
    Map<String, Integer> callsPerName = testTimerReport.getCallsPerNameForReportCall(0);
    assertThat(callsPerName)
            .hasSize(2)
            .containsEntry("ServiceWithTimers.runInternal_runnableFailableFail", 1)
            .containsEntry("interal_runnableFailableFail", 1);
  }

  @Test
  void runInternal_supplier() {
    String result =  serviceWithTimers.runInternal_supplier();

    assertThat(result).isEqualTo("returnValue_interal_supplier");
    assertThat(testTimerReport.getReports()).hasSize(1);
    Map<String, Integer> callsPerName = testTimerReport.getCallsPerNameForReportCall(0);
    assertThat(callsPerName)
            .hasSize(2)
            .containsEntry("ServiceWithTimers.runInternal_supplier", 1)
            .containsEntry("interal_supplier", 1);
  }

  @Test
  void runInternal_supplierFailable() {
    try {
      String result = serviceWithTimers.runInternal_supplierFailable();
      assertThat(result).isEqualTo("returnValue_interal_supplierFailable");
    }
    catch (IOException e) {
      Assertions.fail("SupplierFailable should not fail");
    }

    assertThat(testTimerReport.getReports()).hasSize(1);
    Map<String, Integer> callsPerName = testTimerReport.getCallsPerNameForReportCall(0);
    assertThat(callsPerName)
            .hasSize(2)
            .containsEntry("ServiceWithTimers.runInternal_supplierFailable", 1)
            .containsEntry("interal_supplierFailable", 1);
  }

  @Test
  void runInternal_supplierFailableFail() {
    try {
      serviceWithTimers.runInternal_supplierFailableFail();
      Assertions.fail("SupplierFailable should fail");
    }
    catch (IOException e) {
      assertThat(e).hasMessage("exception_interal_supplierFail");
    }

    assertThat(testTimerReport.getReports()).hasSize(1);
    Map<String, Integer> callsPerName = testTimerReport.getCallsPerNameForReportCall(0);
    assertThat(callsPerName)
            .hasSize(2)
            .containsEntry("ServiceWithTimers.runInternal_supplierFailableFail", 1)
            .containsEntry("interal_supplierFailableFail", 1);
  }
}
