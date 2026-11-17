package expo.modules.shikhsha;

import com.facebook.common.util.UriUtil;
import com.facebook.react.uimanager.ViewProps;
import kotlin.Metadata;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

/* JADX INFO: compiled from: StepWidgetState.kt */
/* JADX INFO: loaded from: classes3.dex */
@Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\t\bÆ\u0002\u0018\u00002\u00020\u0001:\u0002\u0012\u0013B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003JO\u0010\u0004\u001a\u00020\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u00072\b\u0010\b\u001a\u0004\u0018\u00010\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000b2\u0006\u0010\r\u001a\u00020\u000b2\u0006\u0010\u000e\u001a\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\u000b2\u0006\u0010\u0010\u001a\u00020\u000b¢\u0006\u0002\u0010\u0011¨\u0006\u0014"}, d2 = {"Lexpo/modules/shikhsha/StepWidgetState;", "", "<init>", "()V", UriUtil.LOCAL_CONTENT_SCHEME, "Lexpo/modules/shikhsha/StepWidgetState$Content;", "steps", "", "goal", "", ViewProps.ENABLED, "", "running", "available", "permission", "failed", "hasHistory", "(Ljava/lang/Long;Ljava/lang/Integer;ZZZZZZ)Lexpo/modules/shikhsha/StepWidgetState$Content;", "Status", "Content", "shikhsha-pedometer_release"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class StepWidgetState {
    public static final StepWidgetState INSTANCE = new StepWidgetState();

    /* JADX INFO: compiled from: StepWidgetState.kt */
    @Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\n\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\n¨\u0006\u000b"}, d2 = {"Lexpo/modules/shikhsha/StepWidgetState$Status;", "", "<init>", "(Ljava/lang/String;I)V", "UNAVAILABLE", "PERMISSION", "ERROR", "PAUSED", "STOPPED", "ACTIVE", "NOT_STARTED", "shikhsha-pedometer_release"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public enum Status {
        UNAVAILABLE,
        PERMISSION,
        ERROR,
        PAUSED,
        STOPPED,
        ACTIVE,
        NOT_STARTED;

        private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries(values());

        public static EnumEntries<Status> getEntries() {
            return $ENTRIES;
        }
    }

    private StepWidgetState() {
    }

    /* JADX INFO: compiled from: StepWidgetState.kt */
    @Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0013\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B+\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\b¢\u0006\u0004\b\t\u0010\nJ\u0010\u0010\u0015\u001a\u0004\u0018\u00010\u0003HÆ\u0003¢\u0006\u0002\u0010\fJ\u0010\u0010\u0016\u001a\u0004\u0018\u00010\u0005HÆ\u0003¢\u0006\u0002\u0010\u000fJ\t\u0010\u0017\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0018\u001a\u00020\bHÆ\u0003J:\u0010\u0019\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\bHÆ\u0001¢\u0006\u0002\u0010\u001aJ\u0013\u0010\u001b\u001a\u00020\u001c2\b\u0010\u001d\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u001e\u001a\u00020\u0005HÖ\u0001J\t\u0010\u001f\u001a\u00020 HÖ\u0001R\u0015\u0010\u0002\u001a\u0004\u0018\u00010\u0003¢\u0006\n\n\u0002\u0010\r\u001a\u0004\b\u000b\u0010\fR\u0015\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\n\n\u0002\u0010\u0010\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0006\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\u0007\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014¨\u0006!"}, d2 = {"Lexpo/modules/shikhsha/StepWidgetState$Content;", "", "steps", "", "goal", "", "progress", "status", "Lexpo/modules/shikhsha/StepWidgetState$Status;", "<init>", "(Ljava/lang/Long;Ljava/lang/Integer;ILexpo/modules/shikhsha/StepWidgetState$Status;)V", "getSteps", "()Ljava/lang/Long;", "Ljava/lang/Long;", "getGoal", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "getProgress", "()I", "getStatus", "()Lexpo/modules/shikhsha/StepWidgetState$Status;", "component1", "component2", "component3", "component4", "copy", "(Ljava/lang/Long;Ljava/lang/Integer;ILexpo/modules/shikhsha/StepWidgetState$Status;)Lexpo/modules/shikhsha/StepWidgetState$Content;", "equals", "", "other", "hashCode", "toString", "", "shikhsha-pedometer_release"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final /* data */ class Content {
        private final Integer goal;
        private final int progress;
        private final Status status;
        private final Long steps;

        public static /* synthetic */ Content copy$default(Content content, Long l, Integer num, int i, Status status, int i2, Object obj) {
            if ((i2 & 1) != 0) {
                l = content.steps;
            }
            if ((i2 & 2) != 0) {
                num = content.goal;
            }
            if ((i2 & 4) != 0) {
                i = content.progress;
            }
            if ((i2 & 8) != 0) {
                status = content.status;
            }
            return content.copy(l, num, i, status);
        }

        /* JADX INFO: renamed from: component1, reason: from getter */
        public final Long getSteps() {
            return this.steps;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final Integer getGoal() {
            return this.goal;
        }

        /* JADX INFO: renamed from: component3, reason: from getter */
        public final int getProgress() {
            return this.progress;
        }

        /* JADX INFO: renamed from: component4, reason: from getter */
        public final Status getStatus() {
            return this.status;
        }

        public final Content copy(Long steps, Integer goal, int progress, Status status) {
            Intrinsics.checkNotNullParameter(status, "status");
            return new Content(steps, goal, progress, status);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Content)) {
                return false;
            }
            Content content = (Content) other;
            return Intrinsics.areEqual(this.steps, content.steps) && Intrinsics.areEqual(this.goal, content.goal) && this.progress == content.progress && this.status == content.status;
        }

        public int hashCode() {
            Long l = this.steps;
            int iHashCode = (l == null ? 0 : l.hashCode()) * 31;
            Integer num = this.goal;
            return ((((iHashCode + (num != null ? num.hashCode() : 0)) * 31) + Integer.hashCode(this.progress)) * 31) + this.status.hashCode();
        }

        public String toString() {
            return "Content(steps=" + this.steps + ", goal=" + this.goal + ", progress=" + this.progress + ", status=" + this.status + ")";
        }

        public Content(Long l, Integer num, int i, Status status) {
            Intrinsics.checkNotNullParameter(status, "status");
            this.steps = l;
            this.goal = num;
            this.progress = i;
            this.status = status;
        }

        public final Integer getGoal() {
            return this.goal;
        }

        public final int getProgress() {
            return this.progress;
        }

        public final Status getStatus() {
            return this.status;
        }

        public final Long getSteps() {
            return this.steps;
        }
    }

    public final Content content(Long steps, Integer goal, boolean enabled, boolean running, boolean available, boolean permission, boolean failed, boolean hasHistory) {
        Status status;
        int iIntValue;
        Long lValueOf = steps != null ? Long.valueOf(RangesKt.coerceAtLeast(steps.longValue(), 0L)) : null;
        if (goal == null || 1000 > (iIntValue = goal.intValue()) || iIntValue >= 50001) {
            goal = null;
        }
        int iCoerceIn = (lValueOf == null || goal == null) ? 0 : (int) RangesKt.coerceIn((lValueOf.longValue() / ((double) goal.intValue())) * 1000.0d, 0.0d, 1000.0d);
        if (!available) {
            status = Status.UNAVAILABLE;
        } else if (!permission) {
            status = Status.PERMISSION;
        } else if (failed) {
            status = Status.ERROR;
        } else if (!enabled && hasHistory) {
            status = Status.PAUSED;
        } else if (!enabled) {
            status = Status.NOT_STARTED;
        } else if (!running) {
            status = Status.STOPPED;
        } else {
            status = Status.ACTIVE;
        }
        return new Content(lValueOf, goal, iCoerceIn, status);
    }
}
