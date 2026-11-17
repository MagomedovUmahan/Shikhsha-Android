package expo.modules.shikhsha;

import android.app.ActivityManager;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.PowerManager;
import androidx.tracing.Trace;
import com.facebook.react.uimanager.ViewProps;
import expo.modules.interfaces.permissions.PermissionsResponse;
import expo.modules.kotlin.Promise;
import expo.modules.kotlin.functions.AsyncFunctionWithPromiseComponent;
import expo.modules.kotlin.functions.BoolAsyncFunctionComponent;
import expo.modules.kotlin.functions.DoubleAsyncFunctionComponent;
import expo.modules.kotlin.functions.FloatAsyncFunctionComponent;
import expo.modules.kotlin.functions.IntAsyncFunctionComponent;
import expo.modules.kotlin.functions.Queues;
import expo.modules.kotlin.functions.StringAsyncFunctionComponent;
import expo.modules.kotlin.functions.UntypedAsyncFunctionComponent;
import expo.modules.kotlin.modules.Module;
import expo.modules.kotlin.modules.ModuleDefinitionBuilder;
import expo.modules.kotlin.modules.ModuleDefinitionData;
import expo.modules.kotlin.types.AnyType;
import expo.modules.kotlin.types.AnyTypeCache;
import expo.modules.kotlin.types.TypeConverterProvider;
import expo.modules.kotlin.types.descriptors.TypeDescriptor;
import expo.modules.kotlin.types.descriptors.TypeDescriptorKt;
import expo.modules.kotlin.types.descriptors.TypeDescriptorOfKt;
import io.github.expo.pika.PTypeDescriptorRegistry;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.MapsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KType;

