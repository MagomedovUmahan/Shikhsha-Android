package expo.modules.shikhsha;

import java.util.ArrayDeque;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import org.maplibre.reactnative.components.layer.style.MLRNStyleFactory;

/* JADX INFO: compiled from: WalkingClock.kt */
/* JADX INFO: loaded from: classes3.dex */
@Metadata(d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B#\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006¢\u0006\u0004\b\u0007\u0010\bJ\u000e\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0003R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\u0004\u001a\u0004\u0018\u00010\u0003X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\tR\u0012\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\nR\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00030\fX\u0082\u0004¢\u0006\u0002\n\u0000R$\u0010\u000e\u001a\u0004\u0018\u00010\u00032\b\u0010\r\u001a\u0004\u0018\u00010\u0003@BX\u0086\u000e¢\u0006\n\n\u0002\u0010\t\u001a\u0004\b\u000f\u0010\u0010R\u001e\u0010\u0012\u001a\u00020\u00112\u0006\u0010\r\u001a\u00020\u0011@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u000e\u0010\u0015\u001a\u00020\u0011X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u0019"}, d2 = {"Lexpo/modules/shikhsha/WalkingEnergyAccumulator;", "", "stride", "", "kg", "age", "", "<init>", "(DLjava/lang/Double;Ljava/lang/Integer;)V", "Ljava/lang/Double;", "Ljava/lang/Integer;", "cadence", "Ljava/util/ArrayDeque;", MLRNStyleFactory.VALUE_KEY, "calories", "getCalories", "()Ljava/lang/Double;", "", "partial", "getPartial", "()Z", "hasEstimate", "add", "", "seconds", "shikhsha-pedometer_release"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class WalkingEnergyAccumulator {
    private final Integer age;
    private final ArrayDeque<Double> cadence = new ArrayDeque<>();
    private Double calories;
    private boolean hasEstimate;
    private final Double kg;
    private boolean partial;
    private final double stride;

    public WalkingEnergyAccumulator(double d, Double d2, Integer num) {
        this.stride = d;
        this.kg = d2;
        this.age = num;
        this.calories = WalkingEnergy.INSTANCE.netKcal(0.0d, 0L, d, d2, num);
    }

    public final Double getCalories() {
        return this.calories;
    }

    public final boolean getPartial() {
        return this.partial;
    }

    public final void add(double seconds) {
        ArrayDeque<Double> arrayDeque = this.cadence;
        if (seconds <= 0.0d) {
            arrayDeque.clear();
            return;
        }
        arrayDeque.addLast(Double.valueOf(seconds));
        if (this.cadence.size() > 10) {
            this.cadence.removeFirst();
        }
        Double dAtSpeed = WalkingEnergy.INSTANCE.atSpeed(seconds, (((((double) this.cadence.size()) * this.stride) / 100.0d) / CollectionsKt.sumOfDouble(this.cadence)) * 3.6d, this.kg, this.age);
        if (dAtSpeed == null) {
            this.partial = true;
            if (this.hasEstimate) {
                return;
            }
            this.calories = null;
            return;
        }
        Double d = this.calories;
        this.calories = Double.valueOf((d != null ? d.doubleValue() : 0.0d) + dAtSpeed.doubleValue());
        this.hasEstimate = true;
    }
}
