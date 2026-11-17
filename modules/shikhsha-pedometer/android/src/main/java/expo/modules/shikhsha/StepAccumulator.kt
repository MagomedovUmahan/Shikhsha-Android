package expo.modules.shikhsha

/** Never add a device's lifetime count as today's steps. */
data class CounterReading(val total: Long, val boot: Int, val timestamp: Long)
object StepAccumulator {
  fun delta(previous: CounterReading?, next: CounterReading): Long {
    if (previous == null || previous.boot != next.boot) return 0
    if (next.timestamp <= previous.timestamp || next.total < previous.total) return 0
    return next.total - previous.total
  }
}
