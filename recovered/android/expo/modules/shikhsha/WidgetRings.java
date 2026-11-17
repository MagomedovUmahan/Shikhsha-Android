package expo.modules.shikhsha;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import com.facebook.imagepipeline.common.RotationOptions;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import org.maplibre.reactnative.components.layer.style.MLRNStyleFactory;

/* JADX INFO: compiled from: WidgetRings.kt */
/* JADX INFO: loaded from: classes3.dex */
@Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0015\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0010\b\n\u0000\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u001f\u0010\b\u001a\u00020\t2\b\u0010\n\u001a\u0004\u0018\u00010\t2\b\u0010\u000b\u001a\u0004\u0018\u00010\t¢\u0006\u0002\u0010\fJ\u001e\u0010\r\u001a\u00020\u000e2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\t0\u00102\b\b\u0002\u0010\u0011\u001a\u00020\u0012R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007¨\u0006\u0013"}, d2 = {"Lexpo/modules/shikhsha/WidgetRings;", "", "<init>", "()V", "colors", "", "getColors", "()[I", "fraction", "", MLRNStyleFactory.VALUE_KEY, "goal", "(Ljava/lang/Double;Ljava/lang/Double;)D", "bitmap", "Landroid/graphics/Bitmap;", "values", "", "size", "", "shikhsha-pedometer_release"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class WidgetRings {
    public static final WidgetRings INSTANCE = new WidgetRings();
    private static final int[] colors = {-5704142, -16722706, -4895233};

    private WidgetRings() {
    }

    public final int[] getColors() {
        return colors;
    }

    public final double fraction(Double value, Double goal) {
        if (value == null || goal == null || Math.abs(value.doubleValue()) > Double.MAX_VALUE || Math.abs(goal.doubleValue()) > Double.MAX_VALUE || goal.doubleValue() <= 0.0d) {
            return 0.0d;
        }
        return RangesKt.coerceIn(value.doubleValue() / goal.doubleValue(), 0.0d, 1.0d);
    }

    public static /* synthetic */ Bitmap bitmap$default(WidgetRings widgetRings, List list, int i, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = RotationOptions.ROTATE_180;
        }
        return widgetRings.bitmap(list, i);
    }

    public final Bitmap bitmap(List<Double> values, int size) {
        Intrinsics.checkNotNullParameter(values, "values");
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Intrinsics.checkNotNullExpressionValue(bitmapCreateBitmap, "createBitmap(...)");
        Canvas canvas = new Canvas(bitmapCreateBitmap);
        Paint paint = new Paint(1);
        paint.setStyle(Paint.Style.STROKE);
        float f = size;
        paint.setStrokeWidth(0.075f * f);
        paint.setStrokeCap(Paint.Cap.ROUND);
        int i = 0;
        while (i < 3) {
            float f2 = (0.435f - (i * 0.12f)) * f;
            float f3 = f / 2.0f;
            float f4 = f3 - f2;
            float f5 = f2 + f3;
            RectF rectF = new RectF(f4, f4, f5, f5);
            paint.setColor(colors[i]);
            paint.setAlpha(45);
            canvas.drawOval(rectF, paint);
            paint.setAlpha(255);
            Double dValueOf = (i < 0 || i >= values.size()) ? Double.valueOf(0.0d) : values.get(i);
            if (Math.abs(dValueOf.doubleValue()) > Double.MAX_VALUE) {
                dValueOf = null;
            }
            Double d = dValueOf;
            double dCoerceIn = d != null ? RangesKt.coerceIn(d.doubleValue(), 0.0d, 1.0d) : 0.0d;
            if (dCoerceIn > 0.0d) {
                canvas.drawArc(rectF, -90.0f, (float) (dCoerceIn * 359.9d), false, paint);
            }
            i++;
        }
        return bitmapCreateBitmap;
    }
}
