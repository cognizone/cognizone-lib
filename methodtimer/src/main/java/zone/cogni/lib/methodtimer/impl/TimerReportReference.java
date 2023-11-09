package zone.cogni.lib.methodtimer.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

@Slf4j
public class TimerReportReference implements ApplicationListener<ApplicationReadyEvent> {

  private TimerReport timerReport = new Slf4jTimerReport();

  @Override
  public void onApplicationEvent(ApplicationReadyEvent event) {
    try {
      timerReport = event.getApplicationContext().getBean(TimerReport.class);
    }
    catch (NoUniqueBeanDefinitionException e) {
      log.warn("No unique bean of type TimerReport, will use default Slf4jTimerReport one.");
    }
    catch (NoSuchBeanDefinitionException e) {
      log.info("No bean of type TimerReport found, will use default Slf4jTimerReport one.");
    }
  }

  public TimerReport getTimerReport() {
    return timerReport;
  }
}
