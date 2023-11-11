package zone.cogni.lib.methodtimer.infra;

import zone.cogni.lib.methodtimer.ManualTimer;
import zone.cogni.lib.methodtimer.TimedMethod;
import zone.cogni.lib.methodtimer.TimerName;

import java.io.IOException;
import java.util.Arrays;

public class ServiceWithTimers {

  @TimedMethod
  public void emptyMethod() {
  }

  @TimedMethod
  public void emptyMethod_fieldInTimerName(@TimerName String myName) {
  }

  @TimedMethod("hasName")
  public void emptyMethod_timerHasName() {
  }

  @TimedMethod(canStartTimer = false)
  public void emptyMethod_timerNoParent() {
  }

  @TimedMethod
  public void emptyMethod_callRunnables(Runnable... runMe) {
    Arrays.stream(runMe).forEach(Runnable::run);
  }

  @TimedMethod
  public void runInternal_runnable() {
    ManualTimer.run(this::interal_runnable, "interal_runnable");
  }

  private void interal_runnable() {
  }

  @TimedMethod
  public String runInternal_supplier() {
    return ManualTimer.run(this::interal_supplier, "interal_supplier");
  }

  private String interal_supplier() {
    return "returnValue_interal_supplier";
  }

  @TimedMethod
  public void runInternal_runnableFailableFail() throws IOException {
    ManualTimer.runFailable(this::interal_runnableFailableFail, "interal_runnableFailableFail");
  }

  private void interal_runnableFailableFail() throws IOException {
    throw new IOException("exception_interal_runnableFail");
  }

  @TimedMethod
  public String runInternal_supplierFailableFail() throws IOException {
    return ManualTimer.runFailable(this::interal_supplierFailableFail, "interal_supplierFailableFail");
  }

  private String interal_supplierFailableFail() throws IOException {
    throw new IOException("exception_interal_supplierFail");
  }

  @TimedMethod
  public String runInternal_supplierFailable() throws IOException {
    return ManualTimer.runFailable(this::interal_supplierFailable, "interal_supplierFailable");
  }

  private String interal_supplierFailable() {
    return "returnValue_interal_supplierFailable";
  }

}
