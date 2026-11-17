package expo.modules.shikhsha

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Build
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition

class ShikhshaPedometerModule : Module() {
  private fun context(): Context = appContext.reactContext ?: error("Приложение ещё не готово.")
  private fun granted(c: Context) = Build.VERSION.SDK_INT < 29 ||
    c.checkSelfPermission(Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_GRANTED
  private fun available(c: Context) = (c.getSystemService(Context.SENSOR_SERVICE) as SensorManager)
    .getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null
  private fun snapshot(): Map<String, Any?> {
    val c = context()
    val permission = granted(c)
    return mapOf(
      "available" to available(c), "permission" to if (permission) "granted" else "denied",
      "enabled" to StepStore.enabled(c), "running" to (StepStore.enabled(c) && StepService.running && permission),
      "days" to StepStore.history(c), "updatedAt" to StepStore.updatedAt(c), "error" to StepStore.error(c)
    )
  }
  override fun definition() = ModuleDefinition {
    Name("ShikhshaPedometer")
    AsyncFunction("getSnapshot") { snapshot() }
    AsyncFunction("getLiveSnapshot") { snapshot() }
    AsyncFunction("start") {
      val c = context()
      check(available(c)) { "На телефоне нет аппаратного датчика шагов." }
      check(granted(c)) { "Нет разрешения на физическую активность." }
      StepStore.setEnabled(c, true)
      try {
        val intent = Intent(c, StepService::class.java)
        if (Build.VERSION.SDK_INT >= 26) c.startForegroundService(intent) else c.startService(intent)
      } catch (error: Exception) {
        StepStore.failure(c, error.message ?: "Не удалось запустить службу.")
        throw error
      }
      snapshot()
    }
    AsyncFunction("stop") {
      val c = context()
      StepStore.setEnabled(c, false)
      c.stopService(Intent(c, StepService::class.java))
      snapshot()
    }
  }
}
