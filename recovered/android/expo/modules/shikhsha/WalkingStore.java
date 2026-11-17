package expo.modules.shikhsha;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.SystemClock;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import com.facebook.react.uimanager.ViewProps;
import com.google.android.gms.common.Scopes;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.MapsKt;
import kotlin.io.CloseableKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: WalkingStore.kt */
/* JADX INFO: loaded from: classes3.dex */
@Metadata(d1 = {"\u0000l\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010$\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\b\n\u0002\b\r\n\u0002\u0010 \n\u0002\b\u0004\bÆ\u0002\u0018\u00002\u00020\u0001:\u00016B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0018\u0010\f\u001a\n \u000e*\u0004\u0018\u00010\r0\r2\u0006\u0010\u000f\u001a\u00020\u0010H\u0002J\u0018\u0010\u0011\u001a\n \u000e*\u0004\u0018\u00010\u00120\u00122\u0006\u0010\u000f\u001a\u00020\u0010H\u0002J\u0016\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0015\u001a\u00020\u0016J\u0010\u0010\u0017\u001a\u00020\u00162\u0006\u0010\u000f\u001a\u00020\u0010H\u0002J\u0018\u0010\u0018\u001a\u00020\u00142\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0019\u001a\u00020\u001aH\u0002J\u0016\u0010\u001b\u001a\u00020\u00142\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u001c\u001a\u00020\u0016J\u0006\u0010\u001d\u001a\u00020\u0014J\u0016\u0010\n\u001a\u00020\u00142\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u001e\u001a\u00020\u000bJ&\u0010\u001f\u001a\u0012\u0012\u0004\u0012\u00020\u000b\u0012\u0006\u0012\u0004\u0018\u00010\u0001\u0018\u00010 2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u001e\u001a\u00020\u000bJ6\u0010!\u001a\u00020\u00142\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\"\u001a\u00020#2\u0006\u0010$\u001a\u00020%2\u0006\u0010&\u001a\u00020#2\u0006\u0010'\u001a\u00020%2\u0006\u0010(\u001a\u00020%J\u0015\u0010)\u001a\u0004\u0018\u00010%2\u0006\u0010\u000f\u001a\u00020\u0010¢\u0006\u0002\u0010*J\u0015\u0010+\u001a\u0004\u0018\u00010%2\u0006\u0010\u000f\u001a\u00020\u0010¢\u0006\u0002\u0010*J\u0016\u0010,\u001a\u00020\u00142\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010-\u001a\u00020\u001aJ(\u0010.\u001a\u001c\u0012\u0004\u0012\u00020\u000b\u0012\u0012\u0012\u0010\u0012\u0004\u0012\u00020\u000b\u0012\u0006\u0012\u0004\u0018\u00010\u00010 0 2\u0006\u0010\u000f\u001a\u00020\u0010J\u001c\u0010/\u001a\u0010\u0012\u0004\u0012\u00020\u000b\u0012\u0006\u0012\u0004\u0018\u00010\u00010 2\u0006\u0010\u000f\u001a\u00020\u0010J\u000e\u00100\u001a\u00020\u00142\u0006\u0010\u000f\u001a\u00020\u0010J\u000e\u00101\u001a\u00020\u00142\u0006\u0010\u000f\u001a\u00020\u0010J2\u00102\u001a\u0016\u0012\u0012\u0012\u0010\u0012\u0004\u0012\u00020\u000b\u0012\u0006\u0012\u0004\u0018\u00010\u00010 032\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u00104\u001a\u00020%2\u0006\u00105\u001a\u00020%R\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u0004\u0018\u00010\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e¢\u0006\u0002\n\u0000¨\u00067"}, d2 = {"Lexpo/modules/shikhsha/WalkingStore;", "", "<init>", "()V", "helper", "Lexpo/modules/shikhsha/WalkingDatabase;", "clock", "Lexpo/modules/shikhsha/WalkingClock;", "current", "Lexpo/modules/shikhsha/WalkingStore$Bout;", "session", "", "db", "Landroid/database/sqlite/SQLiteDatabase;", "kotlin.jvm.PlatformType", "c", "Landroid/content/Context;", "prefs", "Landroid/content/SharedPreferences;", "begin", "", "detector", "", "recording", "coverage", "at", "", "setDetectorAvailable", "available", ViewProps.END, "id", "walk", "", Scopes.PROFILE, "weight", "", "age", "", "stride", "minutes", "calories", "minuteGoal", "(Landroid/content/Context;)Ljava/lang/Integer;", "calorieGoal", "step", "nanos", "days", "summary", "failure", "clearError", "bouts", "", "limit", TypedValues.Cycle.S_WAVE_OFFSET, "Bout", "shikhsha-pedometer_release"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class WalkingStore {
    public static final WalkingStore INSTANCE = new WalkingStore();
    private static WalkingClock clock = new WalkingClock(0, 1, null);
    private static Bout current;
    private static WalkingDatabase helper;
    private static String session;

    private WalkingStore() {
    }

    /* JADX INFO: compiled from: WalkingStore.kt */
    @Metadata(d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0006\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u001e\n\u0002\u0018\u0002\n\u0002\b\u0010\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0082\b\u0018\u00002\u00020\u0001B[\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u0012\u0006\u0010\b\u001a\u00020\u0003\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\u0003\u0012\u0006\u0010\f\u001a\u00020\n\u0012\b\u0010\r\u001a\u0004\u0018\u00010\n\u0012\b\u0010\u000e\u001a\u0004\u0018\u00010\u000f¢\u0006\u0004\b\u0010\u0010\u0011J\r\u00101\u001a\u0004\u0018\u00010\n¢\u0006\u0002\u0010$J\t\u00102\u001a\u00020\u0003HÆ\u0003J\t\u00103\u001a\u00020\u0005HÆ\u0003J\t\u00104\u001a\u00020\u0003HÆ\u0003J\t\u00105\u001a\u00020\u0003HÆ\u0003J\t\u00106\u001a\u00020\u0003HÆ\u0003J\t\u00107\u001a\u00020\nHÆ\u0003J\t\u00108\u001a\u00020\u0003HÆ\u0003J\t\u00109\u001a\u00020\nHÆ\u0003J\u0010\u0010:\u001a\u0004\u0018\u00010\nHÆ\u0003¢\u0006\u0002\u0010$J\u0010\u0010;\u001a\u0004\u0018\u00010\u000fHÆ\u0003¢\u0006\u0002\u0010)Jv\u0010<\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\u00032\b\b\u0002\u0010\f\u001a\u00020\n2\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\n2\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u000fHÆ\u0001¢\u0006\u0002\u0010=J\u0013\u0010>\u001a\u00020?2\b\u0010@\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010A\u001a\u00020\u000fHÖ\u0001J\t\u0010B\u001a\u00020\u0005HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0011\u0010\u0006\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0013R\u001a\u0010\u0007\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0013\"\u0004\b\u0018\u0010\u0019R\u001a\u0010\b\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u0013\"\u0004\b\u001b\u0010\u0019R\u001a\u0010\t\u001a\u00020\nX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u001d\"\u0004\b\u001e\u0010\u001fR\u001a\u0010\u000b\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b \u0010\u0013\"\u0004\b!\u0010\u0019R\u0011\u0010\f\u001a\u00020\n¢\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u001dR\u001e\u0010\r\u001a\u0004\u0018\u00010\nX\u0086\u000e¢\u0006\u0010\n\u0002\u0010'\u001a\u0004\b#\u0010$\"\u0004\b%\u0010&R\u001e\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u0086\u000e¢\u0006\u0010\n\u0002\u0010,\u001a\u0004\b(\u0010)\"\u0004\b*\u0010+R\u0011\u0010-\u001a\u00020.¢\u0006\b\n\u0000\u001a\u0004\b/\u00100¨\u0006C"}, d2 = {"Lexpo/modules/shikhsha/WalkingStore$Bout;", "", "id", "", "day", "", ViewProps.START, "last", "steps", "seconds", "", "timed", "stride", "weight", "age", "", "<init>", "(JLjava/lang/String;JJJDJDLjava/lang/Double;Ljava/lang/Integer;)V", "getId", "()J", "getDay", "()Ljava/lang/String;", "getStart", "getLast", "setLast", "(J)V", "getSteps", "setSteps", "getSeconds", "()D", "setSeconds", "(D)V", "getTimed", "setTimed", "getStride", "getWeight", "()Ljava/lang/Double;", "setWeight", "(Ljava/lang/Double;)V", "Ljava/lang/Double;", "getAge", "()Ljava/lang/Integer;", "setAge", "(Ljava/lang/Integer;)V", "Ljava/lang/Integer;", "energy", "Lexpo/modules/shikhsha/WalkingEnergyAccumulator;", "getEnergy", "()Lexpo/modules/shikhsha/WalkingEnergyAccumulator;", "kcal", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "component10", "copy", "(JLjava/lang/String;JJJDJDLjava/lang/Double;Ljava/lang/Integer;)Lexpo/modules/shikhsha/WalkingStore$Bout;", "equals", "", "other", "hashCode", "toString", "shikhsha-pedometer_release"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final /* data */ class Bout {
        private Integer age;
        private final String day;
        private final WalkingEnergyAccumulator energy;
        private final long id;
        private long last;
        private double seconds;
        private final long start;
        private long steps;
        private final double stride;
        private long timed;
        private Double weight;

        /* JADX INFO: renamed from: component1, reason: from getter */
        public final long getId() {
            return this.id;
        }

        /* JADX INFO: renamed from: component10, reason: from getter */
        public final Integer getAge() {
            return this.age;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final String getDay() {
            return this.day;
        }

        /* JADX INFO: renamed from: component3, reason: from getter */
        public final long getStart() {
            return this.start;
        }

        /* JADX INFO: renamed from: component4, reason: from getter */
        public final long getLast() {
            return this.last;
        }

        /* JADX INFO: renamed from: component5, reason: from getter */
        public final long getSteps() {
            return this.steps;
        }

        /* JADX INFO: renamed from: component6, reason: from getter */
        public final double getSeconds() {
            return this.seconds;
        }

        /* JADX INFO: renamed from: component7, reason: from getter */
        public final long getTimed() {
            return this.timed;
        }

        /* JADX INFO: renamed from: component8, reason: from getter */
        public final double getStride() {
            return this.stride;
        }

        /* JADX INFO: renamed from: component9, reason: from getter */
        public final Double getWeight() {
            return this.weight;
        }

        public final Bout copy(long id, String day, long start, long last, long steps, double seconds, long timed, double stride, Double weight, Integer age) {
            Intrinsics.checkNotNullParameter(day, "day");
            return new Bout(id, day, start, last, steps, seconds, timed, stride, weight, age);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Bout)) {
                return false;
            }
            Bout bout = (Bout) other;
            return this.id == bout.id && Intrinsics.areEqual(this.day, bout.day) && this.start == bout.start && this.last == bout.last && this.steps == bout.steps && Double.compare(this.seconds, bout.seconds) == 0 && this.timed == bout.timed && Double.compare(this.stride, bout.stride) == 0 && Intrinsics.areEqual((Object) this.weight, (Object) bout.weight) && Intrinsics.areEqual(this.age, bout.age);
        }

        public int hashCode() {
            int iHashCode = ((((((((((((((Long.hashCode(this.id) * 31) + this.day.hashCode()) * 31) + Long.hashCode(this.start)) * 31) + Long.hashCode(this.last)) * 31) + Long.hashCode(this.steps)) * 31) + Double.hashCode(this.seconds)) * 31) + Long.hashCode(this.timed)) * 31) + Double.hashCode(this.stride)) * 31;
            Double d = this.weight;
            int iHashCode2 = (iHashCode + (d == null ? 0 : d.hashCode())) * 31;
            Integer num = this.age;
            return iHashCode2 + (num != null ? num.hashCode() : 0);
        }

        public String toString() {
            return "Bout(id=" + this.id + ", day=" + this.day + ", start=" + this.start + ", last=" + this.last + ", steps=" + this.steps + ", seconds=" + this.seconds + ", timed=" + this.timed + ", stride=" + this.stride + ", weight=" + this.weight + ", age=" + this.age + ")";
        }

        public Bout(long j, String day, long j2, long j3, long j4, double d, long j5, double d2, Double d3, Integer num) {
            Intrinsics.checkNotNullParameter(day, "day");
            this.id = j;
            this.day = day;
            this.start = j2;
            this.last = j3;
            this.steps = j4;
            this.seconds = d;
            this.timed = j5;
            this.stride = d2;
            this.weight = d3;
            this.age = num;
            this.energy = new WalkingEnergyAccumulator(d2, this.weight, this.age);
        }

        public final Integer getAge() {
            return this.age;
        }

        public final String getDay() {
            return this.day;
        }

        public final long getId() {
            return this.id;
        }

        public final long getLast() {
            return this.last;
        }

        public final double getSeconds() {
            return this.seconds;
        }

        public final long getStart() {
            return this.start;
        }

        public final long getSteps() {
            return this.steps;
        }

        public final double getStride() {
            return this.stride;
        }

        public final long getTimed() {
            return this.timed;
        }

        public final Double getWeight() {
            return this.weight;
        }

        public final void setAge(Integer num) {
            this.age = num;
        }

        public final void setLast(long j) {
            this.last = j;
        }

        public final void setSeconds(double d) {
            this.seconds = d;
        }

        public final void setSteps(long j) {
            this.steps = j;
        }

        public final void setTimed(long j) {
            this.timed = j;
        }

        public final void setWeight(Double d) {
            this.weight = d;
        }

        public final WalkingEnergyAccumulator getEnergy() {
            return this.energy;
        }

        public final Double kcal() {
            return this.energy.getCalories();
        }
    }

    private final SQLiteDatabase db(Context c) {
        WalkingDatabase walkingDatabase = helper;
        if (walkingDatabase == null) {
            Context applicationContext = c.getApplicationContext();
            Intrinsics.checkNotNullExpressionValue(applicationContext, "getApplicationContext(...)");
            walkingDatabase = new WalkingDatabase(applicationContext);
            helper = walkingDatabase;
        }
        return walkingDatabase.getWritableDatabase();
    }

    private final SharedPreferences prefs(Context c) {
        return c.getSharedPreferences("shikhsha_motion_v1", 0);
    }

    public final synchronized void begin(Context c, boolean detector) {
        Intrinsics.checkNotNullParameter(c, "c");
        clock = new WalkingClock(SystemClock.elapsedRealtimeNanos());
        current = null;
        SharedPreferences sharedPreferencesPrefs = prefs(c);
        SharedPreferences.Editor editorPutBoolean = sharedPreferencesPrefs.edit().putBoolean("detector", detector);
        if (detector && !sharedPreferencesPrefs.contains("since")) {
            editorPutBoolean.putLong("since", System.currentTimeMillis());
        }
        if (!editorPutBoolean.commit()) {
            throw new IllegalStateException("Не удалось сохранить состояние активности.".toString());
        }
        String strDay = StepStore.INSTANCE.day(System.currentTimeMillis() - 7776000000L);
        db(c).delete("bouts", "day < ?", new String[]{strDay});
        db(c).delete("coverage", "day < ?", new String[]{strDay});
    }

    private final boolean recording(Context c) {
        if (StepService.INSTANCE.getDetectorRunning() && StepStore.INSTANCE.enabled(c)) {
            return Build.VERSION.SDK_INT < 29 || c.checkSelfPermission("android.permission.ACTIVITY_RECOGNITION") == 0;
        }
        return false;
    }

    private final void coverage(Context c, long at) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("day", StepStore.INSTANCE.day(at));
        contentValues.put("since", Long.valueOf(at));
        db(c).insertWithOnConflict("coverage", null, contentValues, 4);
    }

    public final synchronized void setDetectorAvailable(Context c, boolean available) {
        Intrinsics.checkNotNullParameter(c, "c");
        prefs(c).edit().putBoolean("detector", available).commit();
        if (available) {
            coverage(c, System.currentTimeMillis());
        }
    }

    public final synchronized void end() {
        current = null;
        session = null;
        clock = new WalkingClock(Long.MAX_VALUE);
    }

    public final synchronized void session(Context c, String id) {
        Intrinsics.checkNotNullParameter(c, "c");
        Intrinsics.checkNotNullParameter(id, "id");
        String str = !StringsKt.isBlank(id) ? id : null;
        if (id.length() > 100) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        if (!Intrinsics.areEqual(session, str)) {
            current = null;
            clock = new WalkingClock(SystemClock.elapsedRealtimeNanos());
            session = str;
        }
    }

    public final synchronized Map<String, Object> walk(Context c, String id) {
        Intrinsics.checkNotNullParameter(c, "c");
        Intrinsics.checkNotNullParameter(id, "id");
        boolean z = true;
        Cursor cursorRawQuery = db(c).rawQuery("SELECT COUNT(*),SUM(seconds),SUM(kcal),SUM(CASE WHEN missing>0 OR (kcal IS NULL AND seconds>0) THEN 1 ELSE 0 END) FROM bouts WHERE walk_id = ?", new String[]{id});
        try {
            Cursor cursor = cursorRawQuery;
            if (cursor.moveToFirst() && cursor.getInt(0) != 0) {
                Pair[] pairArr = new Pair[4];
                pairArr[0] = TuplesKt.to("seconds", Double.valueOf(cursor.getDouble(1)));
                pairArr[1] = TuplesKt.to("calories", cursor.isNull(2) ? null : Double.valueOf(cursor.getDouble(2)));
                pairArr[2] = TuplesKt.to("partialCalories", Boolean.valueOf(cursor.getInt(3) > 0));
                if (!Intrinsics.areEqual(session, id) || !INSTANCE.recording(c)) {
                    z = false;
                }
                pairArr[3] = TuplesKt.to("linked", Boolean.valueOf(z));
                Map<String, Object> mapMapOf = MapsKt.mapOf(pairArr);
                CloseableKt.closeFinally(cursorRawQuery, null);
                return mapMapOf;
            }
            CloseableKt.closeFinally(cursorRawQuery, null);
            return null;
        } catch (Throwable th) {
            try {
                throw th;
            } catch (Throwable th2) {
                CloseableKt.closeFinally(cursorRawQuery, th);
                throw th2;
            }
        }
    }

    public final synchronized void profile(Context c, double weight, int age, double stride, int minutes, int calories) {
        Intrinsics.checkNotNullParameter(c, "c");
        if (weight != 0.0d && (Math.abs(weight) > Double.MAX_VALUE || 30.0d > weight || weight > 300.0d)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        if (age != 0 && (18 > age || age >= 121)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        if (30.0d > stride || stride > 150.0d) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        if (minutes < 0 || minutes >= 1441 || calories < 0 || calories >= 10001) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        SharedPreferences sharedPreferencesPrefs = prefs(c);
        float f = (float) weight;
        if (sharedPreferencesPrefs.getFloat("weight", 0.0f) != f || sharedPreferencesPrefs.getInt("age", 0) != age || sharedPreferencesPrefs.getFloat("stride", 70.0f) != ((float) stride)) {
            current = null;
            clock = new WalkingClock(SystemClock.elapsedRealtimeNanos());
        }
        if (!sharedPreferencesPrefs.edit().putFloat("weight", f).putInt("age", age).putFloat("stride", (float) stride).putInt("minutes", minutes).putInt("calories", calories).commit()) {
            throw new IllegalStateException("Check failed.");
        }
        if (weight == 0.0d) {
            db(c).execSQL("UPDATE bouts SET weight=NULL, age=NULL, kcal=NULL WHERE weight IS NOT NULL OR age IS NOT NULL OR kcal IS NOT NULL");
            Bout bout = current;
            if (bout != null) {
                bout.setWeight(null);
                bout.setAge(null);
            }
        }
        WidgetUpdates.INSTANCE.request(c, true);
    }

    public final Integer minuteGoal(Context c) {
        Intrinsics.checkNotNullParameter(c, "c");
        Integer numValueOf = Integer.valueOf(prefs(c).getInt("minutes", 0));
        if (numValueOf.intValue() > 0) {
            return numValueOf;
        }
        return null;
    }

    public final Integer calorieGoal(Context c) {
        Intrinsics.checkNotNullParameter(c, "c");
        Integer numValueOf = Integer.valueOf(prefs(c).getInt("calories", 0));
        if (numValueOf.intValue() > 0) {
            return numValueOf;
        }
        return null;
    }

    /* JADX WARN: Type inference failed for: r5v4 */
    /* JADX WARN: Type inference failed for: r5v5, types: [boolean] */
    /* JADX WARN: Type inference failed for: r5v7 */
    public final synchronized void step(Context c, long nanos) {
        int i;
        ?? r5;
        int i2;
        Intrinsics.checkNotNullParameter(c, "c");
        long jCurrentTimeMillis = System.currentTimeMillis() - ((SystemClock.elapsedRealtimeNanos() - nanos) / 1000000);
        String strDay = StepStore.INSTANCE.day(jCurrentTimeMillis);
        WalkingClock walkingClock = clock;
        Intrinsics.checkNotNull(strDay);
        WalkingClock.Span spanStep = walkingClock.step(nanos, jCurrentTimeMillis, strDay);
        if (spanStep == null) {
            return;
        }
        SharedPreferences sharedPreferencesPrefs = prefs(c);
        if (Intrinsics.areEqual(sharedPreferencesPrefs.getString("cleanupDay", null), strDay)) {
            i = 1;
        } else {
            long j = jCurrentTimeMillis - 7776000000L;
            db(c).delete("bouts", "day < ?", new String[]{StepStore.INSTANCE.day(j)});
            i = 1;
            db(c).delete("coverage", "day < ?", new String[]{StepStore.INSTANCE.day(j)});
            coverage(c, jCurrentTimeMillis);
            sharedPreferencesPrefs.edit().putString("cleanupDay", strDay).apply();
        }
        SQLiteDatabase sQLiteDatabaseDb = db(c);
        sQLiteDatabaseDb.beginTransaction();
        try {
            if (spanStep.getNewBout() || current == null) {
                ContentValues contentValues = new ContentValues();
                String str = session;
                if (str != null) {
                    contentValues.put("walk_id", str);
                }
                contentValues.put("day", strDay);
                contentValues.put(ViewProps.START, Long.valueOf(jCurrentTimeMillis));
                contentValues.put("last", Long.valueOf(jCurrentTimeMillis));
                contentValues.put("steps", (Integer) 0);
                contentValues.put("seconds", Double.valueOf(0.0d));
                contentValues.put("timed", (Integer) 0);
                contentValues.put("stride", Double.valueOf(sharedPreferencesPrefs.getFloat("stride", 70.0f)));
                double d = sharedPreferencesPrefs.getFloat("weight", 0.0f);
                if (d > 0.0d) {
                    contentValues.put("weight", Double.valueOf(d));
                }
                int i3 = sharedPreferencesPrefs.getInt("age", 0);
                if (i3 > 0 && d > 0.0d) {
                    contentValues.put("age", Integer.valueOf(i3));
                }
                long jInsertOrThrow = db(c).insertOrThrow("bouts", null, contentValues);
                double d2 = sharedPreferencesPrefs.getFloat("stride", 70.0f);
                Double dValueOf = Double.valueOf(sharedPreferencesPrefs.getFloat("weight", 0.0f));
                Double d3 = dValueOf.doubleValue() > 0.0d ? dValueOf : null;
                Integer numValueOf = Integer.valueOf(sharedPreferencesPrefs.getInt("age", 0));
                Integer num = (numValueOf.intValue() <= 0 || sharedPreferencesPrefs.getFloat("weight", 0.0f) <= 0.0f) ? null : numValueOf;
                r5 = 0;
                i2 = 1;
                Bout bout = new Bout(jInsertOrThrow, strDay, jCurrentTimeMillis, jCurrentTimeMillis, 0L, 0.0d, 0L, d2, d3, num);
                jCurrentTimeMillis = jCurrentTimeMillis;
                current = bout;
            } else {
                r5 = 0;
                i2 = i;
            }
            Bout bout2 = current;
            Intrinsics.checkNotNull(bout2);
            bout2.setLast(jCurrentTimeMillis);
            bout2.setSteps(bout2.getSteps() + 1);
            bout2.setSeconds(bout2.getSeconds() + spanStep.getSeconds());
            if (spanStep.getSeconds() > 0.0d) {
                bout2.setTimed(bout2.getTimed() + 1);
            }
            bout2.getEnergy().add(spanStep.getSeconds());
            ContentValues contentValues2 = new ContentValues();
            contentValues2.put("missing", Integer.valueOf(bout2.getEnergy().getPartial() ? 1 : 0));
            contentValues2.put("last", Long.valueOf(bout2.getLast()));
            contentValues2.put("steps", Long.valueOf(bout2.getSteps()));
            contentValues2.put("seconds", Double.valueOf(bout2.getSeconds()));
            contentValues2.put("timed", Long.valueOf(bout2.getTimed()));
            Double dKcal = bout2.kcal();
            if (dKcal == null) {
                contentValues2.putNull("kcal");
            } else {
                contentValues2.put("kcal", dKcal);
            }
            SQLiteDatabase sQLiteDatabaseDb2 = db(c);
            String[] strArr = new String[i2];
            strArr[r5] = String.valueOf(bout2.getId());
            sQLiteDatabaseDb2.update("bouts", contentValues2, "id = ?", strArr);
            sQLiteDatabaseDb.setTransactionSuccessful();
            sQLiteDatabaseDb.endTransaction();
            WidgetUpdates.request$default(WidgetUpdates.INSTANCE, c, r5, 2, null);
        } catch (Throwable th) {
            sQLiteDatabaseDb.endTransaction();
            throw th;
        }
    }

    public final synchronized Map<String, Map<String, Object>> days(Context c) {
        LinkedHashMap linkedHashMap;
        char c2;
        try {
            Intrinsics.checkNotNullParameter(c, "c");
            linkedHashMap = new LinkedHashMap();
            Cursor cursorRawQuery = db(c).rawQuery("SELECT day,SUM(seconds),SUM(steps),SUM(kcal),SUM(CASE WHEN missing>0 OR (kcal IS NULL AND seconds>0) THEN 1 ELSE 0 END),MIN(start),MAX(last) FROM bouts GROUP BY day ORDER BY day", null);
            try {
                Cursor cursor = cursorRawQuery;
                while (true) {
                    c2 = 5;
                    if (!cursor.moveToNext()) {
                        break;
                    }
                    LinkedHashMap linkedHashMap2 = linkedHashMap;
                    String string = cursor.getString(0);
                    Pair[] pairArr = new Pair[6];
                    pairArr[0] = TuplesKt.to("seconds", Double.valueOf(cursor.getDouble(1)));
                    pairArr[1] = TuplesKt.to("steps", Long.valueOf(cursor.getLong(2)));
                    pairArr[2] = TuplesKt.to("calories", cursor.isNull(3) ? null : Double.valueOf(cursor.getDouble(3)));
                    pairArr[3] = TuplesKt.to("partialCalories", Boolean.valueOf(cursor.getInt(4) > 0));
                    pairArr[4] = TuplesKt.to("firstAt", Long.valueOf(cursor.getLong(5)));
                    pairArr[5] = TuplesKt.to("lastAt", Long.valueOf(cursor.getLong(6)));
                    linkedHashMap2.put(string, MapsKt.mapOf(pairArr));
                    throw th;
                }
                Unit unit = Unit.INSTANCE;
                CloseableKt.closeFinally(cursorRawQuery, null);
                Cursor cursorRawQuery2 = db(c).rawQuery("SELECT day FROM coverage ORDER BY day", null);
                try {
                    Cursor cursor2 = cursorRawQuery2;
                    while (cursor2.moveToNext()) {
                        if (!linkedHashMap.containsKey(cursor2.getString(0))) {
                            SharedPreferences sharedPreferencesPrefs = INSTANCE.prefs(c);
                            Double dNetKcal = WalkingEnergy.INSTANCE.netKcal(0.0d, 0L, 70.0d, Double.valueOf(sharedPreferencesPrefs.getFloat("weight", 0.0f)), Integer.valueOf(sharedPreferencesPrefs.getInt("age", 0)));
                            String string2 = cursor2.getString(0);
                            Pair[] pairArr2 = new Pair[6];
                            char c3 = c2;
                            pairArr2[0] = TuplesKt.to("seconds", Double.valueOf(0.0d));
                            pairArr2[1] = TuplesKt.to("steps", 0L);
                            pairArr2[2] = TuplesKt.to("calories", dNetKcal);
                            pairArr2[3] = TuplesKt.to("partialCalories", false);
                            pairArr2[4] = TuplesKt.to("firstAt", null);
                            pairArr2[c3] = TuplesKt.to("lastAt", null);
                            linkedHashMap.put(string2, MapsKt.mapOf(pairArr2));
                            c2 = c3;
                        }
                    }
                    Unit unit2 = Unit.INSTANCE;
                    CloseableKt.closeFinally(cursorRawQuery2, null);
                } catch (Throwable th) {
                    try {
                        throw th;
                    } catch (Throwable th2) {
                        CloseableKt.closeFinally(cursorRawQuery2, th);
                        throw th2;
                    }
                }
            } catch (Throwable th3) {
                try {
                    throw th3;
                } catch (Throwable th4) {
                    CloseableKt.closeFinally(cursorRawQuery, th3);
                    throw th4;
                }
            }
        } catch (Throwable th5) {
            throw th5;
        }
        return linkedHashMap;
    }

    public final synchronized Map<String, Object> summary(Context c) {
        Intrinsics.checkNotNullParameter(c, "c");
        return MapsKt.mapOf(TuplesKt.to("days", days(c)), TuplesKt.to("checked", Boolean.valueOf(prefs(c).contains("detector"))), TuplesKt.to("detectorAvailable", Boolean.valueOf(prefs(c).getBoolean("detector", false))), TuplesKt.to("running", Boolean.valueOf(recording(c))), TuplesKt.to("since", Long.valueOf(prefs(c).getLong("since", 0L))), TuplesKt.to("error", prefs(c).getString("error", null)));
    }

    public final synchronized void failure(Context c) {
        Intrinsics.checkNotNullParameter(c, "c");
        end();
        prefs(c).edit().putString("error", "Не удалось сохранить события шагов. Проверь свободное место и перезапусти счётчик.").commit();
    }

    public final synchronized void clearError(Context c) {
        Intrinsics.checkNotNullParameter(c, "c");
        prefs(c).edit().remove("error").commit();
    }

    public final synchronized List<Map<String, Object>> bouts(Context c, int limit, int offset) {
        ArrayList arrayList;
        Intrinsics.checkNotNullParameter(c, "c");
        if (1 > limit || limit >= 101 || offset < 0) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        arrayList = new ArrayList();
        Cursor cursorRawQuery = db(c).rawQuery("SELECT id,day,start,last,steps,seconds,stride,weight,age,kcal,missing FROM bouts ORDER BY start DESC,id DESC LIMIT ? OFFSET ?", new String[]{String.valueOf(limit), String.valueOf(offset)});
        try {
            Cursor cursor = cursorRawQuery;
            while (true) {
                Double dValueOf = null;
                if (cursor.moveToNext()) {
                    Pair[] pairArr = new Pair[11];
                    pairArr[0] = TuplesKt.to("id", String.valueOf(cursor.getLong(0)));
                    pairArr[1] = TuplesKt.to("day", cursor.getString(1));
                    pairArr[2] = TuplesKt.to(ViewProps.START, Long.valueOf(cursor.getLong(2)));
                    pairArr[3] = TuplesKt.to(ViewProps.END, Long.valueOf(cursor.getLong(3)));
                    pairArr[4] = TuplesKt.to("steps", Long.valueOf(cursor.getLong(4)));
                    pairArr[5] = TuplesKt.to("seconds", Double.valueOf(cursor.getDouble(5)));
                    pairArr[6] = TuplesKt.to("distance", Double.valueOf((cursor.getLong(4) * cursor.getDouble(6)) / 100.0d));
                    pairArr[7] = TuplesKt.to("weightKg", cursor.isNull(7) ? null : Double.valueOf(cursor.getDouble(7)));
                    pairArr[8] = TuplesKt.to("ageYears", cursor.isNull(8) ? null : Integer.valueOf(cursor.getInt(8)));
                    if (!cursor.isNull(9)) {
                        dValueOf = Double.valueOf(cursor.getDouble(9));
                    }
                    pairArr[9] = TuplesKt.to("calories", dValueOf);
                    pairArr[10] = TuplesKt.to("partialCalories", Boolean.valueOf(cursor.getInt(10) > 0 || (cursor.isNull(9) && cursor.getDouble(5) > 0.0d)));
                    arrayList.add(MapsKt.mapOf(pairArr));
                } else {
                    Unit unit = Unit.INSTANCE;
                    CloseableKt.closeFinally(cursorRawQuery, null);
                }
                throw th;
            }
        } catch (Throwable th) {
            try {
                throw th;
            } catch (Throwable th2) {
                CloseableKt.closeFinally(cursorRawQuery, th);
                throw th2;
            }
        }
        return arrayList;
    }
}
