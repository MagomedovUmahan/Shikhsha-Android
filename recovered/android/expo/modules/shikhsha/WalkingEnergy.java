package expo.modules.shikhsha;

import kotlin.Metadata;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;

/* JADX INFO: compiled from: WalkingClock.kt */
/* JADX INFO: loaded from: classes3.dex */
@Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0005\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J9\u0010\u0004\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u00052\b\u0010\n\u001a\u0004\u0018\u00010\u00052\b\u0010\u000b\u001a\u0004\u0018\u00010\f¢\u0006\u0002\u0010\rJ1\u0010\u000e\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u000f\u001a\u00020\u00052\b\u0010\n\u001a\u0004\u0018\u00010\u00052\b\u0010\u000b\u001a\u0004\u0018\u00010\f¢\u0006\u0002\u0010\u0010¨\u0006\u0011"}, d2 = {"Lexpo/modules/shikhsha/WalkingEnergy;", "", "<init>", "()V", "netKcal", "", "seconds", "timedSteps", "", "strideCm", "kg", "age", "", "(DJDLjava/lang/Double;Ljava/lang/Integer;)Ljava/lang/Double;", "atSpeed", "speed", "(DDLjava/lang/Double;Ljava/lang/Integer;)Ljava/lang/Double;", "shikhsha-pedometer_release"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class WalkingEnergy {
    public static final WalkingEnergy INSTANCE = new WalkingEnergy();

    private WalkingEnergy() {
    }

    public final Double netKcal(double seconds, long timedSteps, double strideCm, Double kg, Integer age) {
        if (kg == null || Math.abs(kg.doubleValue()) > Double.MAX_VALUE || !RangesKt.rangeTo(30.0d, 300.0d).contains(kg) || age == null || !new IntRange(19, 59).contains(age.intValue()) || seconds < 0.0d || Math.abs(seconds) > Double.MAX_VALUE || timedSteps < 0 || Math.abs(strideCm) > Double.MAX_VALUE || 30.0d > strideCm || strideCm > 150.0d) {
            return null;
        }
        if (seconds == 0.0d || timedSteps == 0) {
            return Double.valueOf(0.0d);
        }
        return atSpeed(seconds, (((timedSteps * strideCm) / 100.0d) / seconds) * 3.6d, kg, age);
    }

    public final Double atSpeed(double seconds, double speed, Double kg, Integer age) {
        double d;
        if (kg == null || Math.abs(kg.doubleValue()) > Double.MAX_VALUE || !RangesKt.rangeTo(30.0d, 300.0d).contains(kg) || age == null || !new IntRange(19, 59).contains(age.intValue()) || Math.abs(seconds) > Double.MAX_VALUE || seconds < 0.0d || speed < 0.0d || Math.abs(speed) > Double.MAX_VALUE || speed >= 8.0d) {
            return null;
        }
        if (speed < 3.2d) {
            d = 2.3d;
        } else if (speed < 4.0d) {
            d = 2.8d;
        } else if (speed < 4.5d) {
            d = 3.0d;
        } else if (speed < 5.6d) {
            d = 3.8d;
        } else if (speed < 6.4d) {
            d = 4.8d;
        } else {
            d = speed < 7.2d ? 5.5d : 7.0d;
        }
        return Double.valueOf((((d - 1.0d) * kg.doubleValue()) * seconds) / 3600.0d);
    }
}
