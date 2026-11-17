package expo.modules.shikhsha;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: compiled from: WalkingClock.kt */
/* JADX INFO: loaded from: classes3.dex */
@Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001:\u0001\u0010B\u0011\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J \u0010\u000b\u001a\u0004\u0018\u00010\f2\u0006\u0010\r\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\u00032\u0006\u0010\u000f\u001a\u00020\nR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\u0006\u001a\u0004\u0018\u00010\u0003X\u0082\u000e¢\u0006\u0004\n\u0002\u0010\u0007R\u000e\u0010\b\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u0011"}, d2 = {"Lexpo/modules/shikhsha/WalkingClock;", "", "notBeforeNanos", "", "<init>", "(J)V", "nanos", "Ljava/lang/Long;", "wall", "day", "", "step", "Lexpo/modules/shikhsha/WalkingClock$Span;", "atNanos", "atMillis", "dayKey", "Span", "shikhsha-pedometer_release"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class WalkingClock {
    private String day;
    private Long nanos;
    private final long notBeforeNanos;
    private long wall;

    public WalkingClock() {
        this(0L, 1, null);
    }

    /* JADX INFO: compiled from: WalkingClock.kt */
    @Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u000f\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0004\b\b\u0010\tJ\t\u0010\u0010\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0011\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0012\u001a\u00020\u0007HÆ\u0003J'\u0010\u0013\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0007HÆ\u0001J\u0013\u0010\u0014\u001a\u00020\u00072\b\u0010\u0015\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0016\u001a\u00020\u0017HÖ\u0001J\t\u0010\u0018\u001a\u00020\u0019HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f¨\u0006\u001a"}, d2 = {"Lexpo/modules/shikhsha/WalkingClock$Span;", "", "at", "", "seconds", "", "newBout", "", "<init>", "(JDZ)V", "getAt", "()J", "getSeconds", "()D", "getNewBout", "()Z", "component1", "component2", "component3", "copy", "equals", "other", "hashCode", "", "toString", "", "shikhsha-pedometer_release"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final /* data */ class Span {
        private final long at;
        private final boolean newBout;
        private final double seconds;

        public static /* synthetic */ Span copy$default(Span span, long j, double d, boolean z, int i, Object obj) {
            if ((i & 1) != 0) {
                j = span.at;
            }
            long j2 = j;
            if ((i & 2) != 0) {
                d = span.seconds;
            }
            double d2 = d;
            if ((i & 4) != 0) {
                z = span.newBout;
            }
            return span.copy(j2, d2, z);
        }

        /* JADX INFO: renamed from: component1, reason: from getter */
        public final long getAt() {
            return this.at;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final double getSeconds() {
            return this.seconds;
        }

        /* JADX INFO: renamed from: component3, reason: from getter */
        public final boolean getNewBout() {
            return this.newBout;
        }

        public final Span copy(long at, double seconds, boolean newBout) {
            return new Span(at, seconds, newBout);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Span)) {
                return false;
            }
            Span span = (Span) other;
            return this.at == span.at && Double.compare(this.seconds, span.seconds) == 0 && this.newBout == span.newBout;
        }

        public int hashCode() {
            return (((Long.hashCode(this.at) * 31) + Double.hashCode(this.seconds)) * 31) + Boolean.hashCode(this.newBout);
        }

        public String toString() {
            return "Span(at=" + this.at + ", seconds=" + this.seconds + ", newBout=" + this.newBout + ")";
        }

        public Span(long j, double d, boolean z) {
            this.at = j;
            this.seconds = d;
            this.newBout = z;
        }

        public final long getAt() {
            return this.at;
        }

        public final boolean getNewBout() {
            return this.newBout;
        }

        public final double getSeconds() {
            return this.seconds;
        }
    }

    public WalkingClock(long j) {
        this.notBeforeNanos = j;
        this.day = "";
    }

    public /* synthetic */ WalkingClock(long j, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? 0L : j);
    }

    /* JADX WARN: Code restructure failed: missing block: B:7:0x0019, code lost:

        if (r9 <= r0.longValue()) goto L31;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final expo.modules.shikhsha.WalkingClock.Span step(long r9, long r11, java.lang.String r13) {
        /*
            r8 = this;
            java.lang.String r0 = "dayKey"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r13, r0)
            long r0 = r8.notBeforeNanos
            int r0 = (r9 > r0 ? 1 : (r9 == r0 ? 0 : -1))
            r1 = 0
            if (r0 < 0) goto L8c
            java.lang.Long r0 = r8.nanos
            if (r0 == 0) goto L1d
            kotlin.jvm.internal.Intrinsics.checkNotNull(r0)
            long r2 = r0.longValue()
            int r0 = (r9 > r2 ? 1 : (r9 == r2 ? 0 : -1))
            if (r0 > 0) goto L1d
            goto L8c
        L1d:
            java.lang.Long r0 = r8.nanos
            if (r0 == 0) goto L34
            java.lang.Number r0 = (java.lang.Number) r0
            long r0 = r0.longValue()
            long r0 = r9 - r0
            double r0 = (double) r0
            r2 = 4741671816366391296(0x41cdcd6500000000, double:1.0E9)
            double r0 = r0 / r2
            java.lang.Double r1 = java.lang.Double.valueOf(r0)
        L34:
            r2 = 4617315517961601024(0x4014000000000000, double:5.0)
            if (r1 == 0) goto L65
            double r4 = r1.doubleValue()
            r6 = 4633641066610819072(0x404e000000000000, double:60.0)
            int r0 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r0 > 0) goto L65
            java.lang.String r0 = r8.day
            boolean r0 = kotlin.jvm.internal.Intrinsics.areEqual(r13, r0)
            if (r0 == 0) goto L65
            long r4 = r8.wall
            long r4 = r11 - r4
            double r4 = (double) r4
            r6 = 4652007308841189376(0x408f400000000000, double:1000.0)
            double r4 = r4 / r6
            double r6 = r1.doubleValue()
            double r4 = r4 - r6
            double r4 = java.lang.Math.abs(r4)
            int r0 = (r4 > r2 ? 1 : (r4 == r2 ? 0 : -1))
            if (r0 <= 0) goto L63
            goto L65
        L63:
            r0 = 0
            goto L66
        L65:
            r0 = 1
        L66:
            if (r0 != 0) goto L77
            if (r1 == 0) goto L77
            double r4 = r1.doubleValue()
            int r2 = (r4 > r2 ? 1 : (r4 == r2 ? 0 : -1))
            if (r2 > 0) goto L77
            double r1 = r1.doubleValue()
            goto L79
        L77:
            r1 = 0
        L79:
            java.lang.Long r9 = java.lang.Long.valueOf(r9)
            r8.nanos = r9
            r8.wall = r11
            r8.day = r13
            expo.modules.shikhsha.WalkingClock$Span r8 = new expo.modules.shikhsha.WalkingClock$Span
            r9 = r11
            r13 = r0
            r11 = r1
            r8.<init>(r9, r11, r13)
            return r8
        L8c:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: expo.modules.shikhsha.WalkingClock.step(long, long, java.lang.String):expo.modules.shikhsha.WalkingClock$Span");
    }
}