/* JADX INFO: compiled from: ShikhshaPedometerModule.kt */
/* JADX INFO: loaded from: classes3.dex */
@Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\b\u0010\u0004\u001a\u00020\u0005H\u0002J\u0010\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0005H\u0002J\u0010\u0010\t\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0005H\u0002J\u0016\u0010\n\u001a\u0010\u0012\u0004\u0012\u00020\f\u0012\u0006\u0012\u0004\u0018\u00010\r0\u000bH\u0002J\b\u0010\u000e\u001a\u00020\u000fH\u0016¨\u0006\u0010"}, d2 = {"Lexpo/modules/shikhsha/ShikhshaPedometerModule;", "Lexpo/modules/kotlin/modules/Module;", "<init>", "()V", "context", "Landroid/content/Context;", PermissionsResponse.GRANTED_KEY, "", "c", "available", "snapshot", "", "", "", "definition", "Lexpo/modules/kotlin/modules/ModuleDefinitionData;", "shikhsha-pedometer_release"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class ShikhshaPedometerModule extends Module {
    /* JADX INFO: Access modifiers changed from: private */
    public final Context context() {
        Context reactContext = getAppContext().getReactContext();
        if (reactContext != null) {
            return reactContext;
        }
        throw new IllegalStateException("Приложение ещё не готово.".toString());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean granted(Context c) {
        return Build.VERSION.SDK_INT < 29 || c.checkSelfPermission("android.permission.ACTIVITY_RECOGNITION") == 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean available(Context c) {
        Object systemService = c.getSystemService("sensor");
        Intrinsics.checkNotNull(systemService, "null cannot be cast to non-null type android.hardware.SensorManager");
        return ((SensorManager) systemService).getDefaultSensor(19) != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final Map<String, Object> snapshot() {
        Object objM783constructorimpl;
        Context context = context();
        WidgetUpdates.request$default(WidgetUpdates.INSTANCE, context, false, 2, null);
        boolean zGranted = granted(context);
        Pair[] pairArr = new Pair[8];
        pairArr[0] = TuplesKt.to("available", Boolean.valueOf(available(context)));
        pairArr[1] = TuplesKt.to("permission", zGranted ? PermissionsResponse.GRANTED_KEY : "denied");
        pairArr[2] = TuplesKt.to(ViewProps.ENABLED, Boolean.valueOf(StepStore.INSTANCE.enabled(context)));
        pairArr[3] = TuplesKt.to("running", Boolean.valueOf(StepStore.INSTANCE.enabled(context) && StepService.INSTANCE.getRunning() && zGranted));
        try {
            Result.Companion companion = Result.INSTANCE;
            ShikhshaPedometerModule shikhshaPedometerModule = this;
            objM783constructorimpl = Result.m783constructorimpl(WalkingStore.INSTANCE.summary(context));
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM783constructorimpl = Result.m783constructorimpl(ResultKt.createFailure(th));
        }
        if (Result.m786exceptionOrNullimpl(objM783constructorimpl) != null) {
            objM783constructorimpl = MapsKt.mapOf(TuplesKt.to("days", MapsKt.emptyMap()), TuplesKt.to("detectorAvailable", false), TuplesKt.to("running", false), TuplesKt.to("since", 0L), TuplesKt.to("error", "Не удалось прочитать активность. Проверь свободное место."));
        }
        pairArr[4] = TuplesKt.to("motion", objM783constructorimpl);
        pairArr[5] = TuplesKt.to("days", StepStore.INSTANCE.history(context));
        pairArr[6] = TuplesKt.to("updatedAt", Long.valueOf(StepStore.INSTANCE.updatedAt(context)));
        pairArr[7] = TuplesKt.to("error", StepStore.INSTANCE.error(context));
        return MapsKt.mapOf(pairArr);
    }

    @Override // expo.modules.kotlin.modules.Module
    public ModuleDefinitionData definition() {
        Object objM783constructorimpl;
        UntypedAsyncFunctionComponent stringAsyncFunctionComponent;
        AsyncFunctionWithPromiseComponent asyncFunctionWithPromiseComponent;
        Object objM783constructorimpl2;
        UntypedAsyncFunctionComponent untypedAsyncFunctionComponent;
        Object objM783constructorimpl3;
        Object objM783constructorimpl4;
        Object objM783constructorimpl5;
        Object objM783constructorimpl6;
        Object objM783constructorimpl7;
        UntypedAsyncFunctionComponent stringAsyncFunctionComponent2;
        Object objM783constructorimpl8;
        Object objM783constructorimpl9;
        UntypedAsyncFunctionComponent stringAsyncFunctionComponent3;
        Object objM783constructorimpl10;
        UntypedAsyncFunctionComponent stringAsyncFunctionComponent4;
        AsyncFunctionWithPromiseComponent asyncFunctionWithPromiseComponent2;
        Object objM783constructorimpl11;
        UntypedAsyncFunctionComponent stringAsyncFunctionComponent5;
        AsyncFunctionWithPromiseComponent asyncFunctionWithPromiseComponent3;
        Object objM783constructorimpl12;
        UntypedAsyncFunctionComponent stringAsyncFunctionComponent6;
        AsyncFunctionWithPromiseComponent asyncFunctionWithPromiseComponent4;
        ShikhshaPedometerModule shikhshaPedometerModule = this;
        Trace.beginSection("[ExpoModulesCore] " + (shikhshaPedometerModule.getClass() + ".ModuleDefinition"));
        try {
            ModuleDefinitionBuilder moduleDefinitionBuilder = new ModuleDefinitionBuilder(shikhshaPedometerModule);
            moduleDefinitionBuilder.Name("ShikhshaPedometer");
            ModuleDefinitionBuilder moduleDefinitionBuilder2 = moduleDefinitionBuilder;
            Object obj = null;
            if (Intrinsics.areEqual(String.class, Promise.class)) {
                asyncFunctionWithPromiseComponent = new AsyncFunctionWithPromiseComponent("setMotionSession", new AnyType[0], new Function2<Object[], Promise, Unit>() { // from class: expo.modules.shikhsha.ShikhshaPedometerModule$definition$lambda$21$$inlined$AsyncFunction$1
                    /* JADX WARN: Multi-variable type inference failed */
                    /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
                    public final void invoke2(Object[] objArr, Promise promise) {
                        Intrinsics.checkNotNullParameter(objArr, "<unused var>");
                        Intrinsics.checkNotNullParameter(promise, "promise");
                        WalkingStore.INSTANCE.session(this.this$0.context(), (String) promise);
                    }

                    @Override // kotlin.jvm.functions.Function2
                    public /* bridge */ /* synthetic */ Unit invoke(Object[] objArr, Promise promise) {
                        invoke2(objArr, promise);
                        return Unit.INSTANCE;
                    }
                });
            } else {
                TypeConverterProvider converters = moduleDefinitionBuilder2.getConverters();
                AnyType[] anyTypeArr = new AnyType[1];
                AnyType anyType = AnyTypeCache.INSTANCE.getTypesMap().get(new Pair(Reflection.getOrCreateKotlinClass(String.class), false));
                if (anyType == null) {
                    try {
                        Result.Companion companion = Result.INSTANCE;
                        objM783constructorimpl = Result.m783constructorimpl(new TypeDescriptor(TypeDescriptorOfKt.toRawTypeDescriptor(PTypeDescriptorRegistry.STRING), new Function0<KType>() { // from class: expo.modules.shikhsha.ShikhshaPedometerModule$definition$lambda$21$$inlined$AsyncFunction$2
                            @Override // kotlin.jvm.functions.Function0
                            public final KType invoke() {
                                return Reflection.typeOf(String.class);
                            }
                        }));
                    } catch (Throwable th) {
                        Result.Companion companion2 = Result.INSTANCE;
                        objM783constructorimpl = Result.m783constructorimpl(ResultKt.createFailure(th));
                    }
                    if (Result.m789isFailureimpl(objM783constructorimpl)) {
                        objM783constructorimpl = null;
                    }
                    TypeDescriptor typeDescriptor = (TypeDescriptor) objM783constructorimpl;
                    if (typeDescriptor == null) {
                        typeDescriptor = TypeDescriptorKt.toTypeDescriptor(Reflection.typeOf(String.class));
                    }
                    anyType = new AnyType(typeDescriptor, converters);
                }
                anyTypeArr[0] = anyType;
                Function1<Object[], Unit> function1 = new Function1<Object[], Unit>() { // from class: expo.modules.shikhsha.ShikhshaPedometerModule$definition$lambda$21$$inlined$AsyncFunction$3
                    @Override // kotlin.jvm.functions.Function1
                    public final Unit invoke(Object[] objArr) {
                        Intrinsics.checkNotNullParameter(objArr, "<destruct>");
                        WalkingStore.INSTANCE.session(this.this$0.context(), (String) objArr[0]);
                        return Unit.INSTANCE;
                    }
                };
                if (Intrinsics.areEqual(Unit.class, Integer.TYPE)) {
                    stringAsyncFunctionComponent = new IntAsyncFunctionComponent("setMotionSession", anyTypeArr, function1);
                } else if (Intrinsics.areEqual(Unit.class, Boolean.TYPE)) {
                    stringAsyncFunctionComponent = new BoolAsyncFunctionComponent("setMotionSession", anyTypeArr, function1);
                } else if (Intrinsics.areEqual(Unit.class, Double.TYPE)) {
                    stringAsyncFunctionComponent = new DoubleAsyncFunctionComponent("setMotionSession", anyTypeArr, function1);
                } else if (Intrinsics.areEqual(Unit.class, Float.TYPE)) {
                    stringAsyncFunctionComponent = new FloatAsyncFunctionComponent("setMotionSession", anyTypeArr, function1);
                } else {
                    stringAsyncFunctionComponent = Intrinsics.areEqual(Unit.class, String.class) ? new StringAsyncFunctionComponent("setMotionSession", anyTypeArr, function1) : new UntypedAsyncFunctionComponent("setMotionSession", anyTypeArr, function1);
                }
                asyncFunctionWithPromiseComponent = stringAsyncFunctionComponent;
            }
            moduleDefinitionBuilder2.getAsyncFunctions().put("setMotionSession", asyncFunctionWithPromiseComponent);
            ModuleDefinitionBuilder moduleDefinitionBuilder3 = moduleDefinitionBuilder;
            if (Intrinsics.areEqual(String.class, Promise.class)) {
                untypedAsyncFunctionComponent = new AsyncFunctionWithPromiseComponent("getWalkMotion", new AnyType[0], new Function2<Object[], Promise, Unit>() { // from class: expo.modules.shikhsha.ShikhshaPedometerModule$definition$lambda$21$$inlined$AsyncFunction$4
                    /* JADX WARN: Multi-variable type inference failed */
                    /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
                    public final void invoke2(Object[] objArr, Promise promise) {
                        Intrinsics.checkNotNullParameter(objArr, "<unused var>");
                        Intrinsics.checkNotNullParameter(promise, "promise");
                        WalkingStore.INSTANCE.walk(this.this$0.context(), (String) promise);
                    }

                    @Override // kotlin.jvm.functions.Function2
                    public /* bridge */ /* synthetic */ Unit invoke(Object[] objArr, Promise promise) {
                        invoke2(objArr, promise);
                        return Unit.INSTANCE;
                    }
                });
            } else {
                TypeConverterProvider converters2 = moduleDefinitionBuilder3.getConverters();
                AnyType[] anyTypeArr2 = new AnyType[1];
                AnyType anyType2 = AnyTypeCache.INSTANCE.getTypesMap().get(new Pair(Reflection.getOrCreateKotlinClass(String.class), false));
                if (anyType2 == null) {
                    try {
                        Result.Companion companion3 = Result.INSTANCE;
                        objM783constructorimpl2 = Result.m783constructorimpl(new TypeDescriptor(TypeDescriptorOfKt.toRawTypeDescriptor(PTypeDescriptorRegistry.STRING), new Function0<KType>() { // from class: expo.modules.shikhsha.ShikhshaPedometerModule$definition$lambda$21$$inlined$AsyncFunction$5
                            @Override // kotlin.jvm.functions.Function0
                            public final KType invoke() {
                                return Reflection.typeOf(String.class);
                            }
                        }));
                    } catch (Throwable th2) {
                        Result.Companion companion4 = Result.INSTANCE;
                        objM783constructorimpl2 = Result.m783constructorimpl(ResultKt.createFailure(th2));
                    }
                    if (Result.m789isFailureimpl(objM783constructorimpl2)) {
                        objM783constructorimpl2 = null;
                    }
                    TypeDescriptor typeDescriptor2 = (TypeDescriptor) objM783constructorimpl2;
                    if (typeDescriptor2 == null) {
                        typeDescriptor2 = TypeDescriptorKt.toTypeDescriptor(Reflection.typeOf(String.class));
                    }
                    anyType2 = new AnyType(typeDescriptor2, converters2);
                }
                anyTypeArr2[0] = anyType2;
                untypedAsyncFunctionComponent = new UntypedAsyncFunctionComponent("getWalkMotion", anyTypeArr2, new Function1<Object[], Map<String, ? extends Object>>() { // from class: expo.modules.shikhsha.ShikhshaPedometerModule$definition$lambda$21$$inlined$AsyncFunction$6
                    @Override // kotlin.jvm.functions.Function1
                    public final Map<String, ? extends Object> invoke(Object[] objArr) {
                        Intrinsics.checkNotNullParameter(objArr, "<destruct>");
                        return WalkingStore.INSTANCE.walk(this.this$0.context(), (String) objArr[0]);
                    }
                });
            }
            moduleDefinitionBuilder3.getAsyncFunctions().put("getWalkMotion", untypedAsyncFunctionComponent);
            ModuleDefinitionBuilder moduleDefinitionBuilder4 = moduleDefinitionBuilder;
            TypeConverterProvider converters3 = moduleDefinitionBuilder4.getConverters();
            AnyType[] anyTypeArr3 = new AnyType[5];
            AnyType anyType3 = AnyTypeCache.INSTANCE.getTypesMap().get(new Pair(Reflection.getOrCreateKotlinClass(Double.class), false));
            if (anyType3 == null) {
                try {
                    Result.Companion companion5 = Result.INSTANCE;
                    objM783constructorimpl3 = Result.m783constructorimpl(new TypeDescriptor(TypeDescriptorOfKt.toRawTypeDescriptor(PTypeDescriptorRegistry.DOUBLE), new Function0<KType>() { // from class: expo.modules.shikhsha.ShikhshaPedometerModule$definition$lambda$21$$inlined$AsyncFunction$7
                        @Override // kotlin.jvm.functions.Function0
                        public final KType invoke() {
                            return Reflection.typeOf(Double.TYPE);
                        }
                    }));
                } catch (Throwable th3) {
                    Result.Companion companion6 = Result.INSTANCE;
                    objM783constructorimpl3 = Result.m783constructorimpl(ResultKt.createFailure(th3));
                }
                if (Result.m789isFailureimpl(objM783constructorimpl3)) {
                    objM783constructorimpl3 = null;
                }
                TypeDescriptor typeDescriptor3 = (TypeDescriptor) objM783constructorimpl3;
                if (typeDescriptor3 == null) {
                    typeDescriptor3 = TypeDescriptorKt.toTypeDescriptor(Reflection.typeOf(Double.TYPE));
                }
                anyType3 = new AnyType(typeDescriptor3, converters3);
            }
            anyTypeArr3[0] = anyType3;
            AnyType anyType4 = AnyTypeCache.INSTANCE.getTypesMap().get(new Pair(Reflection.getOrCreateKotlinClass(Integer.class), false));
            if (anyType4 == null) {
                try {
                    Result.Companion companion7 = Result.INSTANCE;
                    objM783constructorimpl4 = Result.m783constructorimpl(new TypeDescriptor(TypeDescriptorOfKt.toRawTypeDescriptor(PTypeDescriptorRegistry.INT), new Function0<KType>() { // from class: expo.modules.shikhsha.ShikhshaPedometerModule$definition$lambda$21$$inlined$AsyncFunction$8
                        @Override // kotlin.jvm.functions.Function0
                        public final KType invoke() {
                            return Reflection.typeOf(Integer.TYPE);
                        }
                    }));
                } catch (Throwable th4) {
                    Result.Companion companion8 = Result.INSTANCE;
                    objM783constructorimpl4 = Result.m783constructorimpl(ResultKt.createFailure(th4));
                }
                if (Result.m789isFailureimpl(objM783constructorimpl4)) {
                    objM783constructorimpl4 = null;
                }
                TypeDescriptor typeDescriptor4 = (TypeDescriptor) objM783constructorimpl4;
                if (typeDescriptor4 == null) {
                    typeDescriptor4 = TypeDescriptorKt.toTypeDescriptor(Reflection.typeOf(Integer.TYPE));
                }
                anyType4 = new AnyType(typeDescriptor4, converters3);
            }
            anyTypeArr3[1] = anyType4;
            AnyType anyType5 = AnyTypeCache.INSTANCE.getTypesMap().get(new Pair(Reflection.getOrCreateKotlinClass(Double.class), false));
            if (anyType5 == null) {
                try {
                    Result.Companion companion9 = Result.INSTANCE;
                    objM783constructorimpl5 = Result.m783constructorimpl(new TypeDescriptor(TypeDescriptorOfKt.toRawTypeDescriptor(PTypeDescriptorRegistry.DOUBLE), new Function0<KType>() { // from class: expo.modules.shikhsha.ShikhshaPedometerModule$definition$lambda$21$$inlined$AsyncFunction$9
                        @Override // kotlin.jvm.functions.Function0
                        public final KType invoke() {
                            return Reflection.typeOf(Double.TYPE);
                        }
                    }));
                } catch (Throwable th5) {
                    Result.Companion companion10 = Result.INSTANCE;
                    objM783constructorimpl5 = Result.m783constructorimpl(ResultKt.createFailure(th5));
                }
                if (Result.m789isFailureimpl(objM783constructorimpl5)) {
                    objM783constructorimpl5 = null;
                }
                TypeDescriptor typeDescriptor5 = (TypeDescriptor) objM783constructorimpl5;
                if (typeDescriptor5 == null) {
                    typeDescriptor5 = TypeDescriptorKt.toTypeDescriptor(Reflection.typeOf(Double.TYPE));
                }
                anyType5 = new AnyType(typeDescriptor5, converters3);
            }
            anyTypeArr3[2] = anyType5;
            AnyType anyType6 = AnyTypeCache.INSTANCE.getTypesMap().get(new Pair(Reflection.getOrCreateKotlinClass(Integer.class), false));
            if (anyType6 == null) {
                try {
                    Result.Companion companion11 = Result.INSTANCE;
                    objM783constructorimpl6 = Result.m783constructorimpl(new TypeDescriptor(TypeDescriptorOfKt.toRawTypeDescriptor(PTypeDescriptorRegistry.INT), new Function0<KType>() { // from class: expo.modules.shikhsha.ShikhshaPedometerModule$definition$lambda$21$$inlined$AsyncFunction$10
                        @Override // kotlin.jvm.functions.Function0
                        public final KType invoke() {
                            return Reflection.typeOf(Integer.TYPE);
                        }
                    }));
                } catch (Throwable th6) {
                    Result.Companion companion12 = Result.INSTANCE;
                    objM783constructorimpl6 = Result.m783constructorimpl(ResultKt.createFailure(th6));
                }
                if (Result.m789isFailureimpl(objM783constructorimpl6)) {
                    objM783constructorimpl6 = null;
                }
                TypeDescriptor typeDescriptor6 = (TypeDescriptor) objM783constructorimpl6;
                if (typeDescriptor6 == null) {
                    typeDescriptor6 = TypeDescriptorKt.toTypeDescriptor(Reflection.typeOf(Integer.TYPE));
                }
                anyType6 = new AnyType(typeDescriptor6, converters3);
            }
            anyTypeArr3[3] = anyType6;
            AnyType anyType7 = AnyTypeCache.INSTANCE.getTypesMap().get(new Pair(Reflection.getOrCreateKotlinClass(Integer.class), false));
            if (anyType7 == null) {
                try {
                    Result.Companion companion13 = Result.INSTANCE;
                    objM783constructorimpl7 = Result.m783constructorimpl(new TypeDescriptor(TypeDescriptorOfKt.toRawTypeDescriptor(PTypeDescriptorRegistry.INT), new Function0<KType>() { // from class: expo.modules.shikhsha.ShikhshaPedometerModule$definition$lambda$21$$inlined$AsyncFunction$11
                        @Override // kotlin.jvm.functions.Function0
                        public final KType invoke() {
                            return Reflection.typeOf(Integer.TYPE);
                        }
                    }));
                } catch (Throwable th7) {
                    Result.Companion companion14 = Result.INSTANCE;
                    objM783constructorimpl7 = Result.m783constructorimpl(ResultKt.createFailure(th7));
                }
                if (Result.m789isFailureimpl(objM783constructorimpl7)) {
                    objM783constructorimpl7 = null;
                }
                TypeDescriptor typeDescriptor7 = (TypeDescriptor) objM783constructorimpl7;
                if (typeDescriptor7 == null) {
                    typeDescriptor7 = TypeDescriptorKt.toTypeDescriptor(Reflection.typeOf(Integer.TYPE));
                }
                anyType7 = new AnyType(typeDescriptor7, converters3);
            }
            anyTypeArr3[4] = anyType7;
            Function1<Object[], Unit> function2 = new Function1<Object[], Unit>() { // from class: expo.modules.shikhsha.ShikhshaPedometerModule$definition$lambda$21$$inlined$AsyncFunction$12
                @Override // kotlin.jvm.functions.Function1
                public final Unit invoke(Object[] objArr) {
                    Intrinsics.checkNotNullParameter(objArr, "<destruct>");
                    Object obj2 = objArr[0];
                    Object obj3 = objArr[1];
                    Object obj4 = objArr[2];
                    Object obj5 = objArr[3];
                    int iIntValue = ((Number) objArr[4]).intValue();
                    int iIntValue2 = ((Number) obj5).intValue();
                    double dDoubleValue = ((Number) obj4).doubleValue();
                    int iIntValue3 = ((Number) obj3).intValue();
                    WalkingStore.INSTANCE.profile(this.this$0.context(), ((Number) obj2).doubleValue(), iIntValue3, dDoubleValue, iIntValue2, iIntValue);
                    return Unit.INSTANCE;
                }
            };
            if (Intrinsics.areEqual(Unit.class, Integer.TYPE)) {
                stringAsyncFunctionComponent2 = new IntAsyncFunctionComponent("setActivityProfile", anyTypeArr3, function2);
            } else if (Intrinsics.areEqual(Unit.class, Boolean.TYPE)) {
                stringAsyncFunctionComponent2 = new BoolAsyncFunctionComponent("setActivityProfile", anyTypeArr3, function2);
            } else if (Intrinsics.areEqual(Unit.class, Double.TYPE)) {
                stringAsyncFunctionComponent2 = new DoubleAsyncFunctionComponent("setActivityProfile", anyTypeArr3, function2);
            } else if (Intrinsics.areEqual(Unit.class, Float.TYPE)) {
                stringAsyncFunctionComponent2 = new FloatAsyncFunctionComponent("setActivityProfile", anyTypeArr3, function2);
            } else {
                stringAsyncFunctionComponent2 = Intrinsics.areEqual(Unit.class, String.class) ? new StringAsyncFunctionComponent("setActivityProfile", anyTypeArr3, function2) : new UntypedAsyncFunctionComponent("setActivityProfile", anyTypeArr3, function2);
            }
            moduleDefinitionBuilder4.getAsyncFunctions().put("setActivityProfile", stringAsyncFunctionComponent2);
            ModuleDefinitionBuilder moduleDefinitionBuilder5 = moduleDefinitionBuilder;
            TypeConverterProvider converters4 = moduleDefinitionBuilder5.getConverters();
            AnyType[] anyTypeArr4 = new AnyType[2];
            AnyType anyType8 = AnyTypeCache.INSTANCE.getTypesMap().get(new Pair(Reflection.getOrCreateKotlinClass(Integer.class), false));
            if (anyType8 == null) {
                try {
                    Result.Companion companion15 = Result.INSTANCE;
                    objM783constructorimpl8 = Result.m783constructorimpl(new TypeDescriptor(TypeDescriptorOfKt.toRawTypeDescriptor(PTypeDescriptorRegistry.INT), new Function0<KType>() { // from class: expo.modules.shikhsha.ShikhshaPedometerModule$definition$lambda$21$$inlined$AsyncFunction$13
                        @Override // kotlin.jvm.functions.Function0
                        public final KType invoke() {
                            return Reflection.typeOf(Integer.TYPE);
                        }
                    }));
                } catch (Throwable th8) {
                    Result.Companion companion16 = Result.INSTANCE;
                    objM783constructorimpl8 = Result.m783constructorimpl(ResultKt.createFailure(th8));
                }
                if (Result.m789isFailureimpl(objM783constructorimpl8)) {
                    objM783constructorimpl8 = null;
                }
                TypeDescriptor typeDescriptor8 = (TypeDescriptor) objM783constructorimpl8;
                if (typeDescriptor8 == null) {
                    typeDescriptor8 = TypeDescriptorKt.toTypeDescriptor(Reflection.typeOf(Integer.TYPE));
                }
                anyType8 = new AnyType(typeDescriptor8, converters4);
            }
            anyTypeArr4[0] = anyType8;
            AnyType anyType9 = AnyTypeCache.INSTANCE.getTypesMap().get(new Pair(Reflection.getOrCreateKotlinClass(Integer.class), false));
            if (anyType9 == null) {
                try {
                    Result.Companion companion17 = Result.INSTANCE;
                    objM783constructorimpl9 = Result.m783constructorimpl(new TypeDescriptor(TypeDescriptorOfKt.toRawTypeDescriptor(PTypeDescriptorRegistry.INT), new Function0<KType>() { // from class: expo.modules.shikhsha.ShikhshaPedometerModule$definition$lambda$21$$inlined$AsyncFunction$14
                        @Override // kotlin.jvm.functions.Function0
                        public final KType invoke() {
                            return Reflection.typeOf(Integer.TYPE);
                        }
                    }));
                } catch (Throwable th9) {
                    Result.Companion companion18 = Result.INSTANCE;
                    objM783constructorimpl9 = Result.m783constructorimpl(ResultKt.createFailure(th9));
                }
                if (Result.m789isFailureimpl(objM783constructorimpl9)) {
                    objM783constructorimpl9 = null;
                }
                TypeDescriptor typeDescriptor9 = (TypeDescriptor) objM783constructorimpl9;
                if (typeDescriptor9 == null) {
                    typeDescriptor9 = TypeDescriptorKt.toTypeDescriptor(Reflection.typeOf(Integer.TYPE));
                }
                anyType9 = new AnyType(typeDescriptor9, converters4);
            }
            anyTypeArr4[1] = anyType9;
            Function1<Object[], List<? extends Map<String, ? extends Object>>> function3 = new Function1<Object[], List<? extends Map<String, ? extends Object>>>() { // from class: expo.modules.shikhsha.ShikhshaPedometerModule$definition$lambda$21$$inlined$AsyncFunction$15
                @Override // kotlin.jvm.functions.Function1
                public final List<? extends Map<String, ? extends Object>> invoke(Object[] objArr) {
                    Intrinsics.checkNotNullParameter(objArr, "<destruct>");
                    Object obj2 = objArr[0];
                    int iIntValue = ((Number) objArr[1]).intValue();
                    return WalkingStore.INSTANCE.bouts(this.this$0.context(), ((Number) obj2).intValue(), iIntValue);
                }
            };
            if (Intrinsics.areEqual(List.class, Integer.TYPE)) {
                stringAsyncFunctionComponent3 = new IntAsyncFunctionComponent("getMotionBouts", anyTypeArr4, function3);
            } else if (Intrinsics.areEqual(List.class, Boolean.TYPE)) {
                stringAsyncFunctionComponent3 = new BoolAsyncFunctionComponent("getMotionBouts", anyTypeArr4, function3);
            } else if (Intrinsics.areEqual(List.class, Double.TYPE)) {
                stringAsyncFunctionComponent3 = new DoubleAsyncFunctionComponent("getMotionBouts", anyTypeArr4, function3);
            } else if (Intrinsics.areEqual(List.class, Float.TYPE)) {
                stringAsyncFunctionComponent3 = new FloatAsyncFunctionComponent("getMotionBouts", anyTypeArr4, function3);
            } else {
                stringAsyncFunctionComponent3 = Intrinsics.areEqual(List.class, String.class) ? new StringAsyncFunctionComponent("getMotionBouts", anyTypeArr4, function3) : new UntypedAsyncFunctionComponent("getMotionBouts", anyTypeArr4, function3);
            }
            moduleDefinitionBuilder5.getAsyncFunctions().put("getMotionBouts", stringAsyncFunctionComponent3);
            moduleDefinitionBuilder.getAsyncFunctions().put("getBackgroundStatus", new UntypedAsyncFunctionComponent("getBackgroundStatus", new AnyType[0], new Function1<Object[], Object>() { // from class: expo.modules.shikhsha.ShikhshaPedometerModule$definition$lambda$21$$inlined$AsyncFunctionWithoutArgs$1
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object[] it) {
                    Intrinsics.checkNotNullParameter(it, "it");
                    Context context = this.this$0.context();
                    Pair[] pairArr = new Pair[3];
                    boolean z = false;
                    pairArr[0] = TuplesKt.to("resumeAfterBoot", Boolean.valueOf(StepStore.INSTANCE.resumeAfterBoot(context)));
                    Object systemService = context.getSystemService("power");
                    Intrinsics.checkNotNull(systemService, "null cannot be cast to non-null type android.os.PowerManager");
                    pairArr[1] = TuplesKt.to("batteryUnrestricted", Boolean.valueOf(((PowerManager) systemService).isIgnoringBatteryOptimizations(context.getPackageName())));
                    if (Build.VERSION.SDK_INT >= 28) {
                        Object systemService2 = context.getSystemService("activity");
                        Intrinsics.checkNotNull(systemService2, "null cannot be cast to non-null type android.app.ActivityManager");
                        if (((ActivityManager) systemService2).isBackgroundRestricted()) {
                            z = true;
                        }
                    }
                    pairArr[2] = TuplesKt.to("backgroundRestricted", Boolean.valueOf(z));
                    return MapsKt.mapOf(pairArr);
                }
            }));
            ModuleDefinitionBuilder moduleDefinitionBuilder6 = moduleDefinitionBuilder;
            if (Intrinsics.areEqual(Boolean.class, Promise.class)) {
                asyncFunctionWithPromiseComponent2 = new AsyncFunctionWithPromiseComponent("setResumeAfterBoot", new AnyType[0], new Function2<Object[], Promise, Unit>() { // from class: expo.modules.shikhsha.ShikhshaPedometerModule$definition$lambda$21$$inlined$AsyncFunction$16
                    /* JADX WARN: Multi-variable type inference failed */
                    /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
                    public final void invoke2(Object[] objArr, Promise promise) {
                        Intrinsics.checkNotNullParameter(objArr, "<unused var>");
                        Intrinsics.checkNotNullParameter(promise, "promise");
                        StepStore.INSTANCE.setResumeAfterBoot(this.this$0.context(), ((Boolean) promise).booleanValue());
                    }

                    @Override // kotlin.jvm.functions.Function2
                    public /* bridge */ /* synthetic */ Unit invoke(Object[] objArr, Promise promise) {
                        invoke2(objArr, promise);
                        return Unit.INSTANCE;
                    }
                });
            } else {
                TypeConverterProvider converters5 = moduleDefinitionBuilder6.getConverters();
                AnyType[] anyTypeArr5 = new AnyType[1];
                AnyType anyType10 = AnyTypeCache.INSTANCE.getTypesMap().get(new Pair(Reflection.getOrCreateKotlinClass(Boolean.class), false));
                if (anyType10 == null) {
                    try {
                        Result.Companion companion19 = Result.INSTANCE;
                        objM783constructorimpl10 = Result.m783constructorimpl(new TypeDescriptor(TypeDescriptorOfKt.toRawTypeDescriptor(PTypeDescriptorRegistry.BOOLEAN), new Function0<KType>() { // from class: expo.modules.shikhsha.ShikhshaPedometerModule$definition$lambda$21$$inlined$AsyncFunction$17
                            @Override // kotlin.jvm.functions.Function0
                            public final KType invoke() {
                                return Reflection.typeOf(Boolean.TYPE);
                            }
                        }));
                    } catch (Throwable th10) {
                        Result.Companion companion20 = Result.INSTANCE;
                        objM783constructorimpl10 = Result.m783constructorimpl(ResultKt.createFailure(th10));
                    }
                    if (Result.m789isFailureimpl(objM783constructorimpl10)) {
                        objM783constructorimpl10 = null;
                    }
                    TypeDescriptor typeDescriptor10 = (TypeDescriptor) objM783constructorimpl10;
                    if (typeDescriptor10 == null) {
                        typeDescriptor10 = TypeDescriptorKt.toTypeDescriptor(Reflection.typeOf(Boolean.TYPE));
                    }
                    anyType10 = new AnyType(typeDescriptor10, converters5);
                }
                anyTypeArr5[0] = anyType10;
                Function1<Object[], Unit> function4 = new Function1<Object[], Unit>() { // from class: expo.modules.shikhsha.ShikhshaPedometerModule$definition$lambda$21$$inlined$AsyncFunction$18
                    @Override // kotlin.jvm.functions.Function1
                    public final Unit invoke(Object[] objArr) {
                        Intrinsics.checkNotNullParameter(objArr, "<destruct>");
                        StepStore.INSTANCE.setResumeAfterBoot(this.this$0.context(), ((Boolean) objArr[0]).booleanValue());
                        return Unit.INSTANCE;
                    }
                };
                if (Intrinsics.areEqual(Unit.class, Integer.TYPE)) {
                    stringAsyncFunctionComponent4 = new IntAsyncFunctionComponent("setResumeAfterBoot", anyTypeArr5, function4);
                } else if (Intrinsics.areEqual(Unit.class, Boolean.TYPE)) {
                    stringAsyncFunctionComponent4 = new BoolAsyncFunctionComponent("setResumeAfterBoot", anyTypeArr5, function4);
                } else if (Intrinsics.areEqual(Unit.class, Double.TYPE)) {
                    stringAsyncFunctionComponent4 = new DoubleAsyncFunctionComponent("setResumeAfterBoot", anyTypeArr5, function4);
                } else if (Intrinsics.areEqual(Unit.class, Float.TYPE)) {
                    stringAsyncFunctionComponent4 = new FloatAsyncFunctionComponent("setResumeAfterBoot", anyTypeArr5, function4);
                } else {
                    stringAsyncFunctionComponent4 = Intrinsics.areEqual(Unit.class, String.class) ? new StringAsyncFunctionComponent("setResumeAfterBoot", anyTypeArr5, function4) : new UntypedAsyncFunctionComponent("setResumeAfterBoot", anyTypeArr5, function4);
                }
                asyncFunctionWithPromiseComponent2 = stringAsyncFunctionComponent4;
            }
            moduleDefinitionBuilder6.getAsyncFunctions().put("setResumeAfterBoot", asyncFunctionWithPromiseComponent2);
            UntypedAsyncFunctionComponent untypedAsyncFunctionComponent2 = new UntypedAsyncFunctionComponent("openBatterySettings", new AnyType[0], new Function1<Object[], Object>() { // from class: expo.modules.shikhsha.ShikhshaPedometerModule$definition$lambda$21$$inlined$AsyncFunctionWithoutArgs$2
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object[] it) {
                    boolean z;
                    Intrinsics.checkNotNullParameter(it, "it");
                    try {
                        this.this$0.context().startActivity(new Intent("android.settings.IGNORE_BATTERY_OPTIMIZATION_SETTINGS").addFlags(268435456));
                        z = true;
                    } catch (Exception unused) {
                        z = false;
                    }
                    return Boolean.valueOf(z);
                }
            });
            moduleDefinitionBuilder.getAsyncFunctions().put("openBatterySettings", untypedAsyncFunctionComponent2);
            untypedAsyncFunctionComponent2.runOnQueue(Queues.MAIN);
            ModuleDefinitionBuilder moduleDefinitionBuilder7 = moduleDefinitionBuilder;
            if (Intrinsics.areEqual(Integer.class, Promise.class)) {
                asyncFunctionWithPromiseComponent3 = new AsyncFunctionWithPromiseComponent("setWidgetGoal", new AnyType[0], new Function2<Object[], Promise, Unit>() { // from class: expo.modules.shikhsha.ShikhshaPedometerModule$definition$lambda$21$$inlined$AsyncFunction$19
                    /* JADX WARN: Multi-variable type inference failed */
                    /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
                    public final void invoke2(Object[] objArr, Promise promise) {
                        Intrinsics.checkNotNullParameter(objArr, "<unused var>");
                        Intrinsics.checkNotNullParameter(promise, "promise");
                        StepWidgetProvider.INSTANCE.saveGoal(this.this$0.context(), ((Integer) promise).intValue());
                    }

                    @Override // kotlin.jvm.functions.Function2
                    public /* bridge */ /* synthetic */ Unit invoke(Object[] objArr, Promise promise) {
                        invoke2(objArr, promise);
                        return Unit.INSTANCE;
                    }
                });
            } else {
                TypeConverterProvider converters6 = moduleDefinitionBuilder7.getConverters();
                AnyType[] anyTypeArr6 = new AnyType[1];
                AnyType anyType11 = AnyTypeCache.INSTANCE.getTypesMap().get(new Pair(Reflection.getOrCreateKotlinClass(Integer.class), false));
                if (anyType11 == null) {
                    try {
                        Result.Companion companion21 = Result.INSTANCE;
                        objM783constructorimpl11 = Result.m783constructorimpl(new TypeDescriptor(TypeDescriptorOfKt.toRawTypeDescriptor(PTypeDescriptorRegistry.INT), new Function0<KType>() { // from class: expo.modules.shikhsha.ShikhshaPedometerModule$definition$lambda$21$$inlined$AsyncFunction$20
                            @Override // kotlin.jvm.functions.Function0
                            public final KType invoke() {
                                return Reflection.typeOf(Integer.TYPE);
                            }
                        }));
                    } catch (Throwable th11) {
                        Result.Companion companion22 = Result.INSTANCE;
                        objM783constructorimpl11 = Result.m783constructorimpl(ResultKt.createFailure(th11));
                    }
                    if (Result.m789isFailureimpl(objM783constructorimpl11)) {
                        objM783constructorimpl11 = null;
                    }
                    TypeDescriptor typeDescriptor11 = (TypeDescriptor) objM783constructorimpl11;
                    if (typeDescriptor11 == null) {
                        typeDescriptor11 = TypeDescriptorKt.toTypeDescriptor(Reflection.typeOf(Integer.TYPE));
                    }
                    anyType11 = new AnyType(typeDescriptor11, converters6);
                }
                anyTypeArr6[0] = anyType11;
                Function1<Object[], Unit> function5 = new Function1<Object[], Unit>() { // from class: expo.modules.shikhsha.ShikhshaPedometerModule$definition$lambda$21$$inlined$AsyncFunction$21
                    @Override // kotlin.jvm.functions.Function1
                    public final Unit invoke(Object[] objArr) {
                        Intrinsics.checkNotNullParameter(objArr, "<destruct>");
                        StepWidgetProvider.INSTANCE.saveGoal(this.this$0.context(), ((Number) objArr[0]).intValue());
                        return Unit.INSTANCE;
                    }
                };
                if (Intrinsics.areEqual(Unit.class, Integer.TYPE)) {
                    stringAsyncFunctionComponent5 = new IntAsyncFunctionComponent("setWidgetGoal", anyTypeArr6, function5);
                } else if (Intrinsics.areEqual(Unit.class, Boolean.TYPE)) {
                    stringAsyncFunctionComponent5 = new BoolAsyncFunctionComponent("setWidgetGoal", anyTypeArr6, function5);
                } else if (Intrinsics.areEqual(Unit.class, Double.TYPE)) {
                    stringAsyncFunctionComponent5 = new DoubleAsyncFunctionComponent("setWidgetGoal", anyTypeArr6, function5);
                } else if (Intrinsics.areEqual(Unit.class, Float.TYPE)) {
                    stringAsyncFunctionComponent5 = new FloatAsyncFunctionComponent("setWidgetGoal", anyTypeArr6, function5);
                } else {
                    stringAsyncFunctionComponent5 = Intrinsics.areEqual(Unit.class, String.class) ? new StringAsyncFunctionComponent("setWidgetGoal", anyTypeArr6, function5) : new UntypedAsyncFunctionComponent("setWidgetGoal", anyTypeArr6, function5);
                }
                asyncFunctionWithPromiseComponent3 = stringAsyncFunctionComponent5;
            }
            moduleDefinitionBuilder7.getAsyncFunctions().put("setWidgetGoal", asyncFunctionWithPromiseComponent3);
            moduleDefinitionBuilder.getAsyncFunctions().put("widgetStatus", new UntypedAsyncFunctionComponent("widgetStatus", new AnyType[0], new Function1<Object[], Object>() { // from class: expo.modules.shikhsha.ShikhshaPedometerModule$definition$lambda$21$$inlined$AsyncFunctionWithoutArgs$3
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object[] it) {
                    Intrinsics.checkNotNullParameter(it, "it");
                    Context context = this.this$0.context();
                    Pair[] pairArr = new Pair[2];
                    boolean z = false;
                    pairArr[0] = TuplesKt.to("count", Integer.valueOf(StepWidgetProvider.INSTANCE.ids(context).length));
                    if (Build.VERSION.SDK_INT >= 26 && AppWidgetManager.getInstance(context).isRequestPinAppWidgetSupported()) {
                        z = true;
                    }
                    pairArr[1] = TuplesKt.to("pinSupported", Boolean.valueOf(z));
                    return MapsKt.mapOf(pairArr);
                }
            }));
            ModuleDefinitionBuilder moduleDefinitionBuilder8 = moduleDefinitionBuilder;
            if (Intrinsics.areEqual(String.class, Promise.class)) {
                asyncFunctionWithPromiseComponent4 = new AsyncFunctionWithPromiseComponent("requestActivityWidget", new AnyType[0], new Function2<Object[], Promise, Unit>() { // from class: expo.modules.shikhsha.ShikhshaPedometerModule$definition$lambda$21$$inlined$AsyncFunction$22
                    /* JADX WARN: Multi-variable type inference failed */
                    /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
                    public final void invoke2(Object[] objArr, Promise promise) {
                        Intrinsics.checkNotNullParameter(objArr, "<unused var>");
                        Intrinsics.checkNotNullParameter(promise, "promise");
                        String str = (String) promise;
                        if (this.this$0.getAppContext().getCurrentActivity() != null) {
                            StepWidgetProvider.INSTANCE.pin(this.this$0.context(), str);
                            return;
                        }
                        throw new IllegalStateException("Открой приложение для добавления виджета.".toString());
                    }

                    @Override // kotlin.jvm.functions.Function2
                    public /* bridge */ /* synthetic */ Unit invoke(Object[] objArr, Promise promise) {
                        invoke2(objArr, promise);
                        return Unit.INSTANCE;
                    }
                });
            } else {
                TypeConverterProvider converters7 = moduleDefinitionBuilder8.getConverters();
                AnyType[] anyTypeArr7 = new AnyType[1];
                AnyType anyType12 = AnyTypeCache.INSTANCE.getTypesMap().get(new Pair(Reflection.getOrCreateKotlinClass(String.class), false));
                if (anyType12 == null) {
                    try {
                        Result.Companion companion23 = Result.INSTANCE;
                        objM783constructorimpl12 = Result.m783constructorimpl(new TypeDescriptor(TypeDescriptorOfKt.toRawTypeDescriptor(PTypeDescriptorRegistry.STRING), new Function0<KType>() { // from class: expo.modules.shikhsha.ShikhshaPedometerModule$definition$lambda$21$$inlined$AsyncFunction$23
                            @Override // kotlin.jvm.functions.Function0
                            public final KType invoke() {
                                return Reflection.typeOf(String.class);
                            }
                        }));
                    } catch (Throwable th12) {
                        Result.Companion companion24 = Result.INSTANCE;
                        objM783constructorimpl12 = Result.m783constructorimpl(ResultKt.createFailure(th12));
                    }
                    if (!Result.m789isFailureimpl(objM783constructorimpl12)) {
                        obj = objM783constructorimpl12;
                    }
                    TypeDescriptor typeDescriptor12 = (TypeDescriptor) obj;
                    if (typeDescriptor12 == null) {
                        typeDescriptor12 = TypeDescriptorKt.toTypeDescriptor(Reflection.typeOf(String.class));
                    }
                    anyType12 = new AnyType(typeDescriptor12, converters7);
                }
                anyTypeArr7[0] = anyType12;
                Function1<Object[], Boolean> function6 = new Function1<Object[], Boolean>() { // from class: expo.modules.shikhsha.ShikhshaPedometerModule$definition$lambda$21$$inlined$AsyncFunction$24
                    @Override // kotlin.jvm.functions.Function1
                    public final Boolean invoke(Object[] objArr) {
                        Intrinsics.checkNotNullParameter(objArr, "<destruct>");
                        String str = (String) objArr[0];
                        if (this.this$0.getAppContext().getCurrentActivity() != null) {
                            return Boolean.valueOf(StepWidgetProvider.INSTANCE.pin(this.this$0.context(), str));
                        }
                        throw new IllegalStateException("Открой приложение для добавления виджета.".toString());
                    }
                };
                if (Intrinsics.areEqual(Boolean.class, Integer.TYPE)) {
                    stringAsyncFunctionComponent6 = new IntAsyncFunctionComponent("requestActivityWidget", anyTypeArr7, function6);
                } else if (Intrinsics.areEqual(Boolean.class, Boolean.TYPE)) {
                    stringAsyncFunctionComponent6 = new BoolAsyncFunctionComponent("requestActivityWidget", anyTypeArr7, function6);
                } else if (Intrinsics.areEqual(Boolean.class, Double.TYPE)) {
                    stringAsyncFunctionComponent6 = new DoubleAsyncFunctionComponent("requestActivityWidget", anyTypeArr7, function6);
                } else if (Intrinsics.areEqual(Boolean.class, Float.TYPE)) {
                    stringAsyncFunctionComponent6 = new FloatAsyncFunctionComponent("requestActivityWidget", anyTypeArr7, function6);
                } else {
                    stringAsyncFunctionComponent6 = Intrinsics.areEqual(Boolean.class, String.class) ? new StringAsyncFunctionComponent("requestActivityWidget", anyTypeArr7, function6) : new UntypedAsyncFunctionComponent("requestActivityWidget", anyTypeArr7, function6);
                }
                asyncFunctionWithPromiseComponent4 = stringAsyncFunctionComponent6;
            }
            moduleDefinitionBuilder8.getAsyncFunctions().put("requestActivityWidget", asyncFunctionWithPromiseComponent4);
            asyncFunctionWithPromiseComponent4.runOnQueue(Queues.MAIN);
            UntypedAsyncFunctionComponent untypedAsyncFunctionComponent3 = new UntypedAsyncFunctionComponent("requestWidget", new AnyType[0], new Function1<Object[], Object>() { // from class: expo.modules.shikhsha.ShikhshaPedometerModule$definition$lambda$21$$inlined$AsyncFunctionWithoutArgs$4
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object[] it) {
                    Intrinsics.checkNotNullParameter(it, "it");
                    if (this.this$0.getAppContext().getCurrentActivity() != null) {
                        return Boolean.valueOf(StepWidgetProvider.Companion.pin$default(StepWidgetProvider.INSTANCE, this.this$0.context(), null, 2, null));
                    }
                    throw new IllegalStateException("Открой приложение для добавления виджета.".toString());
                }
            });
            moduleDefinitionBuilder.getAsyncFunctions().put("requestWidget", untypedAsyncFunctionComponent3);
            untypedAsyncFunctionComponent3.runOnQueue(Queues.MAIN);
            moduleDefinitionBuilder.getAsyncFunctions().put("getSnapshot", new UntypedAsyncFunctionComponent("getSnapshot", new AnyType[0], new Function1<Object[], Object>() { // from class: expo.modules.shikhsha.ShikhshaPedometerModule$definition$lambda$21$$inlined$AsyncFunctionWithoutArgs$5
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object[] it) {
                    Intrinsics.checkNotNullParameter(it, "it");
                    return this.this$0.snapshot();
                }
            }));
            moduleDefinitionBuilder.getAsyncFunctions().put("getLiveSnapshot", new UntypedAsyncFunctionComponent("getLiveSnapshot", new AnyType[0], new Function1<Object[], Object>() { // from class: expo.modules.shikhsha.ShikhshaPedometerModule$definition$lambda$21$$inlined$AsyncFunctionWithoutArgs$6
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object[] it) {
                    Intrinsics.checkNotNullParameter(it, "it");
                    return this.this$0.snapshot();
                }
            }));
            moduleDefinitionBuilder.getAsyncFunctions().put(ViewProps.START, new UntypedAsyncFunctionComponent(ViewProps.START, new AnyType[0], new Function1<Object[], Object>() { // from class: expo.modules.shikhsha.ShikhshaPedometerModule$definition$lambda$21$$inlined$AsyncFunctionWithoutArgs$7
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object[] it) throws Exception {
                    Intrinsics.checkNotNullParameter(it, "it");
                    Context context = this.this$0.context();
                    if (this.this$0.available(context)) {
                        if (!this.this$0.granted(context)) {
                            throw new IllegalStateException("Нет разрешения на физическую активность.".toString());
                        }
                        StepStore.INSTANCE.setEnabled(context, true);
                        try {
                            Intent intent = new Intent(context, (Class<?>) StepService.class);
                            if (Build.VERSION.SDK_INT >= 26) {
                                context.startForegroundService(intent);
                            } else {
                                context.startService(intent);
                            }
                            return this.this$0.snapshot();
                        } catch (Exception e) {
                            StepStore stepStore = StepStore.INSTANCE;
                            String message = e.getMessage();
                            if (message == null) {
                                message = "Не удалось запустить службу.";
                            }
                            stepStore.failure(context, message);
                            throw e;
                        }
                    }
                    throw new IllegalStateException("На телефоне нет аппаратного датчика шагов.".toString());
                }
            }));
            moduleDefinitionBuilder.getAsyncFunctions().put("stop", new UntypedAsyncFunctionComponent("stop", new AnyType[0], new Function1<Object[], Object>() { // from class: expo.modules.shikhsha.ShikhshaPedometerModule$definition$lambda$21$$inlined$AsyncFunctionWithoutArgs$8
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object[] it) {
                    Intrinsics.checkNotNullParameter(it, "it");
                    Context context = this.this$0.context();
                    StepStore.INSTANCE.setEnabled(context, false);
                    context.stopService(new Intent(context, (Class<?>) StepService.class));
                    return this.this$0.snapshot();
                }
            }));
            ModuleDefinitionData moduleDefinitionDataBuildModule = moduleDefinitionBuilder.buildModule();
            Trace.endSection();
            return moduleDefinitionDataBuildModule;
        } catch (Throwable th13) {
            Trace.endSection();
            throw th13;
        }
    }
}
