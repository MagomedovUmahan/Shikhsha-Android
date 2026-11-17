package expo.modules.shikhsha;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Build;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: StepRecoveryReceiver.kt */
/* JADX INFO: loaded from: classes3.dex */
@Metadata(d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\u0018\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH\u0016¨\u0006\n"}, d2 = {"Lexpo/modules/shikhsha/StepRecoveryReceiver;", "Landroid/content/BroadcastReceiver;", "<init>", "()V", "onReceive", "", "context", "Landroid/content/Context;", "intent", "Landroid/content/Intent;", "shikhsha-pedometer_release"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class StepRecoveryReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(intent, "intent");
        if ((Intrinsics.areEqual(intent.getAction(), "android.intent.action.BOOT_COMPLETED") || Intrinsics.areEqual(intent.getAction(), "android.intent.action.MY_PACKAGE_REPLACED")) && StepStore.INSTANCE.enabled(context) && StepStore.INSTANCE.resumeAfterBoot(context)) {
            if (Intrinsics.areEqual(intent.getAction(), "android.intent.action.BOOT_COMPLETED")) {
                StepStore.INSTANCE.resetBaseline(context);
            }
            boolean z = Build.VERSION.SDK_INT < 29 || context.checkSelfPermission("android.permission.ACTIVITY_RECOGNITION") == 0;
            Object systemService = context.getSystemService("sensor");
            Intrinsics.checkNotNull(systemService, "null cannot be cast to non-null type android.hardware.SensorManager");
            if (!RecoveryPolicy.INSTANCE.shouldResume(StepStore.INSTANCE.enabled(context), StepStore.INSTANCE.resumeAfterBoot(context), z, ((SensorManager) systemService).getDefaultSensor(19) != null)) {
                StepStore.INSTANCE.failure(context, !z ? "Для восстановления шагомера разреши физическую активность и нажми «Старт»." : "Не найден датчик шагов. Проверь счётчик в приложении.");
                return;
            }
            if (StepService.INSTANCE.getRunning()) {
                return;
            }
            try {
                Intent intent2 = new Intent(context, (Class<?>) StepService.class);
                if (Build.VERSION.SDK_INT >= 26) {
                    context.startForegroundService(intent2);
                } else {
                    context.startService(intent2);
                }
            } catch (Exception unused) {
                StepStore.INSTANCE.failure(context, "Android не разрешил восстановить фоновый шагомер. Открой Шихшу и нажми «Старт».");
            }
        }
    }
}
