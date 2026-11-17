package expo.modules.shikhsha;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

/* JADX INFO: compiled from: StepWidgetProvider.kt */
/* JADX INFO: loaded from: classes3.dex */
@Metadata(d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0018\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u0011\u001a\u00020\u0012R\u0016\u0010\u0004\u001a\n \u0006*\u0004\u0018\u00010\u00050\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u0013"}, d2 = {"Lexpo/modules/shikhsha/WidgetUpdates;", "", "<init>", "()V", "executor", "Ljava/util/concurrent/ExecutorService;", "kotlin.jvm.PlatformType", "handler", "Landroid/os/Handler;", "pending", "Ljava/lang/Runnable;", "lastUpdate", "", "request", "", "context", "Landroid/content/Context;", "immediate", "", "shikhsha-pedometer_release"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class WidgetUpdates {
    public static final WidgetUpdates INSTANCE = new WidgetUpdates();
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final Handler handler = new Handler(Looper.getMainLooper());
    private static long lastUpdate;
    private static Runnable pending;

    private WidgetUpdates() {
    }

    public static /* synthetic */ void request$default(WidgetUpdates widgetUpdates, Context context, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        widgetUpdates.request(context, z);
    }

    public final synchronized void request(Context context, boolean immediate) {
        Intrinsics.checkNotNullParameter(context, "context");
        try {
            final Context applicationContext = context.getApplicationContext();
            StepWidgetProvider.Companion companion = StepWidgetProvider.INSTANCE;
            Intrinsics.checkNotNull(applicationContext);
            if (companion.ids(applicationContext).length == 0) {
                return;
            }
            Runnable runnable = pending;
            if (runnable != null && !immediate) {
                return;
            }
            if (runnable != null) {
                handler.removeCallbacks(runnable);
            }
            Runnable runnable2 = new Runnable() { // from class: expo.modules.shikhsha.WidgetUpdates$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    WidgetUpdates.request$lambda$4(applicationContext);
                }
            };
            pending = runnable2;
            Handler handler2 = handler;
            long jCoerceAtLeast = 0;
            if (!immediate) {
                jCoerceAtLeast = RangesKt.coerceAtLeast(10000 - (SystemClock.elapsedRealtime() - lastUpdate), 0L);
            }
            handler2.postDelayed(runnable2, jCoerceAtLeast);
        } catch (Exception unused) {
        }
    }

    static final void request$lambda$4(final Context context) {
        synchronized (INSTANCE) {
            pending = null;
            lastUpdate = SystemClock.elapsedRealtime();
            Unit unit = Unit.INSTANCE;
        }
        executor.execute(new Runnable() { // from class: expo.modules.shikhsha.WidgetUpdates$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                WidgetUpdates.request$lambda$4$lambda$3(context);
            }
        });
    }

    static final void request$lambda$4$lambda$3(Context context) {
        try {
            Result.Companion companion = Result.INSTANCE;
            StepWidgetProvider.Companion companion2 = StepWidgetProvider.INSTANCE;
            Intrinsics.checkNotNull(context);
            companion2.updateAll(context);
            Result.m783constructorimpl(Unit.INSTANCE);
        } catch (Throwable th) {
            Result.Companion companion3 = Result.INSTANCE;
            Result.m783constructorimpl(ResultKt.createFailure(th));
        }
    }
}
