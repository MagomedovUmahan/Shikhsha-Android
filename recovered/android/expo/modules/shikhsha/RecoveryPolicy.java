package expo.modules.shikhsha;

import kotlin.Metadata;

/* JADX INFO: compiled from: RecoveryPolicy.kt */
/* JADX INFO: loaded from: classes3.dex */
@Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0005\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J&\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u00052\u0006\u0010\t\u001a\u00020\u0005¨\u0006\n"}, d2 = {"Lexpo/modules/shikhsha/RecoveryPolicy;", "", "<init>", "()V", "shouldResume", "", "userEnabled", "resumeAfterBoot", "permission", "sensor", "shikhsha-pedometer_release"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class RecoveryPolicy {
    public static final RecoveryPolicy INSTANCE = new RecoveryPolicy();

    public final boolean shouldResume(boolean userEnabled, boolean resumeAfterBoot, boolean permission, boolean sensor) {
        return userEnabled && resumeAfterBoot && permission && sensor;
    }

    private RecoveryPolicy() {
    }
}
