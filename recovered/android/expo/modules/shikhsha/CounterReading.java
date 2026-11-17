package expo.modules.shikhsha;

import kotlin.Metadata;

/* JADX INFO: compiled from: StepAccumulator.kt */
/* JADX INFO: loaded from: classes3.dex */
@Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0002\b\r\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0003¢\u0006\u0004\b\u0007\u0010\bJ\t\u0010\u000e\u001a\u00020\u0003HÆ\u0003J\t\u0010\u000f\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0010\u001a\u00020\u0003HÆ\u0003J'\u0010\u0011\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\u0012\u001a\u00020\u00132\b\u0010\u0014\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0015\u001a\u00020\u0005HÖ\u0001J\t\u0010\u0016\u001a\u00020\u0017HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0006\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\n¨\u0006\u0018"}, d2 = {"Lexpo/modules/shikhsha/CounterReading;", "", "total", "", "boot", "", "timestamp", "<init>", "(JIJ)V", "getTotal", "()J", "getBoot", "()I", "getTimestamp", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "toString", "", "shikhsha-pedometer_release"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class CounterReading {
    private final int boot;
    private final long timestamp;
    private final long total;

    public static /* synthetic */ CounterReading copy$default(CounterReading counterReading, long j, int i, long j2, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            j = counterReading.total;
        }
        long j3 = j;
        if ((i2 & 2) != 0) {
            i = counterReading.boot;
        }
        int i3 = i;
        if ((i2 & 4) != 0) {
            j2 = counterReading.timestamp;
        }
        return counterReading.copy(j3, i3, j2);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final long getTotal() {
        return this.total;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final int getBoot() {
        return this.boot;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final long getTimestamp() {
        return this.timestamp;
    }

    public final CounterReading copy(long total, int boot, long timestamp) {
        return new CounterReading(total, boot, timestamp);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CounterReading)) {
            return false;
        }
        CounterReading counterReading = (CounterReading) other;
        return this.total == counterReading.total && this.boot == counterReading.boot && this.timestamp == counterReading.timestamp;
    }

    public int hashCode() {
        return (((Long.hashCode(this.total) * 31) + Integer.hashCode(this.boot)) * 31) + Long.hashCode(this.timestamp);
    }

    public String toString() {
        return "CounterReading(total=" + this.total + ", boot=" + this.boot + ", timestamp=" + this.timestamp + ")";
    }

    public CounterReading(long j, int i, long j2) {
        this.total = j;
        this.boot = i;
        this.timestamp = j2;
    }

    public final int getBoot() {
        return this.boot;
    }

    public final long getTimestamp() {
        return this.timestamp;
    }

    public final long getTotal() {
        return this.total;
    }
}
