package expo.modules.shikhsha;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: WalkingStore.kt */
/* JADX INFO: loaded from: classes3.dex */
@Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0002\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\u0010\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH\u0016J \u0010\n\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\fH\u0016¨\u0006\u000e"}, d2 = {"Lexpo/modules/shikhsha/WalkingDatabase;", "Landroid/database/sqlite/SQLiteOpenHelper;", "c", "Landroid/content/Context;", "<init>", "(Landroid/content/Context;)V", "onCreate", "", "db", "Landroid/database/sqlite/SQLiteDatabase;", "onUpgrade", "oldVersion", "", "newVersion", "shikhsha-pedometer_release"}, k = 1, mv = {2, 2, 0}, xi = 48)
final class WalkingDatabase extends SQLiteOpenHelper {
    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Intrinsics.checkNotNullParameter(db, "db");
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public WalkingDatabase(Context c) {
        super(c, "shikhsha-motion-v1.db", (SQLiteDatabase.CursorFactory) null, 1);
        Intrinsics.checkNotNullParameter(c, "c");
        setWriteAheadLoggingEnabled(true);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase db) {
        Intrinsics.checkNotNullParameter(db, "db");
        db.execSQL("CREATE TABLE bouts (id INTEGER PRIMARY KEY AUTOINCREMENT, day TEXT NOT NULL, start INTEGER NOT NULL, last INTEGER NOT NULL, steps INTEGER NOT NULL, seconds REAL NOT NULL, timed INTEGER NOT NULL, stride REAL NOT NULL, weight REAL, age INTEGER, kcal REAL, walk_id TEXT, missing INTEGER NOT NULL DEFAULT 0)");
        db.execSQL("CREATE INDEX bouts_day ON bouts(day, start)");
        db.execSQL("CREATE INDEX bouts_walk ON bouts(walk_id)");
        db.execSQL("CREATE TABLE coverage (day TEXT PRIMARY KEY, since INTEGER NOT NULL)");
    }
}
