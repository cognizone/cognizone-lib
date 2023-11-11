package zone.cogni.lib.methodtimer;

import org.apache.commons.lang3.function.FailableRunnable;
import org.apache.commons.lang3.function.FailableSupplier;
import zone.cogni.lib.methodtimer.impl.TimedMethodRunner;
import zone.cogni.lib.methodtimer.impl.TimerAspect;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

/**
 * Timer to be used in private methods or methods called directly from within the Bean that would otherwise not be picked up by spring aspectJ proxy.
 * Such timers can never start a new timer, so a timer already has to run in the thread.
 */
@SuppressWarnings("NewMethodNamingConvention")
public class ManualTimer {
  private static TimedMethodRunner timedMethodRunner = new SimpleTimedMethodRunner();

  private static final AtomicBoolean calledOnce = new AtomicBoolean(false);

  /**
   * Internal use only, do not call
   */
  public ManualTimer(TimerAspect timerAspect) {

    if(calledOnce.getAndSet(true)) throw new RuntimeException("Already called");

    //ugly stuff here, please look away - how can we set the value without overwrite
    //noinspection AssignmentToStaticFieldFromInstanceMethod
    timedMethodRunner = timerAspect;
  }

  public static void run(Runnable runnable, String timerName) {
    timedMethodRunner.runTimedMethod(() -> run(runnable), () -> timerName, false);
  }

  public static <T> T run(Supplier<T> supplier, String timerName) {
    return timedMethodRunner.runTimedMethod(supplier::get, () -> timerName, false);
  }

  public static <EX extends Throwable> void runFailable(FailableRunnable<EX> runnable, String timerName) throws EX {
    timedMethodRunner.runTimedMethod(() -> run(runnable), () -> timerName, false);
  }

  public static <T, EX extends Throwable> T runFailable(FailableSupplier<T, EX> supplier, String timerName) throws EX {
    return timedMethodRunner.runTimedMethod(supplier, () -> timerName, false);
  }

  private static Void run(Runnable runnable) {
    runnable.run();
    return null;
  }

  private static <EX extends Throwable> Void run(FailableRunnable<EX> runnable) throws EX {
    runnable.run();
    return null;
  }

  private static class SimpleTimedMethodRunner implements TimedMethodRunner {
    @Override
    public <T, EX extends Throwable> T runTimedMethod(FailableSupplier<T, EX> supplier, Supplier<String> nameSupplier, boolean canStartTimer) throws EX {
      return supplier.get();
    }
  }
}
