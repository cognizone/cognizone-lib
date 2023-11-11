package zone.cogni.lib.methodtimer.impl;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Role;
import zone.cogni.lib.methodtimer.ManualTimer;

@Configuration
@ConditionalOnProperty(prefix = "cognizone.methodtimer", name = "enabled", havingValue = "true")
@EnableAspectJAutoProxy
public class MethodTimerConfiguration {

  @Bean
  @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
  public TimerAspect timerAspect() {
    return new TimerAspect(timerHolder(), timerReportReference());
  }

  @Bean
  @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
  public TimerHolder timerHolder() {
    return new TimerHolder();
  }

  @Bean
  @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
  public TimerReportReference timerReportReference() {
    return new TimerReportReference();
  }

  @Bean
  @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
  public ManualTimer manualTimer() {
    return new ManualTimer(timerAspect());
  }
}
