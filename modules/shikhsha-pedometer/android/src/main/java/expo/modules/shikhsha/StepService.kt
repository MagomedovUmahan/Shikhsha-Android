package expo.modules.shikhsha

import android.Manifest
import android.app.*
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.hardware.*
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.os.Looper

/** Sensor subscription belongs to the OS service, never to a React screen. */
class StepService : Service(), SensorEventListener {
  companion object {
    @Volatile var running = false
    const val CHANNEL = "shikhsha_steps"
    const val NOTIFICATION = 4101
  }
  private lateinit var sensors: SensorManager
  private lateinit var sensorThread: HandlerThread
  private lateinit var sensorHandler: Handler
  private val mainHandler = Handler(Looper.getMainLooper())
  private var lastNotification = 0L
  override fun onCreate() {
    super.onCreate()
    sensors = getSystemService(SENSOR_SERVICE) as SensorManager
    sensorThread = HandlerThread("ShikhshaSteps").apply { start() }
    sensorHandler = Handler(sensorThread.looper)
  }
  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    if (!StepStore.enabled(this)) { stopSelf(); return START_NOT_STICKY }
    try {
      if (Build.VERSION.SDK_INT >= 29 && checkSelfPermission(Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED)
        throw SecurityException("Разрешите доступ к физической активности в настройках телефона.")
      val sensor = sensors.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        ?: throw IllegalStateException("На телефоне нет аппаратного датчика шагов.")
      if (Build.VERSION.SDK_INT >= 26) {
        val channel = NotificationChannel(CHANNEL, "Подсчёт шагов", NotificationManager.IMPORTANCE_LOW)
        channel.description = "Работающий шагомер Шихша"
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
      }
      if (Build.VERSION.SDK_INT >= 34)
        startForeground(NOTIFICATION, notification(), ServiceInfo.FOREGROUND_SERVICE_TYPE_HEALTH)
      else startForeground(NOTIFICATION, notification())
      if (!running && !sensors.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL, sensorHandler))
        throw IllegalStateException("Не удалось подключиться к датчику шагов.")
      running = true
      return START_STICKY
    } catch (error: Exception) {
      StepStore.failure(this, error.message ?: "Не удалось запустить подсчёт шагов.")
      stopSelf()
      return START_NOT_STICKY
    }
  }
  private fun notification(): Notification {
    val launch = packageManager.getLaunchIntentForPackage(packageName)
    val builder = if (Build.VERSION.SDK_INT >= 26) Notification.Builder(this, CHANNEL) else Notification.Builder(this)
    builder.setSmallIcon(R.drawable.ic_shikhsha_notification)
      .setContentTitle("Шихша · ${StepStore.history(this)[StepStore.day()] ?: 0} шагов")
      .setContentText("Шагомер работает. Остановить можно в приложении.")
      .setOngoing(true).setOnlyAlertOnce(true).setShowWhen(false)
    if (launch != null) builder.setContentIntent(PendingIntent.getActivity(
      this, 0, launch, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    ))
    return builder.build()
  }
  override fun onSensorChanged(event: SensorEvent) {
    if (event.sensor.type != Sensor.TYPE_STEP_COUNTER || event.values.isEmpty() || !StepStore.enabled(this)) return
    StepStore.record(this, event.values[0].toLong(), event.timestamp)
    val now = android.os.SystemClock.elapsedRealtime()
    if (now - lastNotification > 10_000) {
      lastNotification = now
      mainHandler.post {
        if (running && StepStore.enabled(this))
          getSystemService(NotificationManager::class.java).notify(NOTIFICATION, notification())
      }
    }
  }
  override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
  override fun onBind(intent: Intent?): IBinder? = null
  override fun onDestroy() {
    running = false
    sensors.unregisterListener(this)
    sensorThread.quitSafely()
    mainHandler.removeCallbacksAndMessages(null)
    stopForeground(STOP_FOREGROUND_REMOVE)
    super.onDestroy()
  }
}
