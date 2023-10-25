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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = TestConfiguration.class)
public class DisabledTests {

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

    assertThat(testTimerReport.getReports()).isEmpty();
  }
}
