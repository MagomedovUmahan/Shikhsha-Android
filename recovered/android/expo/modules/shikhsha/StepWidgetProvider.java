package expo.modules.shikhsha;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.SizeF;
import android.widget.RemoteViews;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.TuplesKt;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.collections.SetsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.maplibre.android.constants.MapLibreConstants;
import org.maplibre.android.style.layers.Property;

/* JADX INFO: compiled from: StepWidgetProvider.kt */
/* JADX INFO: loaded from: classes3.dex */
@Metadata(d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0016\u0018\u0000 \u00152\u00020\u0001:\u0001\u0015B\u0007¢\u0006\u0004\b\u0002\u0010\u0003J \u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0016J\u0010\u0010\f\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016J(\u0010\r\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0016J\u0018\u0010\u0012\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\u0013\u001a\u00020\u0014H\u0016¨\u0006\u0016"}, d2 = {"Lexpo/modules/shikhsha/StepWidgetProvider;", "Landroid/appwidget/AppWidgetProvider;", "<init>", "()V", "onUpdate", "", "context", "Landroid/content/Context;", "manager", "Landroid/appwidget/AppWidgetManager;", "ids", "", "onEnabled", "onAppWidgetOptionsChanged", "id", "", "options", "Landroid/os/Bundle;", "onReceive", "intent", "Landroid/content/Intent;", "Companion", "shikhsha-pedometer_release"}, k = 1, mv = {2, 2, 0}, xi = 48)
public class StepWidgetProvider extends AppWidgetProvider {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private static final List<Class<? extends StepWidgetProvider>> providers = CollectionsKt.listOf((Object[]) new Class[]{StepWidgetProvider.class, ActivityCompactWidgetProvider.class, ActivityWeekWidgetProvider.class});

    @Override // android.appwidget.AppWidgetProvider
    public void onUpdate(Context context, AppWidgetManager manager, int[] ids) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(manager, "manager");
        Intrinsics.checkNotNullParameter(ids, "ids");
        INSTANCE.updateAll(context);
    }

    @Override // android.appwidget.AppWidgetProvider
    public void onEnabled(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        INSTANCE.updateAll(context);
    }

    @Override // android.appwidget.AppWidgetProvider
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager manager, int id, Bundle options) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(manager, "manager");
        Intrinsics.checkNotNullParameter(options, "options");
        INSTANCE.updateAll(context);
    }

    @Override // android.appwidget.AppWidgetProvider, android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(intent, "intent");
        super.onReceive(context, intent);
        if (CollectionsKt.contains(SetsKt.setOf((Object[]) new String[]{"android.intent.action.DATE_CHANGED", "android.intent.action.TIME_SET", "android.intent.action.TIMEZONE_CHANGED", "android.intent.action.MY_PACKAGE_REPLACED", "android.intent.action.BOOT_COMPLETED"}), intent.getAction())) {
            INSTANCE.updateAll(context);
        }
    }

    /* JADX INFO: compiled from: StepWidgetProvider.kt */
    @Metadata(d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0015\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u001a\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\rH\u0002J\u0018\u0010\u000e\u001a\n \u0010*\u0004\u0018\u00010\u000f0\u000f2\u0006\u0010\n\u001a\u00020\u000bH\u0002J\u000e\u0010\u0011\u001a\u00020\u00122\u0006\u0010\n\u001a\u00020\u000bJ\u0016\u0010\u0013\u001a\u00020\u00142\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0015\u001a\u00020\u0016J\u0018\u0010\u0017\u001a\u00020\u00182\u0006\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\rJ\u000e\u0010\u0019\u001a\u00020\u00142\u0006\u0010\n\u001a\u00020\u000bR\u001c\u0010\u0004\u001a\u0010\u0012\f\u0012\n\u0012\u0006\b\u0001\u0012\u00020\u00070\u00060\u0005X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u001a"}, d2 = {"Lexpo/modules/shikhsha/StepWidgetProvider$Companion;", "", "<init>", "()V", "providers", "", "Ljava/lang/Class;", "Lexpo/modules/shikhsha/StepWidgetProvider;", "component", "Landroid/content/ComponentName;", "c", "Landroid/content/Context;", "kind", "", "prefs", "Landroid/content/SharedPreferences;", "kotlin.jvm.PlatformType", "ids", "", "saveGoal", "", "goal", "", "pin", "", "updateAll", "shikhsha-pedometer_release"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Companion {

        /* JADX INFO: compiled from: StepWidgetProvider.kt */
        @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
        public static final /* synthetic */ class WhenMappings {
            public static final /* synthetic */ int[] $EnumSwitchMapping$0;

            static {
                int[] iArr = new int[StepWidgetState.Status.values().length];
                try {
                    iArr[StepWidgetState.Status.ACTIVE.ordinal()] = 1;
                } catch (NoSuchFieldError unused) {
                }
                try {
                    iArr[StepWidgetState.Status.PAUSED.ordinal()] = 2;
                } catch (NoSuchFieldError unused2) {
                }
                try {
                    iArr[StepWidgetState.Status.STOPPED.ordinal()] = 3;
                } catch (NoSuchFieldError unused3) {
                }
                try {
                    iArr[StepWidgetState.Status.PERMISSION.ordinal()] = 4;
                } catch (NoSuchFieldError unused4) {
                }
                try {
                    iArr[StepWidgetState.Status.UNAVAILABLE.ordinal()] = 5;
                } catch (NoSuchFieldError unused5) {
                }
                try {
                    iArr[StepWidgetState.Status.ERROR.ordinal()] = 6;
                } catch (NoSuchFieldError unused6) {
                }
                try {
                    iArr[StepWidgetState.Status.NOT_STARTED.ordinal()] = 7;
                } catch (NoSuchFieldError unused7) {
                }
                $EnumSwitchMapping$0 = iArr;
            }
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        private final ComponentName component(Context c, String kind) {
            Class cls;
            if (Intrinsics.areEqual(kind, "compact")) {
                cls = ActivityCompactWidgetProvider.class;
            } else {
                cls = Intrinsics.areEqual(kind, "week") ? ActivityWeekWidgetProvider.class : StepWidgetProvider.class;
            }
            return new ComponentName(c, (Class<?>) cls);
        }

        static /* synthetic */ ComponentName component$default(Companion companion, Context context, String str, int i, Object obj) {
            if ((i & 2) != 0) {
                str = Property.LINE_CAP_SQUARE;
            }
            return companion.component(context, str);
        }

        private final SharedPreferences prefs(Context c) {
            return c.getSharedPreferences("shikhsha_widget_v1", 0);
        }

        public final int[] ids(Context c) {
            Intrinsics.checkNotNullParameter(c, "c");
            List list = StepWidgetProvider.providers;
            ArrayList arrayList = new ArrayList();
            Iterator it = list.iterator();
            while (it.hasNext()) {
                int[] appWidgetIds = AppWidgetManager.getInstance(c).getAppWidgetIds(new ComponentName(c, (Class<?>) it.next()));
                Intrinsics.checkNotNullExpressionValue(appWidgetIds, "getAppWidgetIds(...)");
                CollectionsKt.addAll(arrayList, ArraysKt.toList(appWidgetIds));
            }
            return CollectionsKt.toIntArray(arrayList);
        }

        public final void saveGoal(Context c, int goal) {
            Intrinsics.checkNotNullParameter(c, "c");
            if (1000 > goal || goal >= 50001) {
                throw new IllegalArgumentException("Failed requirement.".toString());
            }
            if (!prefs(c).edit().putInt("goal", goal).commit()) {
                throw new IllegalStateException("Check failed.");
            }
            WidgetUpdates.INSTANCE.request(c, true);
        }

        public static /* synthetic */ boolean pin$default(Companion companion, Context context, String str, int i, Object obj) {
            if ((i & 2) != 0) {
                str = Property.LINE_CAP_SQUARE;
            }
            return companion.pin(context, str);
        }

        public final boolean pin(Context c, String kind) {
            Intrinsics.checkNotNullParameter(c, "c");
            Intrinsics.checkNotNullParameter(kind, "kind");
            if (!SetsKt.setOf((Object[]) new String[]{Property.LINE_CAP_SQUARE, "compact", "week"}).contains(kind)) {
                throw new IllegalArgumentException("Failed requirement.".toString());
            }
            if (Build.VERSION.SDK_INT < 26) {
                return false;
            }
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(c);
            return appWidgetManager.isRequestPinAppWidgetSupported() && appWidgetManager.requestPinAppWidget(component(c, kind), null, null);
        }

        /* JADX WARN: Code duplicated, block: B:29:0x0096  */
        public final void updateAll(Context c) {
            Object objM783constructorimpl;
            Integer num;
            String str;
            String str2;
            Map map;
            Integer num2;
            String str3;
            Integer num3;
            Double d;
            Double d2;
            Map<String, Long> map2;
            Integer num4;
            NumberFormat numberFormat;
            String str4;
            Bitmap bitmap;
            StepWidgetState.Content content;
            Class cls;
            RemoteViews remoteViewsUpdateAll$render;
            AppWidgetManager appWidgetManager;
            Context c2 = c;
            Intrinsics.checkNotNullParameter(c2, "c");
            try {
                AppWidgetManager appWidgetManager2 = AppWidgetManager.getInstance(c2);
                Map<String, Long> mapHistory = StepStore.INSTANCE.history(c2);
                try {
                    Result.Companion companion = Result.INSTANCE;
                    Companion companion2 = this;
                    objM783constructorimpl = Result.m783constructorimpl(WalkingStore.INSTANCE.days(c2));
                } catch (Throwable th) {
                    Result.Companion companion3 = Result.INSTANCE;
                    objM783constructorimpl = Result.m783constructorimpl(ResultKt.createFailure(th));
                }
                Map mapEmptyMap = MapsKt.emptyMap();
                if (Result.m789isFailureimpl(objM783constructorimpl)) {
                    objM783constructorimpl = mapEmptyMap;
                }
                Map map3 = (Map) objM783constructorimpl;
                boolean z = Build.VERSION.SDK_INT < 29 || c2.checkSelfPermission("android.permission.ACTIVITY_RECOGNITION") == 0;
                Object systemService = c2.getSystemService("sensor");
                Intrinsics.checkNotNull(systemService, "null cannot be cast to non-null type android.hardware.SensorManager");
                boolean z2 = ((SensorManager) systemService).getDefaultSensor(19) != null;
                if (prefs(c).contains("goal")) {
                    Integer numValueOf = Integer.valueOf(prefs(c).getInt("goal", 0));
                    int iIntValue = numValueOf.intValue();
                    if (1000 > iIntValue || iIntValue >= 50001) {
                        num = null;
                    } else {
                        num = numValueOf;
                    }
                } else {
                    num = null;
                }
                String strDay$default = StepStore.day$default(StepStore.INSTANCE, 0L, 1, null);
                StepWidgetState.Content content2 = StepWidgetState.INSTANCE.content(mapHistory.get(strDay$default), num, StepStore.INSTANCE.enabled(c2), StepService.INSTANCE.getRunning(), z2, z, StepStore.INSTANCE.error(c2) != null, !mapHistory.isEmpty());
                Integer numMinuteGoal = WalkingStore.INSTANCE.minuteGoal(c2);
                Integer numCalorieGoal = WalkingStore.INSTANCE.calorieGoal(c2);
                NumberFormat integerInstance = NumberFormat.getIntegerInstance(Locale.forLanguageTag("ru-RU"));
                Map map4 = (Map) map3.get(strDay$default);
                Object obj = map4 != null ? map4.get("seconds") : null;
                Number number = obj instanceof Number ? (Number) obj : null;
                Double dValueOf = number != null ? Double.valueOf(number.doubleValue()) : null;
                Object obj2 = map4 != null ? map4.get("calories") : null;
                Number number2 = obj2 instanceof Number ? (Number) obj2 : null;
                Double dValueOf2 = number2 != null ? Double.valueOf(number2.doubleValue()) : null;
                WidgetRings widgetRings = WidgetRings.INSTANCE;
                Intrinsics.checkNotNull(strDay$default);
                Integer num5 = num;
                Map<String, Long> map5 = mapHistory;
                Bitmap bitmapBitmap$default = WidgetRings.bitmap$default(widgetRings, updateAll$fractions(map5, num5, map3, numMinuteGoal, numCalorieGoal, strDay$default), 0, 2, null);
                switch (WhenMappings.$EnumSwitchMapping$0[content2.getStatus().ordinal()]) {
                    case 1:
                        str = "Счётчик работает";
                        break;
                    case 2:
                        str = "На паузе";
                        break;
                    case 3:
                        str = "Проверь запуск";
                        break;
                    case 4:
                        str = "Разреши активность";
                        break;
                    case 5:
                        str = "Нет датчика шагов";
                        break;
                    case 6:
                        str = "Проверь ошибку";
                        break;
                    case 7:
                        str = "Открой Шихшу";
                        break;
                    default:
                        throw new NoWhenBranchMatchedException();
                }
                Integer num6 = numMinuteGoal;
                long jUpdatedAt = StepStore.INSTANCE.updatedAt(c2);
                String str5 = jUpdatedAt > 0 ? new SimpleDateFormat("HH:mm", Locale.US).format(new Date(jUpdatedAt)) : "—";
                for (Class cls2 : StepWidgetProvider.providers) {
                    int[] appWidgetIds = appWidgetManager2.getAppWidgetIds(new ComponentName(c2, (Class<?>) cls2));
                    int i = 0;
                    for (int length = appWidgetIds.length; i < length; length = length) {
                        StepWidgetState.Content content3 = content2;
                        int i2 = appWidgetIds[i];
                        NumberFormat numberFormat2 = integerInstance;
                        if (Build.VERSION.SDK_INT >= 31) {
                            bitmap = bitmapBitmap$default;
                            map = map4;
                            AppWidgetManager appWidgetManager3 = appWidgetManager2;
                            cls = cls2;
                            str2 = str;
                            content = content3;
                            num2 = num6;
                            str3 = str5;
                            num3 = numCalorieGoal;
                            d = dValueOf;
                            d2 = dValueOf2;
                            map2 = map5;
                            num4 = num5;
                            numberFormat = numberFormat2;
                            remoteViewsUpdateAll$render = new RemoteViews((Map<SizeF, RemoteViews>) MapsKt.mapOf(TuplesKt.to(new SizeF(130.0f, 64.0f), updateAll$render(c, bitmap, content, num4, numberFormat, d, num2, d2, map, num3, str2, str3, map2, map3, "compact")), TuplesKt.to(new SizeF(130.0f, 148.0f), updateAll$render(c, bitmap, content, num4, numberFormat, d, num2, d2, map, num3, str2, str3, map2, map3, Property.LINE_CAP_SQUARE)), TuplesKt.to(new SizeF(240.0f, 148.0f), updateAll$render(c, bitmap, content, num4, numberFormat, d, num2, d2, map, num3, str2, str3, map2, map3, "week"))));
                            appWidgetManager = appWidgetManager3;
                        } else {
                            AppWidgetManager appWidgetManager4 = appWidgetManager2;
                            Class cls3 = cls2;
                            Bitmap bitmap2 = bitmapBitmap$default;
                            str2 = str;
                            map = map4;
                            num2 = num6;
                            str3 = str5;
                            num3 = numCalorieGoal;
                            d = dValueOf;
                            d2 = dValueOf2;
                            map2 = map5;
                            num4 = num5;
                            numberFormat = numberFormat2;
                            Bundle appWidgetOptions = appWidgetManager4.getAppWidgetOptions(i2);
                            int i3 = appWidgetOptions.getInt("appWidgetMinWidth", MapLibreConstants.ANIMATION_DURATION_SHORT);
                            int i4 = appWidgetOptions.getInt("appWidgetMinHeight", Intrinsics.areEqual(cls3, ActivityCompactWidgetProvider.class) ? 64 : 160);
                            if (i3 < 240 || i4 < 140) {
                                str4 = i4 >= 140 ? Property.LINE_CAP_SQUARE : "compact";
                            } else {
                                str4 = "week";
                            }
                            bitmap = bitmap2;
                            content = content3;
                            cls = cls3;
                            remoteViewsUpdateAll$render = updateAll$render(c, bitmap, content, num4, numberFormat, d, num2, d2, map, num3, str2, str3, map2, map3, str4);
                            appWidgetManager = appWidgetManager4;
                        }
                        appWidgetManager.updateAppWidget(i2, remoteViewsUpdateAll$render);
                        Map<String, Long> map6 = map2;
                        i++;
                        content2 = content;
                        appWidgetManager2 = appWidgetManager;
                        map4 = map;
                        bitmapBitmap$default = bitmap;
                        integerInstance = numberFormat;
                        num5 = num4;
                        map5 = map6;
                        dValueOf = d;
                        num6 = num2;
                        dValueOf2 = d2;
                        numCalorieGoal = num3;
                        str5 = str3;
                        appWidgetIds = appWidgetIds;
                        cls2 = cls;
                        str = str2;
                    }
                    c2 = c;
                    numCalorieGoal = numCalorieGoal;
                    map4 = map4;
                    bitmapBitmap$default = bitmapBitmap$default;
                    integerInstance = integerInstance;
                    num5 = num5;
                    map5 = map5;
                }
            } catch (Exception unused) {
            }
        }

        private static final List<Double> updateAll$fractions(Map<String, Long> map, Integer num, Map<String, ? extends Map<String, ? extends Object>> map2, Integer num2, Integer num3, String str) {
            Double[] dArr = new Double[3];
            WidgetRings widgetRings = WidgetRings.INSTANCE;
            Long l = map.get(str);
            dArr[0] = Double.valueOf(widgetRings.fraction(l != null ? Double.valueOf(l.longValue()) : null, num != null ? Double.valueOf(num.intValue()) : null));
            WidgetRings widgetRings2 = WidgetRings.INSTANCE;
            Map<String, ? extends Object> map3 = map2.get(str);
            Object obj = map3 != null ? map3.get("seconds") : null;
            Number number = obj instanceof Number ? (Number) obj : null;
            dArr[1] = Double.valueOf(widgetRings2.fraction(number != null ? Double.valueOf(number.doubleValue() / 60.0d) : null, num2 != null ? Double.valueOf(num2.intValue()) : null));
            WidgetRings widgetRings3 = WidgetRings.INSTANCE;
            Map<String, ? extends Object> map4 = map2.get(str);
            Object obj2 = map4 != null ? map4.get("calories") : null;
            Number number2 = obj2 instanceof Number ? (Number) obj2 : null;
            dArr[2] = Double.valueOf(widgetRings3.fraction(number2 != null ? Double.valueOf(number2.doubleValue()) : null, num3 != null ? Double.valueOf(num3.intValue()) : null));
            return CollectionsKt.listOf((Object[]) dArr);
        }

        private static final RemoteViews updateAll$render(Context context, Bitmap bitmap, StepWidgetState.Content content, Integer num, NumberFormat numberFormat, Double d, Integer num2, Double d2, Map<String, ? extends Object> map, Integer num3, String str, String str2, Map<String, Long> map2, Map<String, ? extends Map<String, ? extends Object>> map3, String str3) {
            int i;
            String str4;
            double d3;
            Double dValueOf;
            String str5;
            Integer num4 = num;
            Integer num5 = num2;
            Integer num6 = num3;
            if (Intrinsics.areEqual(str3, "compact")) {
                i = R.layout.activity_widget_compact;
            } else {
                i = Intrinsics.areEqual(str3, "week") ? R.layout.activity_widget_week : R.layout.activity_widget_square;
            }
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), i);
            remoteViews.setImageViewBitmap(R.id.activity_rings, bitmap);
            boolean zAreEqual = Intrinsics.areEqual(str3, "week");
            Long steps = content.getSteps();
            if (steps == null || (str4 = numberFormat.format(steps.longValue())) == null) {
                str4 = "—";
            }
            String str6 = "";
            String str7 = str4 + ((!zAreEqual || num4 == null) ? "" : " / " + numberFormat.format(num4) + " шагов");
            if (d != null) {
                dValueOf = Double.valueOf(d.doubleValue() / 60.0d);
                d3 = 60.0d;
            } else {
                d3 = 60.0d;
                dValueOf = null;
            }
            String str8 = updateAll$render$count(numberFormat, dValueOf) + ((!zAreEqual || num5 == null) ? "" : " / " + num5) + " мин";
            String str9 = d2 != null ? "≈" : "";
            String strUpdateAll$render$count = updateAll$render$count(numberFormat, d2);
            String str10 = map != null ? Intrinsics.areEqual(map.get("partialCalories"), (Object) true) : false ? "+" : "";
            if (zAreEqual && num6 != null) {
                str6 = " / " + num6;
            }
            remoteViews.setTextViewText(R.id.activity_steps, str7);
            remoteViews.setTextViewText(R.id.activity_minutes, str8);
            remoteViews.setTextViewText(R.id.activity_calories, str9 + strUpdateAll$render$count + str10 + str6 + " ккал");
            remoteViews.setTextViewText(R.id.activity_status, str + " · " + str2);
            int i2 = R.id.activity_root;
            Long steps2 = content.getSteps();
            if (steps2 == null || (str5 = numberFormat.format(steps2.longValue())) == null) {
                str5 = "нет данных";
            }
            remoteViews.setContentDescription(i2, "Шихша. " + str + ". Шаги " + str5 + ". Время ходьбы " + updateAll$render$count(numberFormat, d != null ? Double.valueOf(d.doubleValue() / d3) : null) + " минут. Активные калории, оценка: " + updateAll$render$count(numberFormat, d2) + ". Данные " + str2 + ".");
            if (zAreEqual) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(6, -6);
                List listListOf = CollectionsKt.listOf((Object[]) new Integer[]{Integer.valueOf(R.id.activity_day_0), Integer.valueOf(R.id.activity_day_1), Integer.valueOf(R.id.activity_day_2), Integer.valueOf(R.id.activity_day_3), Integer.valueOf(R.id.activity_day_4), Integer.valueOf(R.id.activity_day_5), Integer.valueOf(R.id.activity_day_6)});
                List listListOf2 = CollectionsKt.listOf((Object[]) new Integer[]{Integer.valueOf(R.id.activity_week_0), Integer.valueOf(R.id.activity_week_1), Integer.valueOf(R.id.activity_week_2), Integer.valueOf(R.id.activity_week_3), Integer.valueOf(R.id.activity_week_4), Integer.valueOf(R.id.activity_week_5), Integer.valueOf(R.id.activity_week_6)});
                int i3 = 0;
                while (i3 < 7) {
                    String strDay = StepStore.INSTANCE.day(calendar.getTimeInMillis());
                    remoteViews.setTextViewText(((Number) listListOf.get(i3)).intValue(), new SimpleDateFormat("EE", Locale.forLanguageTag("ru-RU")).format(calendar.getTime()));
                    int iIntValue = ((Number) listListOf2.get(i3)).intValue();
                    WidgetRings widgetRings = WidgetRings.INSTANCE;
                    Intrinsics.checkNotNull(strDay);
                    remoteViews.setImageViewBitmap(iIntValue, widgetRings.bitmap(updateAll$fractions(map2, num4, map3, num5, num6, strDay), 64));
                    calendar.add(6, 1);
                    i3++;
                    num4 = num;
                    num5 = num2;
                    num6 = num3;
                }
            }
            Intent launchIntentForPackage = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
            if (launchIntentForPackage != null) {
                remoteViews.setOnClickPendingIntent(R.id.activity_root, PendingIntent.getActivity(context, 4102, launchIntentForPackage, 201326592));
            }
            return remoteViews;
        }

        private static final String updateAll$render$count(NumberFormat numberFormat, Double d) {
            String str;
            return (d == null || (str = numberFormat.format((long) Math.floor(d.doubleValue()))) == null) ? "—" : str;
        }
    }
}
