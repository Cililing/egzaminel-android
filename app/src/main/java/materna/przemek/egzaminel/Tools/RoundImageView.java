package materna.przemek.egzaminel.Tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

public class RoundImageView extends android.support.v7.widget.AppCompatImageView {

    final int color = 0xffa1977d;
    final float radiusChanger = 0.7f;

    public RoundImageView(Context context) {
        super(context);
    }

    public RoundImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();

        if (drawable == null || getWidth() == 0 || getHeight() == 0) {
            return;
        }

        Bitmap b = ((BitmapDrawable) drawable).getBitmap();
        Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);

        int radius = getWidth();
        //int h = getHeight();

        Bitmap roundBitmap = getCroppedBitmap(bitmap, radius);
        canvas.drawBitmap(roundBitmap, 0, 0, null);

    }

    private Bitmap getCroppedBitmap(Bitmap bitmap, int radius) {

        Bitmap scaled;

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();


        if (width != radius && height != radius) {
            float smallest = Math.min(width, height);
            float factor = smallest / radius;
            scaled = Bitmap.createScaledBitmap(
                    bitmap,
                    (int) (width / factor),
                    (int) (height / factor),
                    false);

        } else {
            scaled = bitmap;
        }

        Bitmap output = Bitmap.createBitmap(radius, radius, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, radius, radius);

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor(String.valueOf(color)));
        canvas.drawCircle(
                radius / 2 + radiusChanger,
                radius / 2 + radiusChanger,
                radius / 2 + 0.1f,
                paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(scaled, rect, rect, paint);

        return output;
    }
}
