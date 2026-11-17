package expo.modules.shikhsha;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: StepAccumulator.kt */
/* JADX INFO: loaded from: classes3.dex */
@Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0018\u0010\u0004\u001a\u00020\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u00072\u0006\u0010\b\u001a\u00020\u0007¨\u0006\t"}, d2 = {"Lexpo/modules/shikhsha/StepAccumulator;", "", "<init>", "()V", "delta", "", "previous", "Lexpo/modules/shikhsha/CounterReading;", "next", "shikhsha-pedometer_release"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class StepAccumulator {
    public static final StepAccumulator INSTANCE = new StepAccumulator();

    private StepAccumulator() {
    }

    public final long delta(CounterReading previous, CounterReading next) {
        Intrinsics.checkNotNullParameter(next, "next");
        if (previous == null || previous.getBoot() != next.getBoot() || next.getTimestamp() <= previous.getTimestamp() || next.getTotal() < previous.getTotal()) {
            return 0L;
        }
        return next.getTotal() - previous.getTotal();
    }
}
