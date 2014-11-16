package lenguyenthanh.facememo.ui.widgets;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

/**
 * @author lenguyenthanh on 9/12/14.
 */
public class CircleDrawable extends Drawable {

    private final BitmapShader mBitmapShader;
    private final Paint mPaint;
    private Bitmap bitmap;
    private float cx;
    private float cy;
    private float mRadius;
    public CircleDrawable(Bitmap bitmap) {
        this(bitmap, 0);
    }

    public CircleDrawable(Bitmap bitmap, int margin) {
        this.bitmap = bitmap;
        mBitmapShader = new BitmapShader(bitmap,
                Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setShader(mBitmapShader);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        cx = bounds.centerX();
        cy = bounds.centerY();
        mRadius = Math.max(bounds.width(), bounds.height()) >> 1;
        int min = Math.min(bounds.width(), bounds.height()) >> 1;
        Matrix matrix = new Matrix();
        matrix.setScale(mRadius / min,  mRadius / min, cx, cy);
        mBitmapShader.setLocalMatrix(matrix);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(cx, cy, mRadius, mPaint);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    @Override
    public int getIntrinsicHeight() {
        return bitmap.getHeight();
    }

    @Override
    public int getIntrinsicWidth() {
        return bitmap.getWidth();
    }
}
