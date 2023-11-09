package zone.cogni.lib.methodtimer.infra;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import zone.cogni.lib.methodtimer.impl.MethodTimerConfiguration;

@Configuration
@Import(MethodTimerConfiguration.class)
@EnableAspectJAutoProxy
public class TestConfiguration {

  @Bean
  public ServiceWithTimers serviceWithTimers() {
    return new ServiceWithTimers();
  }

  @Bean
  public TestTimerReport testTimerReport() {
    return new TestTimerReport();
  }
}
