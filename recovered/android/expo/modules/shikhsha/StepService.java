package expo.modules.shikhsha;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import androidx.core.app.NotificationCompat;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: StepService.kt */
/* JADX INFO: loaded from: classes3.dex */
@Metadata(d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000 $2\u00020\u00012\u00020\u0002:\u0001$B\u0007¢\u0006\u0004\b\u0003\u0010\u0004J\b\u0010\u000e\u001a\u00020\u000fH\u0016J\"\u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u00132\u0006\u0010\u0014\u001a\u00020\u00112\u0006\u0010\u0015\u001a\u00020\u0011H\u0016J\b\u0010\u0016\u001a\u00020\u0017H\u0002J\u0010\u0010\u0018\u001a\u00020\u000f2\u0006\u0010\u0019\u001a\u00020\u001aH\u0016J\u0012\u0010\u001b\u001a\u00020\u000f2\b\u0010\u001c\u001a\u0004\u0018\u00010\u0013H\u0016J\u001a\u0010\u001d\u001a\u00020\u000f2\b\u0010\u001e\u001a\u0004\u0018\u00010\u001f2\u0006\u0010 \u001a\u00020\u0011H\u0016J\u0014\u0010!\u001a\u0004\u0018\u00010\"2\b\u0010\u0012\u001a\u0004\u0018\u00010\u0013H\u0016J\b\u0010#\u001a\u00020\u000fH\u0016R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006%"}, d2 = {"Lexpo/modules/shikhsha/StepService;", "Landroid/app/Service;", "Landroid/hardware/SensorEventListener;", "<init>", "()V", "sensors", "Landroid/hardware/SensorManager;", "sensorThread", "Landroid/os/HandlerThread;", "sensorHandler", "Landroid/os/Handler;", "mainHandler", "lastNotification", "", "onCreate", "", "onStartCommand", "", "intent", "Landroid/content/Intent;", "flags", "startId", "notification", "Landroid/app/Notification;", "onSensorChanged", NotificationCompat.CATEGORY_EVENT, "Landroid/hardware/SensorEvent;", "onTaskRemoved", "rootIntent", "onAccuracyChanged", "sensor", "Landroid/hardware/Sensor;", "accuracy", "onBind", "Landroid/os/IBinder;", "onDestroy", "Companion", "shikhsha-pedometer_release"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class StepService extends Service implements SensorEventListener {
    public static final String CHANNEL = "shikhsha_steps";

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    public static final int NOTIFICATION = 4101;
    private static volatile boolean detectorRunning;
    private static volatile boolean running;
    private long lastNotification;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private Handler sensorHandler;
    private HandlerThread sensorThread;
    private SensorManager sensors;

    @Override // android.hardware.SensorEventListener
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    /* JADX INFO: compiled from: StepService.kt */
    @Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\b\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u001a\u0010\u0004\u001a\u00020\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\tR\u001a\u0010\n\u001a\u00020\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\u0007\"\u0004\b\f\u0010\tR\u000e\u0010\r\u001a\u00020\u000eX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u0011"}, d2 = {"Lexpo/modules/shikhsha/StepService$Companion;", "", "<init>", "()V", "running", "", "getRunning", "()Z", "setRunning", "(Z)V", "detectorRunning", "getDetectorRunning", "setDetectorRunning", "CHANNEL", "", "NOTIFICATION", "", "shikhsha-pedometer_release"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final boolean getRunning() {
            return StepService.running;
        }

        public final void setRunning(boolean z) {
            StepService.running = z;
        }

        public final boolean getDetectorRunning() {
            return StepService.detectorRunning;
        }

        public final void setDetectorRunning(boolean z) {
            StepService.detectorRunning = z;
        }
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        Object systemService = getSystemService("sensor");
        Intrinsics.checkNotNull(systemService, "null cannot be cast to non-null type android.hardware.SensorManager");
        this.sensors = (SensorManager) systemService;
        HandlerThread handlerThread = new HandlerThread("ShikhshaSteps");
        handlerThread.start();
        this.sensorThread = handlerThread;
        HandlerThread handlerThread2 = this.sensorThread;
        if (handlerThread2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sensorThread");
            handlerThread2 = null;
        }
        this.sensorHandler = new Handler(handlerThread2.getLooper());
    }

    /* JADX WARN: Code duplicated, block: B:62:0x00db  */
    @Override // android.app.Service
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean z;
        StepService stepService = this;
        if (!StepStore.INSTANCE.enabled(stepService)) {
            stopSelf();
            return 2;
        }
        try {
            if (Build.VERSION.SDK_INT >= 29 && checkSelfPermission("android.permission.ACTIVITY_RECOGNITION") != 0) {
                throw new SecurityException("Разрешите доступ к физической активности в настройках телефона.");
            }
            SensorManager sensorManager = this.sensors;
            SensorManager sensorManager2 = null;
            if (sensorManager == null) {
                Intrinsics.throwUninitializedPropertyAccessException("sensors");
                sensorManager = null;
            }
            Sensor defaultSensor = sensorManager.getDefaultSensor(19);
            if (defaultSensor == null) {
                throw new IllegalStateException("На телефоне нет аппаратного датчика шагов.");
            }
            if (Build.VERSION.SDK_INT >= 26) {
                NotificationChannel notificationChannel = new NotificationChannel(CHANNEL, "Подсчёт шагов", 2);
                notificationChannel.setDescription("Работающий шагомер Шихша");
                ((NotificationManager) getSystemService(NotificationManager.class)).createNotificationChannel(notificationChannel);
            }
            if (Build.VERSION.SDK_INT >= 34) {
                startForeground(NOTIFICATION, notification(), 256);
            } else {
                startForeground(NOTIFICATION, notification());
            }
            if (!running) {
                SensorManager sensorManager3 = this.sensors;
                if (sensorManager3 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("sensors");
                    sensorManager3 = null;
                }
                StepService stepService2 = this;
                Handler handler = this.sensorHandler;
                if (handler == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("sensorHandler");
                    handler = null;
                }
                if (!sensorManager3.registerListener(stepService2, defaultSensor, 3, handler)) {
                    throw new IllegalStateException("Не удалось подключиться к датчику шагов.");
                }
            }
            if (!running) {
                SensorManager sensorManager4 = this.sensors;
                if (sensorManager4 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("sensors");
                    sensorManager4 = null;
                }
                Sensor defaultSensor2 = sensorManager4.getDefaultSensor(18);
                try {
                    WalkingStore.INSTANCE.begin(this, defaultSensor2 != null);
                    if (defaultSensor2 == null) {
                        z = false;
                    } else {
                        SensorManager sensorManager5 = this.sensors;
                        if (sensorManager5 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("sensors");
                            sensorManager5 = null;
                        }
                        StepService stepService3 = this;
                        Handler handler2 = this.sensorHandler;
                        if (handler2 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("sensorHandler");
                            handler2 = null;
                        }
                        if (sensorManager5.registerListener(stepService3, defaultSensor2, 3, handler2)) {
                            z = true;
                        } else {
                            z = false;
                        }
                    }
                    detectorRunning = z;
                    WalkingStore.INSTANCE.setDetectorAvailable(this, detectorRunning);
                    WalkingStore.INSTANCE.clearError(this);
                } catch (Exception unused) {
                    detectorRunning = false;
                    if (defaultSensor2 != null) {
                        SensorManager sensorManager6 = this.sensors;
                        if (sensorManager6 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("sensors");
                        } else {
                            sensorManager2 = sensorManager6;
                        }
                        sensorManager2.unregisterListener(this, defaultSensor2);
                    }
                    WalkingStore.INSTANCE.failure(this);
                }
            }
            running = true;
            WidgetUpdates.INSTANCE.request(this, true);
            return 1;
        } catch (Exception e) {
            StepStore stepStore = StepStore.INSTANCE;
            String message = e.getMessage();
            if (message == null) {
                message = "Не удалось запустить подсчёт шагов.";
            }
            stepStore.failure(stepService, message);
            stopSelf();
            return 2;
        }
    }

    private final Notification notification() {
        Intent launchIntentForPackage = getPackageManager().getLaunchIntentForPackage(getPackageName());
        Notification.Builder builder = Build.VERSION.SDK_INT >= 26 ? new Notification.Builder(this, CHANNEL) : new Notification.Builder(this);
        Notification.Builder smallIcon = builder.setSmallIcon(R.drawable.ic_shikhsha_notification);
        StepService stepService = this;
        Long l = StepStore.INSTANCE.history(stepService).get(StepStore.day$default(StepStore.INSTANCE, 0L, 1, null));
        smallIcon.setContentTitle("Шихша · " + (l != null ? l.longValue() : 0L) + " шагов").setContentText("Работает с закрытым экраном. Пауза — в Шихше.").setOngoing(true).setOnlyAlertOnce(true).setShowWhen(false);
        if (launchIntentForPackage != null) {
            builder.setContentIntent(PendingIntent.getActivity(stepService, 0, launchIntentForPackage, 201326592));
        }
        Notification notificationBuild = builder.build();
        Intrinsics.checkNotNullExpressionValue(notificationBuild, "build(...)");
        return notificationBuild;
    }

    @Override // android.hardware.SensorEventListener
    public void onSensorChanged(SensorEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        float[] values = event.values;
        Intrinsics.checkNotNullExpressionValue(values, "values");
        if (values.length == 0) {
            return;
        }
        StepService stepService = this;
        if (StepStore.INSTANCE.enabled(stepService)) {
            if (event.sensor.getType() == 18) {
                if (event.values[0] >= 1.0f && detectorRunning) {
                    try {
                        WalkingStore.INSTANCE.step(this, event.timestamp);
                        return;
                    } catch (Exception unused) {
                        detectorRunning = false;
                        WalkingStore.INSTANCE.failure(stepService);
                        return;
                    }
                }
                return;
            }
            if (event.sensor.getType() != 19) {
                return;
            }
            StepStore.INSTANCE.record(stepService, (long) event.values[0], event.timestamp);
            long jElapsedRealtime = SystemClock.elapsedRealtime();
            if (jElapsedRealtime - this.lastNotification > 10000) {
                this.lastNotification = jElapsedRealtime;
                this.mainHandler.post(new Runnable() { // from class: expo.modules.shikhsha.StepService$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        StepService.onSensorChanged$lambda$1(this.f$0);
                    }
                });
            }
        }
    }

    static final void onSensorChanged$lambda$1(StepService stepService) {
        if (running && StepStore.INSTANCE.enabled(stepService)) {
            ((NotificationManager) stepService.getSystemService(NotificationManager.class)).notify(NOTIFICATION, stepService.notification());
        }
    }

    @Override // android.app.Service
    public void onTaskRemoved(Intent rootIntent) {
        try {
            Result.Companion companion = Result.INSTANCE;
            StepService stepService = this;
            SensorManager sensorManager = this.sensors;
            if (sensorManager == null) {
                Intrinsics.throwUninitializedPropertyAccessException("sensors");
                sensorManager = null;
            }
            Result.m783constructorimpl(Boolean.valueOf(sensorManager.flush(this)));
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            Result.m783constructorimpl(ResultKt.createFailure(th));
        }
        super.onTaskRemoved(rootIntent);
    }

    @Override // android.app.Service
    public void onDestroy() {
        running = false;
        detectorRunning = false;
        WalkingStore.INSTANCE.end();
        WidgetUpdates.INSTANCE.request(this, true);
        SensorManager sensorManager = this.sensors;
        if (sensorManager == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sensors");
            sensorManager = null;
        }
        sensorManager.unregisterListener(this);
        HandlerThread handlerThread = this.sensorThread;
        if (handlerThread == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sensorThread");
            handlerThread = null;
        }
        handlerThread.quitSafely();
        this.mainHandler.removeCallbacksAndMessages(null);
        stopForeground(1);
        super.onDestroy();
    }
}
