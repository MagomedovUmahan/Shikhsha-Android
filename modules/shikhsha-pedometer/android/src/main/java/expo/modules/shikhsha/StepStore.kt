package expo.modules.shikhsha

import android.content.Context
import android.os.SystemClock
import android.provider.Settings
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** Serialized writes shared by the foreground service and Expo module. */
object StepStore {
  private fun prefs(c: Context) = c.getSharedPreferences("shikhsha_steps_v1", Context.MODE_PRIVATE)
  fun day(millis: Long = System.currentTimeMillis()) =
    SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date(millis))
  @Synchronized fun enabled(c: Context) = prefs(c).getBoolean("enabled", false)
  @Synchronized fun setEnabled(c: Context, enabled: Boolean) {
    val p = prefs(c)
    val editor = p.edit().putBoolean("enabled", enabled).remove("error")
    // A user pause resets the baseline. Process death does not.
    if (!enabled || !p.getBoolean("enabled", false))
      editor.remove("counter").remove("boot").remove("timestamp")
    if (enabled) {
      val days = JSONObject(p.getString("days", "{}") ?: "{}")
      if (!days.has(day())) days.put(day(), 0)
      editor.putString("days", days.toString())
    }
    editor.commit()
  }
  @Synchronized fun failure(c: Context, message: String) {
    prefs(c).edit().putBoolean("enabled", false).putString("error", message)
      .remove("counter").remove("boot").remove("timestamp").commit()
  }
  @Synchronized fun record(c: Context, total: Long, eventNanos: Long) {
    val p = prefs(c)
    if (!p.getBoolean("enabled", false)) return
    val boot = Settings.Global.getInt(c.contentResolver, Settings.Global.BOOT_COUNT, -1)
    val next = CounterReading(total, boot, eventNanos)
    val previous = if (p.contains("counter")) CounterReading(
      p.getLong("counter", 0), p.getInt("boot", -1), p.getLong("timestamp", 0)
    ) else null
    if (previous != null && previous.boot == boot && eventNanos <= previous.timestamp) return
    val delta = StepAccumulator.delta(previous, next)
    val days = JSONObject(p.getString("days", "{}") ?: "{}")
    val eventMillis = System.currentTimeMillis() -
      (SystemClock.elapsedRealtimeNanos() - eventNanos) / 1_000_000
    val key = day(eventMillis)
    if (delta > 0 || days.has(key)) days.put(key, days.optLong(key, 0) + delta)
    days.keys().asSequence().toList().sortedDescending().drop(90).forEach { days.remove(it) }
    p.edit().putLong("counter", total).putInt("boot", boot).putLong("timestamp", eventNanos)
      .putLong("updatedAt", System.currentTimeMillis()).putString("days", days.toString()).commit()
  }
  @Synchronized fun history(c: Context): Map<String, Long> {
    val json = JSONObject(prefs(c).getString("days", "{}") ?: "{}")
    val result = json.keys().asSequence().associateWith { json.optLong(it, 0) }.toMutableMap()
    if (enabled(c) && !result.containsKey(day())) result[day()] = 0
    return result
  }
  @Synchronized fun error(c: Context) = prefs(c).getString("error", null)
  @Synchronized fun updatedAt(c: Context) = prefs(c).getLong("updatedAt", 0)
}
