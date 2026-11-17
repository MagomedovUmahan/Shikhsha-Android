package expo.modules.shikhsha;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.provider.Settings;
import com.facebook.react.uimanager.ViewProps;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import org.json.JSONObject;
import org.maplibre.reactnative.components.layer.style.MLRNStyleFactory;

/* JADX INFO: compiled from: StepStore.kt */
/* JADX INFO: loaded from: classes3.dex */
@Metadata(d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\n\n\u0002\u0010$\n\u0002\b\u0003\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0018\u0010\u0004\u001a\n \u0006*\u0004\u0018\u00010\u00050\u00052\u0006\u0010\u0007\u001a\u00020\bH\u0002J\u0018\u0010\t\u001a\n \u0006*\u0004\u0018\u00010\n0\n2\b\b\u0002\u0010\u000b\u001a\u00020\fJ\u000e\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0007\u001a\u00020\bJ\u0016\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0011\u001a\u00020\u000eJ\u000e\u0010\u0012\u001a\u00020\u00102\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\u0013\u001a\u00020\u000e2\u0006\u0010\u0007\u001a\u00020\bJ\u0016\u0010\u0014\u001a\u00020\u00102\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0013\u001a\u00020\u000eJ\u0016\u0010\u0015\u001a\u00020\u00102\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0016\u001a\u00020\nJ\u001e\u0010\u0017\u001a\u00020\u00102\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0018\u001a\u00020\f2\u0006\u0010\u0019\u001a\u00020\fJ\u001a\u0010\u001a\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\f0\u001b2\u0006\u0010\u0007\u001a\u00020\bJ\u0010\u0010\u001c\u001a\u0004\u0018\u00010\n2\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\u001d\u001a\u00020\f2\u0006\u0010\u0007\u001a\u00020\b¨\u0006\u001e"}, d2 = {"Lexpo/modules/shikhsha/StepStore;", "", "<init>", "()V", "prefs", "Landroid/content/SharedPreferences;", "kotlin.jvm.PlatformType", "c", "Landroid/content/Context;", "day", "", "millis", "", "resumeAfterBoot", "", "setResumeAfterBoot", "", MLRNStyleFactory.VALUE_KEY, "resetBaseline", ViewProps.ENABLED, "setEnabled", "failure", "message", "record", "total", "eventNanos", "history", "", "error", "updatedAt", "shikhsha-pedometer_release"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class StepStore {
    public static final StepStore INSTANCE = new StepStore();

    private StepStore() {
    }

    private final SharedPreferences prefs(Context c) {
        return c.getSharedPreferences("shikhsha_steps_v1", 0);
    }

    public static /* synthetic */ String day$default(StepStore stepStore, long j, int i, Object obj) {
        if ((i & 1) != 0) {
            j = System.currentTimeMillis();
        }
        return stepStore.day(j);
    }

    public final String day(long millis) {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date(millis));
    }

    public final synchronized boolean resumeAfterBoot(Context c) {
        Intrinsics.checkNotNullParameter(c, "c");
        return prefs(c).getBoolean("resumeAfterBoot", true);
    }

    public final synchronized void setResumeAfterBoot(Context c, boolean value) {
        Intrinsics.checkNotNullParameter(c, "c");
        if (!prefs(c).edit().putBoolean("resumeAfterBoot", value).commit()) {
            throw new IllegalStateException("Не удалось сохранить автовосстановление.".toString());
        }
    }

    public final synchronized void resetBaseline(Context c) {
        Intrinsics.checkNotNullParameter(c, "c");
        prefs(c).edit().remove("counter").remove("boot").remove("timestamp").commit();
    }

    public final synchronized boolean enabled(Context c) {
        Intrinsics.checkNotNullParameter(c, "c");
        return prefs(c).getBoolean(ViewProps.ENABLED, false);
    }

    public final synchronized void setEnabled(Context c, boolean enabled) {
        Intrinsics.checkNotNullParameter(c, "c");
        SharedPreferences sharedPreferencesPrefs = prefs(c);
        SharedPreferences.Editor editorRemove = sharedPreferencesPrefs.edit().putBoolean(ViewProps.ENABLED, enabled).remove("error");
        if (!enabled || !sharedPreferencesPrefs.getBoolean(ViewProps.ENABLED, false)) {
            editorRemove.remove("counter").remove("boot").remove("timestamp");
        }
        if (enabled) {
            String string = sharedPreferencesPrefs.getString("days", "{}");
            if (string == null) {
                string = "{}";
            }
            JSONObject jSONObject = new JSONObject(string);
            if (!jSONObject.has(day$default(this, 0L, 1, null))) {
                jSONObject.put(day$default(this, 0L, 1, null), 0);
            }
            editorRemove.putString("days", jSONObject.toString());
        }
        editorRemove.commit();
        WidgetUpdates.INSTANCE.request(c, true);
    }

    public final synchronized void failure(Context c, String message) {
        Intrinsics.checkNotNullParameter(c, "c");
        Intrinsics.checkNotNullParameter(message, "message");
        prefs(c).edit().putBoolean(ViewProps.ENABLED, false).putString("error", message).remove("counter").remove("boot").remove("timestamp").commit();
        WidgetUpdates.INSTANCE.request(c, true);
    }

    public final synchronized void record(Context c, long total, long eventNanos) {
        Intrinsics.checkNotNullParameter(c, "c");
        SharedPreferences sharedPreferencesPrefs = prefs(c);
        if (sharedPreferencesPrefs.getBoolean(ViewProps.ENABLED, false)) {
            int i = Settings.Global.getInt(c.getContentResolver(), "boot_count", -1);
            CounterReading counterReading = new CounterReading(total, i, eventNanos);
            CounterReading counterReading2 = sharedPreferencesPrefs.contains("counter") ? new CounterReading(sharedPreferencesPrefs.getLong("counter", 0L), sharedPreferencesPrefs.getInt("boot", -1), sharedPreferencesPrefs.getLong("timestamp", 0L)) : null;
            if (counterReading2 == null || counterReading2.getBoot() != i || eventNanos > counterReading2.getTimestamp()) {
                long jDelta = StepAccumulator.INSTANCE.delta(counterReading2, counterReading);
                String string = sharedPreferencesPrefs.getString("days", "{}");
                if (string == null) {
                    string = "{}";
                }
                JSONObject jSONObject = new JSONObject(string);
                String strDay = day(System.currentTimeMillis() - ((SystemClock.elapsedRealtimeNanos() - eventNanos) / 1000000));
                if (jDelta > 0 || jSONObject.has(strDay)) {
                    jSONObject.put(strDay, jSONObject.optLong(strDay, 0L) + jDelta);
                }
                Iterator<String> itKeys = jSONObject.keys();
                Intrinsics.checkNotNullExpressionValue(itKeys, "keys(...)");
                Iterator it = CollectionsKt.drop(CollectionsKt.sortedDescending(SequencesKt.toList(SequencesKt.asSequence(itKeys))), 90).iterator();
                while (it.hasNext()) {
                    jSONObject.remove((String) it.next());
                }
                sharedPreferencesPrefs.edit().putLong("counter", total).putInt("boot", i).putLong("timestamp", eventNanos).putLong("updatedAt", System.currentTimeMillis()).putString("days", jSONObject.toString()).commit();
                WidgetUpdates.request$default(WidgetUpdates.INSTANCE, c, false, 2, null);
            }
        }
    }

    public final synchronized Map<String, Long> history(Context c) {
        Map<String, Long> mutableMap;
        Intrinsics.checkNotNullParameter(c, "c");
        String string = prefs(c).getString("days", "{}");
        if (string == null) {
            string = "{}";
        }
        JSONObject jSONObject = new JSONObject(string);
        Iterator<String> itKeys = jSONObject.keys();
        Intrinsics.checkNotNullExpressionValue(itKeys, "keys(...)");
        Sequence sequenceAsSequence = SequencesKt.asSequence(itKeys);
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (Object obj : sequenceAsSequence) {
            linkedHashMap.put(obj, Long.valueOf(jSONObject.optLong((String) obj, 0L)));
        }
        mutableMap = MapsKt.toMutableMap(linkedHashMap);
        if (enabled(c) && !mutableMap.containsKey(day$default(this, 0L, 1, null))) {
            mutableMap.put(day$default(this, 0L, 1, null), 0L);
        }
        return mutableMap;
    }

    public final synchronized String error(Context c) {
        Intrinsics.checkNotNullParameter(c, "c");
        return prefs(c).getString("error", null);
    }

    public final synchronized long updatedAt(Context c) {
        Intrinsics.checkNotNullParameter(c, "c");
        return prefs(c).getLong("updatedAt", 0L);
    }
}
