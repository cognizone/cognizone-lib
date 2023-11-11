package zone.cogni.lib.methodtimer.impl;

import org.apache.commons.lang3.function.FailableSupplier;

import java.util.function.Supplier;

public interface TimedMethodRunner {
  <T, EX extends Throwable> T runTimedMethod(FailableSupplier<T, EX> supplier, Supplier<String> nameSupplier, boolean canStartTimer) throws EX;
}
