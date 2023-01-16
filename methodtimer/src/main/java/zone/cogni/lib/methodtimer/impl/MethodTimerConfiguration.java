package zone.cogni.lib.methodtimer.impl;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;

@Configuration
@ConditionalOnProperty(prefix = "cognizone.methodtimer", name = "enabled", havingValue = "true")
public class MethodTimerConfiguration {
  @Bean
  @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
  public TimerAspect timerAspect() {
    return new TimerAspect(timerHolder(), timerReport());
  }

  @Bean
  @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
  public TimerHolder timerHolder() {
    return new TimerHolder();
  }

  @Bean
  @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public TimerReport timerReport() {
    return new Slf4jTimerReport();
  }
}
