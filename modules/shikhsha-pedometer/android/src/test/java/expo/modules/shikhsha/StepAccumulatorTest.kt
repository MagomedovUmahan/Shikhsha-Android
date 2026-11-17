package expo.modules.shikhsha

import org.junit.Assert.assertEquals
import org.junit.Test

class StepAccumulatorTest {
  @Test fun firstReadingIsBaseline() = assertEquals(0L, StepAccumulator.delta(null, CounterReading(42000, 1, 1)))
  @Test fun normalDelta() = assertEquals(17L, StepAccumulator.delta(CounterReading(100, 1, 1), CounterReading(117, 1, 2)))
  @Test fun rebootDoesNotAddLifetimeCount() = assertEquals(0L, StepAccumulator.delta(CounterReading(100, 1, 1), CounterReading(120, 2, 2)))
  @Test fun sensorResetDoesNotSubtract() = assertEquals(0L, StepAccumulator.delta(CounterReading(100, 1, 1), CounterReading(2, 1, 2)))
  @Test fun duplicateIgnored() = assertEquals(0L, StepAccumulator.delta(CounterReading(100, 1, 2), CounterReading(117, 1, 2)))
  @Test fun outOfOrderIgnored() = assertEquals(0L, StepAccumulator.delta(CounterReading(100, 1, 2), CounterReading(117, 1, 1)))
}
